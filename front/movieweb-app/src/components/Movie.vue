<template>
  <router-link :to="`/movie/${movie.id}`" class="movie-container">
    <img :src="getImageUrl(movie.img)" alt="Movie Image" class="movie-img" />
    <div class="movie-title">{{ movie.title }}</div>
    <div class="movie-genres">
      <span v-if="movie.genres && movie.genres.length" v-for="genre in movie.genres" :key="genre.id" class="genre-tag">
        {{ genre.name }}
      </span>
    </div>
    <div class="movie-year">{{ movie.year }}</div>
  </router-link>
</template>
  
  <script setup>
  import { defineProps } from 'vue';
  import MovieGenreTags from './MovieGenreTags.vue';
  
  defineProps({
  movie: {
    type: Object,
    required: true,
  },
});
  
  const getImageUrl = (imageName) => `/api/image/${imageName}`;
  </script>
  
  <style scoped>
.movie-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  text-align: center;
  width: 220px;
  border: 1px solid #dcdfe6; 
  border-radius: 8px; 
  background-color: #f9f9f9; 
  padding: 5px;
  box-sizing: border-box;
  text-decoration: none;
  color: inherit;
  transition: transform 0.3s, box-shadow 0.3s; 
}

.movie-container:hover {
  transform: scale(1.05);
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.2); 
}

.movie-img {
  width: 100%; 
  height: auto; 
  max-height: 300px; 
  object-fit: cover; 
  border-radius: 8px; 
  margin-bottom: 15px; 
}

.movie-title {
  margin-bottom: 10px;
  font-size: 1.2em;
  font-weight: bold; 
}

.movie-year {
  font-weight: bold;
  color: #666;
  margin-bottom: 15px;
}

.movie-genres {
  display: flex;
  flex-wrap: wrap;
  gap: 3px;
  justify-content: center;
  margin-bottom: 7px;
}

.genre-tag {
  background-color: #f4f4f4;
  color: #858585;
  font-size: 0.8em;
  padding: 1px 8px;
  border-radius: 4px;
  white-space: nowrap;
}
</style>
