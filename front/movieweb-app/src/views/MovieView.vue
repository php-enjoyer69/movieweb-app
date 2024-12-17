<template>
<div class="movie-view-page">
    <h1>List of Movies</h1>
    <h3>Here you can browse all movies available</h3>
    <MovieList/>
</div>
</template>
  
<script setup>
import MovieList from '@/components/MovieList.vue';

</script>
  
<style scoped>
h1 {
  font-size: 2em;
  color: #333;
}

h3 {
  color: #575757;
  margin-bottom: 45px;
}

.movie-view-page {
  padding: 20px;
  margin: 0 auto;
  max-width: 1250px;
  background-color: #fff;
  border-radius: 8px; 
}

</style>