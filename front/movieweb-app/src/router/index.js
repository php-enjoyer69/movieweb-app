import { createRouter, createWebHistory } from 'vue-router'
import { useAuthStore } from '../stores/auth.store';
import HomeView from '../views/HomeView.vue'
import MovieView from '../views/MovieView.vue'
import AdminPanel from '../views/AdminPanel.vue';
import LoginView from '@/views/LoginView.vue'
import RegisterView from '@/views/RegisterView.vue';
import MovieDetailsView from '@/views/MovieDetailsView.vue';
import UserManagement from '@/views/UserManagement.vue';
import ProfileView from '@/views/ProfileView.vue';
import MovieManagement from '@/views/MovieManagement.vue';

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
    meta: { requiresAuth: true, requiresAdmin: true },
  },
  {
    path: '/movie/:id',
    name: 'movie-details',
    component: MovieDetailsView,
    props: true
  },
  {
    path: '/admin/manage-users',
    name: 'manage-users',
    component: UserManagement,
    meta: { requiresAuth: true, requiresAdmin: true },
  },
  {
    path: '/profile',
    name: 'profile',
    component: ProfileView,
    meta: { requiresAuth: true }, 
  },
  {
    path: '/admin/manage-movies',
    name: 'manage-movies',
    component: MovieManagement,
    meta: { requiresAuth: true, requiresAdmin: true }, 
  },
];

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
});

router.beforeEach((to, from, next) => {
  const authStore = useAuthStore();
  const user = authStore.user;

  if (to.meta.requiresAuth) {
    if (!user) {
      next('/login');
    } else if (to.meta.requiresAdmin && user.email !== 'admin@mail.com') {
      console.log('Access denied: not an admin.');
      next('/');
    } else {
      next();
    }
  } else {
    next();
  }
});

export default router;
