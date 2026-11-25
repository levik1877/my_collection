import { useEffect, useState } from 'react'
import { Link, NavLink, Navigate, Route, Routes, useLocation, useNavigate, useSearchParams } from 'react-router-dom'
import { ArrowDown, ArrowUp, Bookmark, BookOpen, Camera, ChevronDown, CircleHelp, Coins, Compass, Disc3, Flame, Gavel, Home, Layers3, Menu, MessageCircle, Palette, Plus, Search, Stamp, Tag, UserRound } from 'lucide-react'
import { useAuth } from './auth'
import { categories, posts, collections, type Post } from './data'
import { CollectibleArt } from './CollectibleArt'
import { AuthPage, ProfilePage } from './AccountPages'

const categoryIcons = [Coins, Stamp, Disc3, BookOpen, Camera, Palette]
export function Brand() { return <Link className="brand" to="/" aria-label="MyCollection — на главную"><span className="brand-mark"><Layers3 size={23}/></span><span>My<span className="brand-light">Collection</span></span></Link> }
function Header({ onMenu }: { onMenu: () => void }) {
  const { token } = useAuth()
  const [params] = useSearchParams()
  const [query, setQuery] = useState(params.get('q') || '')
  const navigate = useNavigate()
  const showMenu = !['/login', '/register'].includes(useLocation().pathname)
  useEffect(() => setQuery(params.get('q') || ''), [params])
  return <header className="header"><div className="header-inner">{showMenu && <button className="icon-button mobile-menu" aria-label="Открыть меню" onClick={onMenu}><Menu/></button>}<Brand/><form className="global-search" role="search" onSubmit={e => { e.preventDefault(); navigate(query.trim() ? `/?q=${encodeURIComponent(query.trim())}` : '/') }}><Search size={19}/><input type="search" aria-label="Поиск по коллекциям и обсуждениям" placeholder="Поиск в MyCollection" value={query} onChange={e => setQuery(e.target.value)}/></form><div className="header-actions">{token ? <Link className="button primary profile-link" to="/profile"><UserRound size={18}/>Профиль</Link> : <><Link className="button subtle" to="/login">Войти</Link><Link className="button primary" to="/register">Регистрация</Link></>}</div></div></header>
}
function Sidebar({ open, close }: { open: boolean; close: () => void }) {
  const [params] = useSearchParams()
  const location = useLocation()
  const category = params.get('category')
  useEffect(close, [location.pathname, location.search])
  return <><button className={`sidebar-scrim ${open ? 'visible' : ''}`} aria-label="Закрыть меню" onClick={close}/><aside className={`sidebar ${open ? 'open' : ''}`}><nav aria-label="Основная навигация"><div className="nav-group"><NavLink to="/" end className={() => `nav-item ${location.pathname === '/' && !category && !params.get('sort') ? 'active' : ''}`}><Home size={19}/>Главная</NavLink><Link className={`nav-item ${params.get('sort') === 'top' ? 'active' : ''}`} to="/?sort=top"><Flame size={19}/>Популярное</Link><NavLink className="nav-item" to="/collections"><Layers3 size={19}/>Коллекции</NavLink><NavLink className="nav-item" to="/questions"><MessageCircle size={19}/>Обсуждения</NavLink></div><div className="nav-group"><p className="nav-heading">Категории</p>{categories.map((name, i) => { const Icon = categoryIcons[i]; return <Link key={name} to={`/?category=${encodeURIComponent(name)}`} className={`nav-item ${category === name ? 'active' : ''}`}><Icon size={18}/>{name}</Link> })}</div><div className="nav-group"><p className="nav-heading">Маркет</p><NavLink className="nav-item" to="/market"><Tag size={19}/>Объявления<span className="soon-dot">скоро</span></NavLink><NavLink className="nav-item" to="/auctions"><Gavel size={19}/>Аукционы<span className="soon-dot">скоро</span></NavLink></div><div className="sidebar-bottom"><Link className="nav-item help-link" to="/about"><CircleHelp size={18}/>О сообществе</Link></div></nav></aside></>
}
function RightRail({ category }: { category?: string }) {
  const { token } = useAuth()
  const [topCategory, setTopCategory] = useState(category || categories[0])
  useEffect(() => { if (category) setTopCategory(category) }, [category])
  const ranked = collections.filter(p => p.category === topCategory).sort((a, b) => b.votes - a.votes)
  return <aside className="right-rail">
    <section className="panel top-panel">
      <h3>Топ коллекций</h3>
      <div className="select-wrap">
        <select aria-label="Категория топа коллекций" value={topCategory} onChange={e => setTopCategory(e.target.value)}>{categories.map(c => <option key={c}>{c}</option>)}</select>
        <ChevronDown size={16}/>
      </div>
      {ranked.map((post, index) => <Link to={`/post/${post.id}`} key={post.id} className="top-entry">
        <span className="rank">{index + 1}</span>
        <div className="top-thumb">{post.art ? <CollectibleArt kind={post.art} compact/> : <BookOpen size={22}/>}</div>
        <div><strong>{post.title}</strong><small>{post.author}</small></div>
      </Link>)}
      <Link className="text-link" to={`/collections?category=${encodeURIComponent(topCategory)}`}>Смотреть коллекции</Link>
      <p className="demo-footnote">Демонстрационная подборка</p>
    </section>
    {!token && <section className="panel welcome-panel">
      <h3>Ваши люди. Ваши коллекции.</h3>
      <p>Делитесь находками и обсуждайте то, что вам интересно.</p>
      <Link className="text-link" to="/register">Присоединиться</Link>
    </section>}
    <div className="rail-footer"><Link to="/about">О проекте</Link><Link to="/about#rules">Правила</Link><span>© {new Date().getFullYear()} MyCollection</span></div>
  </aside>
}
export function Vote({ count }: { count: number }) {
  const [vote, setVote] = useState(0)
  return <div className="vote" aria-label="Демонстрационное голосование"><button title="Поддержать — демонстрация" aria-label="Поддержать" aria-pressed={vote === 1} className={vote === 1 ? 'voted-up' : ''} onClick={() => setVote(vote === 1 ? 0 : 1)}><ArrowUp size={21}/></button><strong aria-live="polite">{count + vote}</strong><button title="Не поддерживать — демонстрация" aria-label="Не поддерживать" aria-pressed={vote === -1} className={vote === -1 ? 'voted-down' : ''} onClick={() => setVote(vote === -1 ? 0 : -1)}><ArrowDown size={21}/></button></div>
}
function PostCard({ post }: { post: Post }) {
  const [saved, setSaved] = useState(false)
  return <article className="post-card">
    <Vote count={post.votes}/>
    <div className="post-content">
      <div className="post-meta">
        <span className="avatar">{post.initials}</span>
        <span className="post-author">{post.author}</span>
        <span className="meta-dot">·</span>
        <Link className="category-label" to={`/?category=${encodeURIComponent(post.category)}`}>{post.category}</Link>
        <span className="post-time">{post.time}</span>
      </div>
      <Link className="post-title" to={`/post/${post.id}`}><h2>{post.title}</h2></Link>
      <p className="post-excerpt">{post.text}</p>
      {post.art && <Link className="post-art-link" to={`/post/${post.id}`}><CollectibleArt kind={post.art}/></Link>}
      <div className="post-actions">
        <Link to={`/post/${post.id}#comments`} aria-label={`Открыть комментарии: ${post.comments}`}><MessageCircle size={18}/><span>{post.comments}</span></Link>
        <button aria-pressed={saved} onClick={() => setSaved(!saved)} title="Сохранить на время просмотра"><Bookmark size={18} fill={saved ? 'currentColor' : 'none'}/><span>{saved ? 'Сохранено' : 'Сохранить'}</span></button>
        <span className="post-kind">{post.type}</span>
      </div>
    </div>
  </article>
}
function Feed({ mode = 'feed' }: { mode?: 'feed' | 'collections' | 'questions' }) {
  const [params, setParams] = useSearchParams()
  const [type, setType] = useState('Все публикации')
  const category = params.get('category') || ''
  const sort = params.get('sort') || 'popular'
  const query = params.get('q')?.toLocaleLowerCase('ru') || ''
  const title = mode === 'collections' ? 'Коллекции сообщества' : mode === 'questions' ? 'Обсуждения' : category || (query ? 'Результаты поиска' : 'Лента сообщества')
  const source = mode === 'collections' ? collections : posts
  const visible = source.filter(p => (!category || p.category === category) && (!query || `${p.title} ${p.text} ${p.category} ${p.author}`.toLocaleLowerCase('ru').includes(query)) && (mode !== 'collections' || p.type === 'Коллекция' || p.type === 'Экспонат') && (mode !== 'questions' || p.type === 'Вопрос' || p.type === 'Пост') && (type === 'Все публикации' || p.type === type)).sort((a, b) => sort === 'new' ? source.indexOf(a) - source.indexOf(b) : b.votes - a.votes)
  function setSort(value: string) { const next = new URLSearchParams(params); next.set('sort', value); setParams(next) }
  return <div className="content-grid"><main id="main-content" className="feed"><div className="page-heading"><div><h1>{title}</h1><p>{query ? `По запросу «${params.get('q')}»` : 'Демонстрационная лента коллекций и обсуждений.'}</p></div></div><div className="composer"><Link to="/publish">Поделитесь находкой или задайте вопрос</Link><Link to="/publish" className="button primary"><Plus size={17}/><span>Публикация</span></Link></div><div className="feed-controls"><div className="sort-tabs" aria-label="Сортировка ленты"><button className={sort === 'popular' ? 'selected' : ''} onClick={() => setSort('popular')}>Интересное</button><button className={sort === 'new' ? 'selected' : ''} onClick={() => setSort('new')}>Новое</button><button className={sort === 'top' ? 'selected' : ''} onClick={() => setSort('top')}>Топ</button></div><div className="select-wrap type-select"><select aria-label="Тип публикации" value={type} onChange={e => setType(e.target.value)}>{['Все публикации', ...(mode === 'collections' ? ['Коллекция', 'Экспонат'] : mode === 'questions' ? ['Пост', 'Вопрос'] : ['Экспонат', 'Коллекция', 'Пост', 'Вопрос'])].map(t => <option key={t}>{t}</option>)}</select><ChevronDown size={14}/></div></div>{visible.length ? <div className="posts">{visible.map(p => <PostCard key={p.id} post={p}/>)}</div> : <div className="panel empty-state"><Search size={32}/><h2>Пока ничего не нашлось</h2><p>Попробуйте другой запрос или сбросьте фильтры.</p><button className="button secondary" onClick={() => { setParams({}); setType('Все публикации') }}>Сбросить фильтры</button></div>}<p className="feed-end">Это все публикации</p></main><RightRail category={category}/></div>
}
function PostDetail() {
  const location = useLocation()
  const post = [...posts, ...collections].find(p => `/post/${p.id}` === location.pathname)
  if (!post) return <NotFound/>
  return <div className="content-grid"><main id="main-content"><Link className="back-link" to="/">Лента сообщества</Link><p className="demo-banner">Пример публикации · голоса и закладки не сохраняются</p><PostCard post={post}/><section className="panel discussion" id="comments"><h2>Обсуждение</h2><p>Здесь появятся комментарии и ответы участников.</p><div className="empty-discussion"><MessageCircle size={29}/><span>Обсуждения станут доступны в следующей версии.</span></div></section></main><RightRail category={post.category}/></div>
}
function ComingSoon({ kind }: { kind: 'market' | 'auctions' | 'publish' }) {
  const { token } = useAuth()
  const text = { market: ['Объявления', 'Находки для вашей коллекции', 'Здесь можно будет покупать и продавать экспонаты по фиксированной цене.'], auctions: ['Аукционы', 'Редкости, за которые стоит побороться', 'Здесь появятся лоты, история ставок и торги в реальном времени.'], publish: ['Новая публикация', 'У вашей коллекции есть история', 'Создание коллекций, экспонатов, постов и вопросов появится в следующей версии. Уже сейчас можно создать аккаунт и заполнить профиль.'] }[kind]
  return <main id="main-content" className="feature-page"><span className="eyebrow">{text[0]} · СКОРО</span><h1>{text[1]}</h1><p>{text[2]}</p>{kind === 'publish' ? <section className="panel publish-options">{[{ icon: Layers3, name: 'Коллекция', text: 'Соберите экспонаты в одну историю' }, { icon: MessageCircle, name: 'Пост', text: 'Поделитесь опытом и находками' }, { icon: CircleHelp, name: 'Вопрос', text: 'Спросите у тех, кто разбирается' }].map(item => <div key={item.name}><item.icon size={27}/><h3>{item.name}</h3><p>{item.text}</p><span className="badge">Скоро</span></div>)}</section> : <div className="market-grid">{(['coin', 'camera', 'stamps'] as const).map((art, i) => <article className="panel market-card" key={art}><CollectibleArt kind={art}/><div><span className="eyebrow">ПРИМЕР {kind === 'auctions' ? 'ЛОТА' : 'ОБЪЯВЛЕНИЯ'}</span><h3>{['Серебряный рубль 1924', 'Плёночный фотоаппарат', 'Космическая филателия'][i]}</h3><p>{kind === 'auctions' ? 'Стартовая цена' : 'Цена'}<strong>{['4 500', '3 200', '1 800'][i]} ₽</strong></p><span className="badge">{kind === 'auctions' ? 'Торги ещё не открыты' : 'Демонстрационный экспонат'}</span></div></article>)}</div>}<Link to={token ? '/profile' : '/register'} className="button primary">{token ? 'Перейти в профиль' : 'Создать аккаунт'}</Link></main>
}
function About() { return <main id="main-content" className="about-page panel"><span className="eyebrow">О ПРОЕКТЕ</span><h1>Общее место для личных коллекций</h1><p>MyCollection — портал для людей, которые видят в вещах больше, чем вещи. Здесь можно будет вести коллекции, находить редкие экспонаты, обмениваться опытом и знакомиться с единомышленниками.</p><h2>Что уже доступно</h2><p>Регистрация, вход и создание профиля. Лента, подборки и торговая площадка пока показывают примеры будущих материалов. Стрелки голосования и закладки позволяют попробовать интерфейс; их состояние не сохраняется на сервере.</p><h2 id="rules">Правила сообщества</h2><ul><li>Обсуждайте предметы и идеи с уважением к собеседникам.</li><li>Указывайте происхождение и известные особенности экспоната.</li><li>Публикуйте только свои фотографии или материалы с разрешением автора.</li><li>При сделках честно описывайте состояние и условия передачи предмета.</li></ul><Link to="/" className="button secondary">Вернуться к ленте</Link></main> }
function NotFound() { return <main id="main-content" className="panel empty-state"><Compass size={40}/><h1>Эта страница пока не найдена</h1><p>Но в ленте наверняка найдётся что-то интересное.</p><Link to="/" className="button primary">На главную</Link></main> }
export default function App() {
  const [menu, setMenu] = useState(false)
  const location = useLocation()
  useEffect(() => { if (!location.hash) window.scrollTo(0, 0) }, [location.pathname])
  const isAuth = ['/login', '/register'].includes(location.pathname)
  return <><a className="skip-link" href="#main-content">Перейти к содержимому</a><Header onMenu={() => setMenu(!menu)}/>{!isAuth && <Sidebar open={menu} close={() => setMenu(false)}/>}<div className={isAuth ? 'auth-shell' : 'app-shell'}><Routes><Route path="/" element={<Feed key="feed"/>}/><Route path="/collections" element={<Feed key="collections" mode="collections"/>}/><Route path="/questions" element={<Feed key="questions" mode="questions"/>}/><Route path="/login" element={<AuthPage key="login"/>}/><Route path="/register" element={<AuthPage key="register" register/>}/><Route path="/profile" element={<ProfilePage/>}/><Route path="/post/:id" element={<PostDetail/>}/><Route path="/market" element={<ComingSoon kind="market"/>}/><Route path="/auctions" element={<ComingSoon kind="auctions"/>}/><Route path="/publish" element={<ComingSoon kind="publish"/>}/><Route path="/about" element={<About/>}/><Route path="/home" element={<Navigate to="/" replace/>}/><Route path="*" element={<NotFound/>}/></Routes></div></>
}
