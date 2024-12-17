<template>
    <div class="movie-detail-content">
      <img :src="getImageUrl(movie.img)" alt="Movie Image" class="movie-img" />
      <div class="movie-info">
        <h1>{{ movie.title }}</h1>
        <p>{{ movie.description }}</p>
        <MovieGenreTags :genres="movie.genres" />
        <p><strong>Director:</strong> {{ movie.director }}</p>
        <p><strong>Release Date:</strong> {{ movie.year }}</p>
        <p><strong>Rating:</strong> {{ movie.rating }}</p>
        <el-button type="primary" class="rate-movie-button" v-if="user !== null">
          <Icon icon="mdi:star" class="star-icon" />
          Rate this movie
        </el-button>
        <el-button type="primary" class="rate-movie-button" v-if="user !== null">
          <Icon icon="mdi:plus" class="plus-icon" />
          Add to watchlist
        </el-button>
      </div>
    </div>
  </template>
  
  <script setup>
  import { defineProps } from 'vue';
  import { Icon } from '@iconify/vue';
  import { ElButtonGroup, ElButton, ElIcon } from 'element-plus';
  import MovieGenreTags from '@/components/MovieGenreTags.vue';
  
  const props = defineProps({
    movie: Object,
    user: Object,
  });
  
  const getImageUrl = (imageName) => `/api/image/${imageName}`;
  </script>
  
  <style scoped>
  .movie-detail-content {
    display: flex;
    gap: 20px;
  }
  
  .movie-img {
    width: 300px;
    height: auto;
    border-radius: 8px;
    object-fit: cover;
    box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
  }
  
  .movie-info {
    flex: 1;
  }
  
  h1 {
    font-size: 2em;
    color: #333;
  }
  
  p {
    margin: 10px 0;
    line-height: 1.6;
  }
  
  .rate-movie-button {
    display: inline-flex;
    align-items: center;
    gap: 8px;
    margin-top: 20px;
    padding: 10px 20px;
    font-size: 1em;
    color: #fff;
    border: none;
    border-radius: 5px;
    cursor: pointer;
    transition: background-color 0.3s;
  }
  
  .rate-movie-button .star-icon {
    font-size: 1.2em;
    margin-left: -4px;
    margin-right: 5px;
    color: gold;
  }
  
  .rate-movie-button .plus-icon {
    font-size: 1.2em;
    margin-left: -4px;
    margin-right: 5px;
    color: #d7b0ff;
  }
  </style>
  