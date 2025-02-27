<template>
  <el-skeleton v-if="loading" :rows="10" :animated="true">
    <template #default>
      <div class="custom-skeleton">
        <div class="skeleton-image"></div>
        <div class="skeleton-line"></div>
        <div class="skeleton-line"></div>
        <div class="skeleton-line short"></div>
      </div>
    </template>
  </el-skeleton>
</template>

<script setup>
import { ref } from 'vue';

const loading = ref(true);

setTimeout(() => {
loading.value = false;
}, 500);
</script>

<style scoped>
.custom-skeleton .skeleton-image {
width: 120px;
height: 180px;
background: #e0e0e0;
border-radius: 8px;
animation: shimmer 1.5s infinite linear;
margin: 20px auto; 
}

.custom-skeleton .skeleton-line {
height: 20px;
background: #e0e0e0;
border-radius: 5px;
animation: shimmer 1.5s infinite linear;
margin: 10px 20px;
}

.custom-skeleton .skeleton-line.short {
width: 70%;
margin: 10px auto; 
}

@keyframes shimmer {
0% {
  background-position: -100% 0;
}
100% {
  background-position: 200% 0;
}
}
</style>
