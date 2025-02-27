<template>
  <h2>Reviewed Movies</h2>

  <div v-if="loading">Loading...</div>
  <div v-else-if="error">{{ error }}</div>
  
  <div v-else-if="reviews.length === 0">
    <el-empty description="No reviews yet" />
  </div>

  <div v-else>
    <p>{{ reviews.length }} rated movies so far</p>

    <div v-for="review in paginatedReviews" :key="review.id" class="review-item">
      <el-card class="user-review-card" shadow="hover">
        
        <router-link v-if="review.movieId" :to="`/movie/${review.movieId}`" class="movie-container">
          <img 
            v-if="review.movieImage" 
            :src="getImageUrl(review.movieImage)" 
            :alt="review.movieTitle" 
            class="movie-image" 
          />
          <div class="movie-details">
            <h2 class="movie-title">{{ review.movieTitle }}</h2>
            <h4 class="movie-year">{{ review.movieYear }}</h4>
          </div>
        </router-link>

        <el-empty v-else description="You have not rated any movies yet" />

        <div class="user-rating">
          <h4>Rated
            <Icon icon="mdi:star" style="color: #9b4dca;margin: -3px 7px;font-size: x-large;" />
            <span class="rating">
              {{ review.rating }}
              <span style="color: #888; font-size: 0.5em; margin-left: -7px;">/10</span>
            </span>
          </h4>
        </div>

        <div class="user-review-content">
          <p>{{ review.content }}</p>
        </div>

        <p v-if="review.wantsToWatchAgain !== null" class="watch-again">
          <Icon :icon="review.wantsToWatchAgain ? 'mdi:check-circle' : 'mdi:close-circle'" :style="{ color:'#9b4dca' }" />
          {{ review.wantsToWatchAgain ? 'Will watch again' : 'Will not watch again' }}
        </p>
      </el-card>
    </div>
  </div>

  <Pagination 
    :current-page="currentPage" 
    :total-elements="reviews.length" 
    :items-per-page="itemsPerPage" 
    @page-change="goToPage" 
  />
</template>

<script setup>
import { ref, computed, onMounted, watch, nextTick } from 'vue';
import axios from 'axios'; 
import Pagination from './Pagination.vue';
import { useAuthStore } from '../stores/auth.store';
import { Icon } from '@iconify/vue';

const reviews = ref([]);
const loading = ref(true);
const error = ref(null);
const currentPage = ref(1);
const itemsPerPage = ref(1);

const authStore = useAuthStore();
const user = computed(() => authStore.user);

const getImageUrl = (imageName) => `/api/image/${imageName}`;

const fetchReviews = async () => {
  try {
    const response = await axios.get(`/api/user/${user.value.id}/reviews`);

    if (Array.isArray(response.data)) {
      reviews.value = response.data;
    } else if (response.data && Array.isArray(response.data.reviews)) {
      reviews.value = response.data.reviews;
    } else {
      reviews.value = [];
    }

  } catch (err) {
    error.value = 'Failed to load reviews';
    console.error(err);
    reviews.value = [];
  } finally {
    loading.value = false;
  }
};

const totalPages = computed(() => Math.ceil(reviews.value.length / itemsPerPage.value));

const paginatedReviews = computed(() => {
  const start = (currentPage.value - 1) * itemsPerPage.value;
  const end = start + itemsPerPage.value;
  return reviews.value.slice(start, end);
});

const goToPage = (page) => {
  if (page < 1 || page > totalPages.value) return;
  currentPage.value = page;
};

onMounted(() => {
  fetchReviews();
});
</script>

<style scoped>
h2 {
  color: #4b0082;
  margin-bottom: 1rem;
}

h3 {
  color: #6e6e6e;
  font-size: 1.2em;
}

p {
  font-size: smaller;
  color: #6e6e6e;
}

.user-reviews {
  padding: 20px;
  margin: 0 auto;
  max-width: 1200px;
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
}

.review-item {
  margin-bottom: 30px;
  display: flex;
  flex-direction: column;
}

.movie-container {
  display: flex;
  align-items: center;
  text-decoration: none;
  color: inherit;
  transition: transform 0.3s ease;
}

.movie-container:hover {
  transform: scale(1.02);
}

.movie-image {
  width: 120px;
  height: 180px;
  object-fit: cover;
  border-radius: 8px;
  margin-right: 20px;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
}

.movie-details {
  flex: 1;
}

.movie-title {
  font-size: 1.4em;
  color: #333;
  margin: 0 0 10px;
}

.movie-year {
  font-size: 1.1em;
  color: #888;
  margin-top: -5px;
}

.user-rating h3 {
  color: #555;
  font-size: 1.2em;
  margin-top: 10px;
}

.rating {
  font-size: 1.6em;
  color: #9b4dca;
}

.user-review-content {
  font-size: 1em;
  color: #333;
  margin-top: 15px;
}

.user-review-card {
  background-color: #f9f9f9;
  border-radius: 8px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  transition: box-shadow 0.3s ease;
}

.user-review-card:hover {
  box-shadow: 0 6px 12px rgba(0, 0, 0, 0.15);
}

.watch-again {
  font-size: smaller;
  color: #9b4dca;
  margin-top: 25px;
  margin-bottom: -5px;
  display: flex;
  align-items: center;
  gap: 5px;
}
</style>