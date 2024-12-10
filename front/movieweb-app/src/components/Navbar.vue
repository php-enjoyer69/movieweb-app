<template>
  <el-menu
    :default-active="activeIndex"
    class="navbar"
    mode="horizontal"
    background-color="#4b0082"
    text-color="#ffffff"
    active-text-color="#e0b0ff"
  >
    <div class="logo">
      <router-link to="/" class="logo-text">
        Movie<span class="web-text">Web<el-icon><Monitor /></el-icon></span>
      </router-link>
    </div>
    <el-menu-item index="1" @click="goTo('/')">Home</el-menu-item>
    <el-menu-item index="2" @click="goTo('/movies')">Movies</el-menu-item>
    <el-menu-item index="4" @click="goTo('/about')">About</el-menu-item>

    <el-menu-item index="5" class="auth-button" v-if="user == null">
      <router-link to="/login" class="menu-link">Sign in</router-link>
    </el-menu-item>
    <!--<el-menu-item index="6" class="auth-button" v-if="user == null">
      <router-link to="/register" class="menu-link">Sign up</router-link>
    </el-menu-item>-->
    <el-menu-item index="7" class="auth-button" v-else @click="handleLogout">
      <el-icon><SwitchButton /></el-icon>Sign out
    </el-menu-item>
  </el-menu>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth.store';
import { storeToRefs } from 'pinia';
import { ElMessage } from 'element-plus'; 

const authStore = useAuthStore();
const { user } = storeToRefs(authStore);
const router = useRouter();
const activeIndex = ref('1')

const goTo = (path) => {
  router.push(path)
}

const handleLogout = () => {
  authStore.logout();
  ElMessage({
    message: 'You have successfully logged out!',
    type: 'info',
    duration: 1500,  
  });
  router.push('/');
}
</script>

<style scoped>
.navbar {
  display: flex;
  align-items: center;
  justify-content: flex-start;  
  padding: 0 8rem;  
}

.logo-text {
  font-size: 1.5rem;
  font-weight: bold;
  color: white;
  text-decoration: none;
  margin-right: 20px;
}

.web-text {
  color: #9b4dca; 
}

.el-menu {
  display: flex;
  align-items: center;
  justify-content: flex-start; 
}

.menu-link {
  text-decoration: none;
  color: inherit;
}
</style>
