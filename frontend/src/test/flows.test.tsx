import { render, screen, waitFor, within } from '@testing-library/react'
import userEvent from '@testing-library/user-event'
import { MemoryRouter } from 'react-router-dom'
import { describe, expect, it, vi } from 'vitest'
import App from '../App'
import { AuthProvider } from '../auth'

const user = { id: 7, username: 'collector', email: 'collector@example.test', createdAt: '2026-11-25' }
const profile = { id: 3, userId: 7, publicName: 'Коллекционер', description: 'Собираю монеты', reputation: 0, avatarFileId: null, locationId: null }
const token = `test.${btoa(JSON.stringify({ sub: 'collector', exp: Math.floor(Date.now() / 1000) + 3600 }))}.test`
const json = (data: unknown, status = 200) => new Response(JSON.stringify(data), { status, headers: { 'Content-Type': 'application/json' } })
function renderApp(path = '/') { return render(<MemoryRouter initialEntries={[path]}><AuthProvider><App/></AuthProvider></MemoryRouter>) }
function mockApi(profileValue: typeof profile | null = profile) {
  return vi.fn(async (url: string, options?: RequestInit) => {
    if (url === '/api/auth') return json({ token })
    if (url === '/api/registration') return json(user)
    if (url === '/api/user/me') return json({ user, profile: profileValue })
    if (url === '/api/registration/create_profile') return json({ ...profile, ...JSON.parse(String(options?.body)) })
    throw new Error(`Unexpected request ${url}`)
  })
}
describe('MyCollection account flow', () => {
  it('logs in using real endpoint contract, swaps header actions, loads profile and logs out', async () => {
    const fetchMock = mockApi(); vi.stubGlobal('fetch', fetchMock); const actor = userEvent.setup(); renderApp('/login')
    await actor.type(screen.getByLabelText('Логин'), 'collector'); await actor.type(screen.getByLabelText('Пароль', { exact: true }), 'password123')
    await actor.click(screen.getByRole('button', { name: 'Войти' }))
    await screen.findByRole('heading', { name: 'Мой профиль' })
    expect(screen.getByRole('link', { name: 'Профиль' })).toBeTruthy()
    expect(screen.queryByRole('link', { name: 'Регистрация' })).toBeNull()
    expect(screen.getByText('Коллекционер')).toBeTruthy()
    expect(fetchMock.mock.calls.find(c => c[0] === '/api/auth')?.[1]?.body).toBe(JSON.stringify({ username: 'collector', password: 'password123' }))
    expect(fetchMock.mock.calls.find(c => c[0] === '/api/user/me')?.[1]?.headers).toMatchObject({ Authorization: `Bearer ${token}` })
    await actor.click(screen.getByRole('button', { name: 'Выйти' }))
    expect(sessionStorage.getItem('mycollection.session')).toBeNull()
    expect(screen.getByRole('link', { name: 'Регистрация' })).toBeTruthy()
  })
  it('registers, signs in, and creates a profile with the authenticated user id', async () => {
    const fetchMock = mockApi(null); vi.stubGlobal('fetch', fetchMock); const actor = userEvent.setup(); renderApp('/register')
    await actor.type(screen.getByLabelText('Электронная почта'), user.email)
    await actor.type(screen.getByLabelText('Логин'), 'collector')
    await actor.type(screen.getByLabelText('Пароль', { exact: true }), 'password123')
    await actor.type(screen.getByLabelText('Повторите пароль', { exact: true }), 'password123')
    await actor.click(screen.getByRole('button', { name: 'Создать аккаунт' }))
    await screen.findByRole('heading', { name: 'Давайте заполним профиль' })
    await actor.clear(screen.getByLabelText('Отображаемое имя')); await actor.type(screen.getByLabelText('Отображаемое имя'), 'Мой музей')
    await actor.type(screen.getByLabelText(/О себе и своих увлечениях/), 'Собираю монеты')
    await actor.click(screen.getByRole('button', { name: 'Создать профиль' }))
    await screen.findByRole('heading', { name: 'Мой музей' })
    const call = fetchMock.mock.calls.find(c => c[0] === '/api/registration/create_profile')
    expect(JSON.parse(String(call?.[1]?.body))).toEqual({ userId: 7, publicName: 'Мой музей', description: 'Собираю монеты', avatarFileId: null, locationId: null })
    expect(call?.[1]?.headers).toMatchObject({ Authorization: `Bearer ${token}` })
    expect(screen.queryByRole('button', { name: 'Создать профиль' })).toBeNull()
  })
  it('does not register when passwords differ', async () => {
    const fetchMock = mockApi(); vi.stubGlobal('fetch', fetchMock); const actor = userEvent.setup(); renderApp('/register')
    await actor.type(screen.getByLabelText('Электронная почта'), user.email); await actor.type(screen.getByLabelText('Логин'), 'collector')
    await actor.type(screen.getByLabelText('Пароль', { exact: true }), 'password123'); await actor.type(screen.getByLabelText('Повторите пароль', { exact: true }), 'different123')
    await actor.click(screen.getByRole('button', { name: 'Создать аккаунт' }))
    expect(screen.getByRole('alert').textContent).toContain('Пароли не совпадают')
    expect(fetchMock).not.toHaveBeenCalled()
  })
  it('recovers from successful registration followed by a failed automatic login without registering twice', async () => {
    const fetchMock = vi.fn(async (url: string) => url === '/api/registration' ? json(user) : json({}, 503))
    vi.stubGlobal('fetch', fetchMock); const actor = userEvent.setup(); renderApp('/register')
    await actor.type(screen.getByLabelText('Электронная почта'), user.email); await actor.type(screen.getByLabelText('Логин'), 'collector')
    await actor.type(screen.getByLabelText('Пароль', { exact: true }), 'password123'); await actor.type(screen.getByLabelText('Повторите пароль', { exact: true }), 'password123')
    await actor.click(screen.getByRole('button', { name: 'Создать аккаунт' }))
    await screen.findByRole('link', { name: 'Перейти ко входу' })
    expect(screen.getByRole('alert').textContent).toContain('Аккаунт создан')
    expect(fetchMock.mock.calls.filter(c => c[0] === '/api/registration')).toHaveLength(1)
  })
  it('restores a saved session and discards rejected tokens', async () => {
    sessionStorage.setItem('mycollection.session', token); vi.stubGlobal('fetch', vi.fn(async () => json({}, 401)))
    renderApp('/profile')
    await screen.findByRole('heading', { name: 'С возвращением' })
    await waitFor(() => expect(sessionStorage.getItem('mycollection.session')).toBeNull())
    expect(screen.getByRole('alert').textContent).toContain('Сессия завершилась')
  })
  it('shows a useful message when the server is offline', async () => {
    vi.stubGlobal('fetch', vi.fn().mockRejectedValue(new TypeError('Failed to fetch'))); const actor = userEvent.setup(); renderApp('/login')
    await actor.type(screen.getByLabelText('Логин'), 'collector'); await actor.type(screen.getByLabelText('Пароль', { exact: true }), 'password123')
    await actor.click(screen.getByRole('button', { name: 'Войти' }))
    expect((await screen.findByRole('alert')).textContent).toContain('Не удалось связаться с сервером')
    expect(sessionStorage.getItem('mycollection.session')).toBeNull()
  })
})
describe('forum demo', () => {
  it('supports category search and reversible votes', async () => {
    const actor = userEvent.setup(); renderApp('/?category=Нумизматика')
    expect(screen.getAllByRole('article')).toHaveLength(1)
    const article = within(screen.getByRole('article'))
    await actor.click(article.getByRole('button', { name: 'Поддержать' }))
    expect(article.getByText('129')).toBeTruthy()
    await actor.click(article.getByRole('button', { name: 'Не поддерживать' }))
    expect(article.getByText('127')).toBeTruthy()
    await actor.click(article.getByRole('button', { name: 'Не поддерживать' }))
    expect(article.getByText('128')).toBeTruthy()
    await actor.type(screen.getByRole('searchbox', { name: 'Поиск по коллекциям и обсуждениям' }), 'джаз{Enter}')
    expect(screen.getAllByRole('article')).toHaveLength(1)
    expect(screen.getByRole('heading', { name: 'Джаз на виниле: моя полка любимых изданий' })).toBeTruthy()
  })
})
