import { createRouter, createWebHistory } from 'vue-router'
import { useAuthStore } from '../stores/auth.store';
import HomeView from '../views/HomeView.vue'
import MovieView from '../views/MovieView.vue'
import AdminPanel from '../views/AdminPanel.vue';
import LoginView from '@/views/LoginView.vue'
import RegisterView from '@/views/RegisterView.vue';
import MovieDetailsView from '@/views/MovieDetailsView.vue';

const routes = [
  {
    path: '/',
    name: 'home',
    component: HomeView
  },
  {
    path: '/about',
    name: 'about',
    component: () => import('../views/AboutView.vue')
  },
  {
    path: '/movies',
    name: 'movies',
    component: MovieView 
  },
  {
    path: '/login',
    name: 'login',
    component: LoginView,
  },
  {
    path: '/register',
    name: 'register',
    component: RegisterView,
  },
  {
    path: '/admin',
    name: 'admin',
    component: AdminPanel,
    meta: { requiresAuth: true },
  },
  {
    path: '/movie/:id',
    name: 'movie details',
    component: MovieDetailsView,
  },
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

router.beforeEach((to, from, next) => {
  const authStore = useAuthStore();

  if (to.meta.requiresAuth && !authStore.user) {
    next('/login');
  } else {
    next();
  }
});

export default router
