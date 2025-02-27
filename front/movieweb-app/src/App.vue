<template>
  <div id="app">
    <Navbar />
    <el-scrollbar max-height="768px" class="content">
      <router-view />
      <Footer />
    </el-scrollbar>
  </div>
</template>

<script setup>
import Navbar from './components/Navbar.vue';
import Footer from './components/Footer.vue';
import { ElScrollbar } from 'element-plus';
</script>

<style scoped>
#app {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
}

.content {
  flex: 1;
  max-height: 100%;
}

.el-scrollbar__wrap {
  max-height: 90%;
  height: calc(100vh - 60px);
}
</style>
