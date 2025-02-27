<template>
  <el-menu
    class="navbar"
    mode="horizontal"
    background-color="#4b0082"
    :ellipsis="false"
    text-color="#ffffff" 
  >
    <el-menu-item index="/" class="logo-container">
      <router-link to="/" class="logo-text">
        Movie<span class="web-text">Web
          <Icon icon="mdi:movie-star-outline" style="margin: 0 0 15px -7px" />
        </span>
      </router-link>
    </el-menu-item>

    <el-menu-item index="home" @click="goTo('/')">Home</el-menu-item>

    <el-menu-item index="trending-now" @click="goTo('/trending-now')">Trending Movies</el-menu-item>

    <el-menu-item index="top-celebrities" @click="goTo('/top-celebrities')">Top Celebrities</el-menu-item>

    <el-sub-menu index="movies" @open="fetchGenres">
      <template #title>
        <span @click.stop="goTo('/movies')" style="cursor: pointer;">
          Movies
        </span>
      </template>
      <el-menu-item-group>
        <el-menu-item v-for="genre in genres" :key="genre.id" @click="filterByGenre(genre)">
          {{ genre.name }}
        </el-menu-item>
      </el-menu-item-group>
    </el-sub-menu>

    <el-menu-item index="/people" @click="goTo('/people')">Celebs</el-menu-item>

    <el-menu-item index="space" class="spacer" disabled></el-menu-item>

    <el-sub-menu v-if="user" index="auth">
      <template #title>
        <Icon icon="material-symbols:person" style="font-size: 20px;margin-right: 8px; color: #9b4dca;" /> {{ user.name }}
      </template>
      <el-menu-item v-if="user?.role === 'ADMIN'" @click="goTo('/admin')">
        <Icon icon="material-symbols:admin-panel-settings" style="font-size: 20px;margin-right: 8px; color: #9b4dca;" /> Admin Panel
      </el-menu-item>
      <el-menu-item @click="goTo('/profile')">
        <Icon icon="mdi:text-user" style="font-size: 20px;margin-right: 8px; color: #9b4dca;" /> My Profile
      </el-menu-item>
      <el-menu-item @click="handleLogout">
        <Icon icon="stash:signout-light" style="font-size: 20px;margin-right: 8px; color: #9b4dca;" /> Sign Out
      </el-menu-item>
    </el-sub-menu>

    <template v-else>
      <el-menu-item index="login" @click="goTo('/login')">
        <Icon icon="material-symbols:login-rounded" style="font-size: 20px;margin-right: 8px; color: #9b4dca;" /> Log In
      </el-menu-item>
      <el-menu-item index="register" @click="goTo('/register')">
        <Icon icon="mdi:account-plus" style="font-size: 20px;margin-right: 8px; color: #9b4dca;" /> Sign up
      </el-menu-item>
    </template>
  </el-menu>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import { useAuthStore } from '@/stores/auth.store';
import { storeToRefs } from 'pinia';
import { ElMessage, ElNotification } from 'element-plus';
import axios from 'axios';
import { Icon } from '@iconify/vue';

const authStore = useAuthStore();
const { user } = storeToRefs(authStore);
const router = useRouter();
const route = useRoute();

const genres = ref([]);

const activeIndex = ref('');

const goTo = (path) => {
  if (path === route.path) {
    window.location.reload();
  } else {
    window.location.href = path;
  }
};

const handleLogout = () => {
  authStore.logout();
  ElMessage({
    message: 'You have successfully logged out!',
    type: 'info',
    duration: 1500,
  });
  router.push('/');
};

const fetchGenres = async () => {
  try {
    const response = await axios.get('/api/movie-genres', { params: { size: 9999 } });
    genres.value = response.data.content || [];
  } catch (error) {
    ElNotification.error({
      title: 'Error',
      message: 'Failed to load genres.',
    });
  }
};

const filterByGenre = (genre) => {
  router.push({
    path: '/movies',
    query: { genre: genre.name },
  });
};

onMounted(() => {
  fetchGenres();
});
</script>

<style scoped>
.navbar {
  display: flex;
  align-items: center;
  padding: 0 8rem;
  overflow: visible;
}

.logo-container {
  margin-right: auto;
}

.logo-text {
  font-size: 1.5rem;
  font-weight: bold;
  color: white !important;
  text-decoration: none;
}

.logo-text.active {
  color: white !important;
}

.web-text {
  color: #9b4dca;
}

.spacer {
  flex-grow: 1;
  cursor: default;
  pointer-events: none;
}
</style>
