<template>
  <el-menu
    :default-active="activeIndex"
    class="navbar"
    mode="horizontal"
    background-color="#4b0082"
    text-color="#ffffff"
    active-text-color="#e0b0ff"
    :ellipsis="false"
  >
    <el-menu-item index="/" class="logo-container">
      <router-link to="/" class="logo-text">
        Movie<span class="web-text">Web
          <Icon icon="mdi:movie-star-outline" style="margin: 0 0 15px -7px" />
        </span>
      </router-link>
    </el-menu-item>

    <el-menu-item index="/" @click="goTo('/')">Home</el-menu-item>

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


    <el-menu-item index="/about" @click="goTo('/about')">About</el-menu-item>

    <el-menu-item index="space" class="spacer" disabled></el-menu-item>

    <el-sub-menu v-if="user" index="auth">
      <template #title>
        <Icon icon="material-symbols:person" style="font-size: 20px;margin-right: 8px" /> {{ user.name }}
      </template>
      <el-menu-item v-if="user?.email === 'admin@mail.com'" @click="goTo('/admin')">
        <Icon icon="material-symbols:admin-panel-settings" style="font-size: 20px;margin-right: 8px" /> Admin Panel
      </el-menu-item>
      <el-menu-item @click="goTo('/profile')">
        <Icon icon="mdi:text-user" style="font-size: 20px;margin-right: 8px" /> My Profile
      </el-menu-item>
      <el-menu-item @click="handleLogout">
        <Icon icon="stash:signout-light" style="font-size: 20px;margin-right: 8px" /> Sign Out
      </el-menu-item>
    </el-sub-menu>

    <template v-else>
      <el-menu-item index="login" @click="goTo('/login')">
        <Icon icon="material-symbols-light:login" style="font-size: 20px;margin-right: 8px" /> Log In
      </el-menu-item>
      <el-menu-item index="register" @click="goTo('/register')">
        <Icon icon="mdi:account-plus" style="font-size: 20px;margin-right: 8px" /> Sign up
      </el-menu-item>
    </template>
  </el-menu>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue';
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
const menuPaths = ['/', '/movies', '/about'];

const activeIndex = computed(() => {
  return menuPaths.includes(route.path) ? route.path : '';
});

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
  color: white;
  text-decoration: none;
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
