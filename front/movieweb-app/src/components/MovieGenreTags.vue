<template>
    <div class="flex gap-2" v-if="genres && genres.length">
      <el-tag
        v-for="genre in genres"
        :key="genre.id"
        :type="genreColors[genre.id] || 'default'"
        effect="dark"
        round
      >
        {{ genre.name }}
      </el-tag>
    </div>
  </template>
  
  <script setup>
  import { computed } from 'vue';
  
  const props = defineProps({
    genres: {
      type: Array,
      default: () => [],
    },
  });
  
  const genreColors = {
    1: 'info',
    2: 'danger', 
    3: 'warning',    
    4: 'warning', 
    5: 'danger',  
    6: 'primary', 
    7: 'success', 
    8: 'warning',
  };
  </script>
  
  <style scoped>
  .flex {
    display: flex;
    gap: 0.2rem;
  }
  </style>
  