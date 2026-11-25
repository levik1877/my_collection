import { createContext, useContext, useEffect, useRef, useState, type ReactNode } from 'react'
import { api, ApiError, type Account } from './api'

const storageKey = 'mycollection.session'
function storedToken() { try { return sessionStorage.getItem(storageKey) } catch { return null } }
function persist(token: string | null) { try { token ? sessionStorage.setItem(storageKey, token) : sessionStorage.removeItem(storageKey) } catch { /* An in-memory session still works. */ } }
export function tokenExpiresAt(token: string): number | null {
  try { const payload = JSON.parse(atob(token.split('.')[1].replace(/-/g, '+').replace(/_/g, '/'))); return typeof payload.exp === 'number' ? payload.exp * 1000 : null } catch { return null }
}
type Auth = { token: string | null; account: Account | null; loading: boolean; error: string; login: (username: string, password: string) => Promise<void>; logout: () => void; reload: () => void; createProfile: (name: string, description: string) => Promise<void> }
const Context = createContext<Auth | null>(null)
export function AuthProvider({ children }: { children: ReactNode }) {
  const [token, setToken] = useState(storedToken)
  const [account, setAccount] = useState<Account | null>(null)
  const [loading, setLoading] = useState(!!token)
  const [error, setError] = useState('')
  const [attempt, setAttempt] = useState(0)
  const generation = useRef(0)
  function logout() { generation.current++; persist(null); setToken(null); setAccount(null); setLoading(false); setError('') }
  useEffect(() => {
    if (!token) return
    const controller = new AbortController()
    setLoading(true); setError('')
    api.me(token, controller.signal).then(setAccount).catch(err => {
      if (controller.signal.aborted) return
      if (err instanceof ApiError && err.status === 401) { logout(); setError(err.message) }
      else setError(err.message)
    }).finally(() => { if (!controller.signal.aborted) setLoading(false) })
    return () => controller.abort()
  }, [token, attempt])
  useEffect(() => {
    if (!token) return
    const expiry = tokenExpiresAt(token)
    if (!expiry) return
    const expire = () => { logout(); setError('Сессия завершилась. Войдите снова.') }
    const timer = window.setTimeout(expire, Math.max(0, Math.min(expiry - Date.now(), 2147483647)))
    return () => clearTimeout(timer)
  }, [token])
  async function login(username: string, password: string) {
    const current = ++generation.current
    const result = await api.login(username, password)
    if (!result?.token) throw new Error('Сервер не вернул токен входа.')
    if (current !== generation.current) return
    persist(result.token); setAccount(null); setLoading(true); setError(''); setToken(result.token)
  }
  async function createProfile(name: string, description: string) {
    if (!token || !account) throw new Error('Войдите в аккаунт, чтобы создать профиль.')
    const current = generation.current
    try {
      const profile = await api.createProfile(token, account.user.id, name.trim(), description.trim())
      if (current === generation.current) setAccount(value => value ? { ...value, profile } : value)
    } catch (err) { if (err instanceof ApiError && err.status === 401) logout(); throw err }
  }
  return <Context.Provider value={{ token, account, loading, error, login, logout, reload: () => setAttempt(x => x + 1), createProfile }}>{children}</Context.Provider>
}
export function useAuth() { const value = useContext(Context); if (!value) throw new Error('AuthProvider is required'); return value }
