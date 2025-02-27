<template>
  <div class="main-container">
    <router-link to="/admin">
      <el-link :underline="false" :icon="ArrowLeft">
        Back to Admin Panel
      </el-link>
    </router-link>
    <h2>Manage movie reviews</h2>

    <ReviewReports />
    
    <AdminMovieFilter @updateFilters="updateFilters" :genres="genres" />

    <ReviewList 
      :movies="movies" 
      :loading="loading" 
      :error="error" 
      :filters="filters"
      @delete-review="deleteReview" 
    />

    <Pagination v-if="totalPages > 1" :totalElements="totalElements" :itemsPerPage="itemsPerPage"
      :currentPage="currentPage" @page-change="goToPage" />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import axios from 'axios';
import { ElNotification } from 'element-plus';
import { ArrowLeft } from '@element-plus/icons-vue';
import Pagination from '@/components/Pagination.vue';
import ReviewList from '@/components/ReviewList.vue';
import AdminMovieFilter from '@/components/AdminMovieFilter.vue';
import ReviewReports from '@/components/ReviewReports.vue';


const movies = ref([]);
const loading = ref(true);
const error = ref(null);
const currentPage = ref(1);
const itemsPerPage = ref(15);
const totalPages = ref(0);
const totalElements = ref(0);
const genres = ref([]);


const filters = ref({
  title: '',
  minYear: null,
  maxYear: null,
  genres: [],
});

const fetchMovies = async () => {
  try {
    loading.value = true;

    const searchQuery = buildSearchQuery();

    const response = await axios.get('/api/movies', {
      params: {
        page: currentPage.value - 1,
        size: itemsPerPage.value,
        search: searchQuery,
      },
    });

    const movieData = response.data.content.map(movie => ({
      ...movie,
      reviews: [], 
    }));

    movies.value = movieData;
    totalElements.value = response.data.totalElements;
    totalPages.value = Math.ceil(response.data.totalElements / itemsPerPage.value);

    await Promise.all(
      movies.value.map(async (movie) => {
        const reviewsResponse = await axios.get(`/api/movie/${movie.id}/reviews`);
        movie.reviews = reviewsResponse.data.map(review => ({
          ...review,
          movieId: movie.id,
        }));
      })
    );

    error.value = null;
  } catch (err) {
    error.value = 'Failed to load data from the database';
    ElNotification.error({
      title: 'Error',
      message: 'Failed to load movies. Please try again.',
    });
  } finally {
    loading.value = false;
  }
};

const buildSearchQuery = () => {
  const queries = [];
  if (filters.value.title) {
    queries.push(`title:*${filters.value.title}*`);
  }
  if (filters.value.minYear !== null) {
    queries.push(`year>${filters.value.minYear}`);
  }
  if (filters.value.maxYear !== null) {
    queries.push(`year<${filters.value.maxYear}`);
  }
  if (filters.value.genres.length > 0) {
    const genreQueries = filters.value.genres.map(genre => `genres;${genre}`);
    queries.push(`(${genreQueries.join(',')})`);
  }

  return queries.join(',');
};

const updateFilters = (newFilters) => {
  filters.value = newFilters;
  currentPage.value = 1;
  fetchMovies();
};

const goToPage = (page) => {
  if (page < 1 || page > totalPages.value) return;
  currentPage.value = page;
  fetchMovies();
};

const deleteReview = async (movieId, reviewId) => {
  try {
    await axios.delete(`/api/movie/${movieId}/review/${reviewId}`);

    const movie = movies.value.find(movie => movie.id === movieId);
    if (movie) {
      movie.reviews = movie.reviews.filter(review => review.id !== reviewId);
    }

    ElNotification.success({
      title: 'Success',
      message: 'Review has been successfully deleted',
    });
  } catch (err) {
    ElNotification.error({
      title: 'Error',
      message: 'Failed to delete the review. Please try again.',
    });
  }
};

const fetchGenres = async () => {
  try {
    const response = await axios.get('/api/movie-genres', {
      params: { size: 9999 },
    });
    genres.value = response.data.content || [];
  } catch (error) {
    ElNotification.error({
      title: 'Error',
      message: 'There was an error loading the movie genres.',
    });
    genres.value = [];
  }
};

onMounted(() => {
  fetchMovies(currentPage.value);
  fetchGenres();
});
</script>

<style scoped>
.main-container {
  width: 1200px;
  margin: 50px auto 8rem auto;
  padding: 20px;
  border: 1px solid #dcdfe6;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  background-color: #ffffff;
  justify-content: space-between;
  gap: 50px;
}

h2 {
  text-align: center;
  margin-bottom: 50px;
}

.el-link {
  margin-top: 20px;
  margin-bottom: 20px;
  font-size: 16px;
  font-weight: 600;
  color: #333;
}
</style>