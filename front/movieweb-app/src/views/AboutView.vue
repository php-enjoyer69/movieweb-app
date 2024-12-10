<template>
  <el-card class="about-page" shadow="hover">
    <h1>About MovieWeb</h1>
    <p>Welcome to MovieWeb! This is the best place to discover movies and TV shows.</p>
    <el-button type="primary" @click="goHome">Go Home</el-button>
  </el-card>
</template>

<script setup>
import { useRouter } from 'vue-router'

const router = useRouter()
const goHome = () => {
  router.push('/')
}
</script>

<style scoped>
.about-page {
  max-width: 600px;
  margin: 2rem auto;
  padding: 2rem;
  background-color: #f4f4f4;
}

h1 {
  color: #4b0082;
}

p {
  margin-top: 1rem;
  font-size: 1.1rem;
  line-height: 1.6;
}
</style>
