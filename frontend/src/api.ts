export type User = { id: number; username: string; email: string; createdAt: string | null }
export type Profile = { id: number; userId: number; publicName: string; description: string | null; reputation: number; avatarFileId: number | null; locationId: number | null }
export type Account = { user: User; profile: Profile | null }
export type Registration = { username: string; email: string; password: string; confirmPassword: string }
export class ApiError extends Error {
  constructor(public status: number, message: string) { super(message) }
}
const base = (import.meta.env.VITE_API_BASE_URL || '/api').replace(/\/$/, '')
export async function request<T>(path: string, options: { method?: string; body?: unknown; token?: string; signal?: AbortSignal } = {}): Promise<T> {
  let response: Response
  try {
    response = await fetch(`${base}${path}`, {
      method: options.method || 'GET',
      headers: { Accept: 'application/json', ...(options.body ? { 'Content-Type': 'application/json' } : {}), ...(options.token ? { Authorization: `Bearer ${options.token}` } : {}) },
      body: options.body ? JSON.stringify(options.body) : undefined,
      signal: options.signal ? AbortSignal.any([options.signal, AbortSignal.timeout(15000)]) : AbortSignal.timeout(15000),
    })
  } catch (error) {
    if (error instanceof DOMException && error.name === 'AbortError') throw error
    throw new ApiError(0, 'Не удалось связаться с сервером. Проверьте подключение и попробуйте ещё раз.')
  }
  const text = await response.text()
  let data: any = null
  try { data = text ? JSON.parse(text) : null } catch { /* Empty proxy errors are handled by status. */ }
  if (!response.ok) {
    if (response.status === 401) throw new ApiError(401, path === '/auth' ? 'Неверный логин или пароль.' : 'Сессия завершилась. Войдите снова.')
    if (response.status >= 500) throw new ApiError(response.status, 'Сервер временно недоступен. Попробуйте позже.')
    const detail = data?.detailedMessage || data?.message || ''
    const message = /already|duplicate|unique/i.test(detail) ? 'Эти данные уже используются. Проверьте логин и почту.' : /Passwords are not equal/i.test(detail) ? 'Пароли не совпадают.' : response.status === 403 ? 'Недостаточно прав для этого действия.' : 'Не удалось сохранить данные. Проверьте поля и попробуйте снова.'
    throw new ApiError(response.status, message)
  }
  return data as T
}
export const api = {
  login: (username: string, password: string) => request<{ token: string }>('/auth', { method: 'POST', body: { username, password } }),
  register: (body: Registration) => request<User>('/registration', { method: 'POST', body }),
  me: (token: string, signal?: AbortSignal) => request<Account>('/user/me', { token, signal }),
  createProfile: (token: string, userId: number, publicName: string, description: string) => request<Profile>('/registration/create_profile', { method: 'POST', token, body: { userId, publicName, description, avatarFileId: null, locationId: null } }),
}
