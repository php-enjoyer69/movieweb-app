<template>
    <div class="movie-detail-page">
      <el-button
        type="primary"
        link
        @click="goBack"
        class="back-button"
      >
        <Icon icon="mdi:arrow-left" class="back-icon" /> Back to Movie List
      </el-button>
  
      <el-button-group class="navigation-buttons">
  <!-- Previous Movie Button -->
  <el-button type="primary" @click="goToPreviousMovie">
    <el-icon class="button-icon">
      <Icon icon="mdi:chevron-left" />
    </el-icon>
    Previous Movie
  </el-button>

  <!-- Next Movie Button -->
  <el-button type="primary" @click="goToNextMovie">
    Next Movie
    <el-icon class="button-icon button-icon--right">
      <Icon icon="mdi:chevron-right" />
    </el-icon>
  </el-button>
</el-button-group>

  
      <div v-if="loading">Loading...</div>
      <div v-else-if="error">{{ error }}</div>
      <div v-else class="movie-detail-content">
        <img :src="getImageUrl(movie.img)" alt="Movie Image" class="movie-img" />
        <div class="movie-info">
          <h1>{{ movie.title }}</h1>
          <p>{{ movie.description }}</p>
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
    </div>
  </template>
  
  <script setup>
  import { ref, onMounted, watch } from 'vue';
  import { useRoute, useRouter } from 'vue-router';
  import axios from 'axios';
  import { Icon } from '@iconify/vue';
  import { ElButtonGroup, ElButton, ElIcon } from 'element-plus';
  import { ArrowLeft, ArrowRight } from '@element-plus/icons-vue';
  
  const route = useRoute();
  const router = useRouter(); 
  const movie = ref(null);
  const loading = ref(true);
  const error = ref(null);
  
  const fetchMovieDetails = async () => {
    try {
      loading.value = true;
      const response = await axios.get(`/api/movie/${route.params.id}`);
      movie.value = response.data;
      error.value = null;
    } catch (err) {
      error.value = 'Error loading movie details.';
    } finally {
      loading.value = false;
    }
  };
  
  watch(() => route.params.id, (newId) => {
    fetchMovieDetails();
  });
  
  const getImageUrl = (imageName) => `/api/image/${imageName}`;
  
  const goToNextMovie = () => {
    const nextMovieId = parseInt(route.params.id) + 1;
    router.push(`/movie/${nextMovieId}`);
  };
  
  const goToPreviousMovie = () => {
    const prevMovieId = parseInt(route.params.id) - 1;
    if (prevMovieId > 0) {
      router.push(`/movie/${prevMovieId}`);
    }
  };
  
  const goBack = () => {
    router.push('/movies'); 
  };
  
  onMounted(fetchMovieDetails);
  </script>
  
  <style scoped>
  .movie-detail-page {
    padding: 20px;
    margin-left: 100px;
    margin-right: 100px;
    background-color: #fff;
  }
  
  .back-button {
    display: inline-block;
    margin-bottom: 10px;
  }
  
  .back-button:hover {
    color: #9b59b6 !important;
  }
  
  .back-icon {
    font-size: 1.5em;
    color: #4b0082;
  }
  
  .navigation-buttons {
    display: block;
    margin-top: 5px;
    margin-bottom: 5px;
  }
  
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
    color: gold;
  }
  
  .rate-movie-button .plus-icon {
    font-size: 1.2em;
    color: rgb(99, 221, 99);
  }
  </style>
  