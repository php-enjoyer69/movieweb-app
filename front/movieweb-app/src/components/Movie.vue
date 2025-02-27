<template>
  <router-link :to="`/movie/${movie.id}`" class="movie-container">
    <img :src="getImageUrl(movie.img)" alt="Movie Image" class="movie-img" />
    <div class="movie-title">{{ movie.title }}</div>
    <div class="movie-genres">
  <span v-if="movie.genres && movie.genres.length" 
        v-for="(genre, index) in movie.genres.slice(0, 3)" 
        :key="genre.id" 
        class="genre-tag">
    {{ genre.name }}
  </span>
</div>
    <div class="movie-year">{{ movie.year }}</div>
  </router-link>
</template>
  
  <script setup>
  import { defineProps } from 'vue';
  
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
  width: 230px;
  height: 445px;
  border: 1px solid #dcdfe6; 
  border-radius: 8px; 
  background-color: #f9f9f9; 
  padding: 3px;
  box-sizing: border-box;
  text-decoration: none;
  color: inherit;
  transition: transform 0.3s, box-shadow 0.3s; 
}

.movie-img {
  width: 100%;
  height: 320px; 
  object-fit: cover;
  border-radius: 8px; 
  margin-bottom: 10px;
  background-color: transparent;
}

.movie-container:hover {
  transform: scale(1.05);
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.2);
}

.movie-year {
  font-weight: bold;
  color: #666;
  margin-top: 5px;
  margin-bottom: 15px;
}

.genre-tag {
  background-color: #f4f4f4;
  color: #858585;
  font-size: 0.65em;
  padding: 2px 5px; 
  border-radius: 4px;
  white-space: nowrap;
  margin-right: 5px;
}

.movie-title {
  margin-top: 13px;
  margin-bottom: -25px;
  font-size: 1.3em;
  font-weight: bold; 
  padding: 0 10px;
  min-height: 48px;
  display: flex;
  justify-content: center;
  align-items: center;
  color: #333;
  width: 100%;
  overflow: hidden; 
  text-overflow: ellipsis;
  white-space: nowrap;
  text-align: center;
  display: block;
}

.movie-genres {
  margin: 5px 0px 5px;
}
</style>
