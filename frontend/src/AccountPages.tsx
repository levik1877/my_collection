import { useState, type FormEvent } from 'react'
import { Link, Navigate, useLocation, useNavigate } from 'react-router-dom'
import { Check, Eye, EyeOff, Layers3, LogOut, Star, UserRound } from 'lucide-react'
import { api } from './api'
import { useAuth } from './auth'

function PasswordField({ label, name, confirm = false, register = false }: { label: string; name: string; confirm?: boolean; register?: boolean }) {
  const [show, setShow] = useState(false)
  return <label className="field"><span>{label}</span><div className="password-input"><input required name={name} type={show ? 'text' : 'password'} minLength={register ? 8 : undefined} maxLength={register ? 72 : undefined} autoComplete={register || confirm ? 'new-password' : 'current-password'} placeholder={register ? 'Не менее 8 символов' : 'Введите пароль'}/><button type="button" className="icon-button" onClick={() => setShow(!show)} aria-label={show ? `Скрыть ${label.toLowerCase()}` : `Показать ${label.toLowerCase()}`}>{show ? <EyeOff size={18}/> : <Eye size={18}/>}</button></div></label>
}
export function AuthPage({ register = false }: { register?: boolean }) {
  const auth = useAuth()
  const navigate = useNavigate()
  const location = useLocation()
  const [busy, setBusy] = useState(false)
  const [error, setError] = useState('')
  const [registered, setRegistered] = useState(false)
  if (auth.token) return <Navigate to="/profile" replace/>
  async function submit(event: FormEvent<HTMLFormElement>) {
    event.preventDefault(); setError('')
    const data = new FormData(event.currentTarget)
    const username = String(data.get('username')).trim()
    const password = String(data.get('password'))
    if (register && password !== data.get('confirmPassword')) { setError('Пароли не совпадают. Проверьте повторный ввод.'); return }
    if (!username || (register && username.length < 3)) { setError('Логин должен содержать от 3 до 50 символов.'); return }
    if (register && new TextEncoder().encode(password).length > 72) { setError('Пароль слишком длинный: максимум 72 байта (кириллица занимает больше одного байта).'); return }
    setBusy(true)
    let created = false
    try {
      if (register) { await api.register({ username, email: String(data.get('email')).trim(), password, confirmPassword: String(data.get('confirmPassword')) }); created = true; setRegistered(true) }
      await auth.login(username, password)
      navigate('/profile', { replace: true })
    } catch (err) {
      setError(created ? 'Аккаунт создан, но автоматический вход не удался. Войдите с указанным логином и паролем.' : err instanceof Error ? err.message : 'Не удалось выполнить запрос.')
    } finally { setBusy(false) }
  }
  return <main id="main-content" className="auth-layout"><section className="auth-form-wrap"><Link className="back-link" to="/">Вернуться к сообществу</Link><div className="auth-form-heading"><h2>{register ? 'Регистрация' : 'С возвращением'}</h2><p>{register ? 'Создайте аккаунт и расскажите о своём увлечении.' : 'Войдите, чтобы продолжить свою коллекцию.'}</p></div><form onSubmit={submit}><fieldset disabled={busy || registered}>
  <label className="field">
    <span>Логин</span>
    <input name="username" type="text" autoComplete="username" autoCapitalize="none" spellCheck={false} minLength={register ? 3 : undefined} maxLength={50} aria-describedby={register ? 'registration-username-hint' : undefined} placeholder={register ? 'Придумайте логин, например collector_ivan' : 'Ваш логин'} required/>
  </label>
  {register && <p id="registration-username-hint" className="field-hint">От 3 до 50 символов. Этот логин понадобится для входа.</p>}
  {register && <label className="field"><span>Электронная почта</span><input name="email" type="email" autoComplete="email" maxLength={255} placeholder="you@example.com" required/></label>}<PasswordField label="Пароль" name="password" register={register}/>{register && <PasswordField label="Повторите пароль" name="confirmPassword" register confirm/>}</fieldset>{(error || auth.error || location.state?.message) && <div className="form-error" role="alert">{error || auth.error || location.state?.message}</div>}{registered && error ? <Link className="button primary full" to="/login">Перейти ко входу</Link> : <button className="button primary full submit-button" type="submit" disabled={busy}>{busy ? 'Подождите…' : register ? 'Создать аккаунт' : 'Войти'}</button>}{register && <p className="form-note">Присоединяясь, вы соглашаетесь соблюдать <Link to="/about#rules">правила сообщества</Link>.</p>}</form><p className="auth-switch">{register ? 'Уже есть аккаунт?' : 'Впервые здесь?'} <Link to={register ? '/login' : '/register'}>{register ? 'Войти' : 'Зарегистрироваться'}</Link></p></section></main>
}
export function ProfilePage() {
  const { token, account, loading, error, logout, reload, createProfile } = useAuth()
  const [tab, setTab] = useState('Коллекции')
  const [busy, setBusy] = useState(false)
  const [formError, setFormError] = useState('')
  const navigate = useNavigate()
  if (!token) return <Navigate to="/login" replace/>
  if (loading) return <main id="main-content" className="panel empty-state" role="status"><span className="loading-ring"/><h2>Загружаем профиль…</h2></main>
  if (!account) return <main id="main-content" className="panel empty-state"><UserRound size={34}/><h1>Не удалось загрузить профиль</h1><p role="alert">{error || 'Проверьте подключение к серверу.'}</p><button className="button primary" onClick={reload}>Попробовать снова</button><button className="button subtle" onClick={logout}>Выйти</button></main>
  const { user, profile } = account
  const name = profile?.publicName || user.username
  async function submit(event: FormEvent<HTMLFormElement>) {
    event.preventDefault(); const data = new FormData(event.currentTarget)
    const name = String(data.get('publicName')).trim()
    if (!name) { setFormError('Введите отображаемое имя.'); return }
    setBusy(true); setFormError('')
    try { await createProfile(name, String(data.get('description'))) } catch (err) { setFormError(err instanceof Error ? err.message : 'Не удалось создать профиль.') } finally { setBusy(false) }
  }
  const empty = { 'Коллекции': ['Первая коллекция ещё впереди', 'Здесь будут ваши коллекции и экспонаты. Их создание появится в следующей версии.'], 'Публикации': ['Ваши истории будут здесь', 'Посты и вопросы, которыми вы поделитесь с сообществом, появятся в этом разделе.'], 'Отзывы': ['Отзывы появятся после сделок', 'Здесь будут оценки и отзывы участников завершённых сделок.'] }[tab]!
  return <main id="main-content" className="profile-page"><div className="profile-heading"><div><h1>Мой профиль</h1></div><button className="button secondary" onClick={() => { logout(); navigate('/') }}><LogOut size={17}/>Выйти</button></div><section className="panel profile-card"><div className="profile-summary"><div className="profile-avatar">{name.slice(0, 2).toLocaleUpperCase('ru')}</div><div className="profile-name"><h2>{name}</h2><span>@{user.username}</span></div><span className="profile-status"><Check size={15}/>{profile ? 'Участник сообщества' : 'Новый участник'}</span></div><div className="profile-bio"><p>{profile?.description || 'Расскажите о себе и о вещах, которые вам интересны.'}</p><span className="muted small">{user.createdAt ? `В сообществе с ${new Date(user.createdAt + 'T00:00:00').toLocaleDateString('ru', { month: 'long', year: 'numeric' })}` : 'Дата регистрации пока не указана'}</span></div><div className="profile-stats"><div><strong>{profile?.reputation ?? 0}</strong><span>Репутация</span></div><div><strong>—</strong><span>Коллекции</span></div><div><strong>—</strong><span>Публикации</span></div><div><strong>—</strong><span>Отзывы о сделках</span></div></div></section><div className="profile-columns"><div>{!profile && <section className="panel setup-profile"><h2>Давайте заполним профиль</h2><p>Выберите имя, которое увидят другие коллекционеры.</p><form onSubmit={submit}><fieldset disabled={busy}><label className="field"><span>Отображаемое имя</span><input name="publicName" defaultValue={user.username} maxLength={255} required autoComplete="nickname"/></label><label className="field"><span>О себе и своих увлечениях <small>необязательно</small></span><textarea name="description" rows={4} maxLength={4000} placeholder="Что вы коллекционируете? С чего всё началось?"/></label></fieldset>{formError && <p className="form-error" role="alert">{formError}</p>}<button disabled={busy} className="button primary">{busy ? 'Создаём профиль…' : 'Создать профиль'}</button></form></section>}<section className="panel profile-materials"><div className="profile-tabs" role="tablist" aria-label="Материалы профиля">{['Коллекции', 'Публикации', 'Отзывы'].map(label => <button role="tab" aria-selected={tab === label} aria-controls="profile-tab-content" id={`tab-${label}`} key={label} className={tab === label ? 'selected' : ''} onClick={() => setTab(label)}>{label}</button>)}</div><div id="profile-tab-content" role="tabpanel" aria-labelledby={`tab-${tab}`} className="empty-state"><Layers3 size={36}/><h2>{empty[0]}</h2><p>{empty[1]}</p><Link className="button secondary" to="/">Вдохновиться в ленте</Link></div></section></div><aside className="profile-aside"><section className="panel"><h3><UserRound size={18}/>Учётная запись</h3><dl><dt>Логин</dt><dd>{user.username}</dd><dt>Электронная почта</dt><dd>{user.email}</dd></dl><p className="small muted">Почта видна только вам.</p></section><section className="panel reputation-note"><h3><Star size={18}/>О репутации</h3><p>Репутация отражает вклад в сообщество: полезные публикации, оценки участников и отзывы после сделок.</p><p className="small muted">Публикации, сделки и расчёт репутации будут доступны позже.</p></section></aside></div></main>
}
