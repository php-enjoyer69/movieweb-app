<template>
  <div class="movie-list">
    <MovieFilter @updateFilters="updateFilters" :genres="genres"/>

    <div v-if="loading">Loading...</div> 
    <div v-else-if="error">{{ error }}</div>

    <div v-else-if="movies.length === 0">
      <el-empty description="No movies match your search criteria :(" />
    </div>

    <div v-else class="movie-grid">
      <div class="movie-item" v-for="movie in movies" :key="movie.id">
        <Movie :movie="movie" />
      </div>
    </div>

    <Pagination
      v-if="totalPages > 1"
      :current-page="currentPage"
      :total-elements="totalElements"
      :items-per-page="itemsPerPage"
      @page-change="goToPage"
    />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import axios from 'axios';
import MovieFilter from '@/components/MovieFilter.vue';
import Movie from '@/components/Movie.vue';
import Pagination from '@/components/Pagination.vue';
import { ElNotification } from 'element-plus';

const movies = ref([]);
const loading = ref(true);
const error = ref(null);
const currentPage = ref(1);
const itemsPerPage = ref(5);
const totalPages = ref(0);
const totalElements = ref(0);
const genres = ref([]);

const filters = ref({
  title: '',
  minYear: null,
  maxYear: null,
  sortByRating: null,
  sortByYear: null,
  genres: [],
});

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

const fetchData = async (page) => {
  try {
    loading.value = true;
    const searchQuery = buildSearchQuery();

    const sortOrder = filters.value.sortByRating === 'ratingAsc' ? 'rating,asc' :
      filters.value.sortByRating === 'ratingDesc' ? 'rating,desc' :
        filters.value.sortByYear === 'yearAsc' ? 'year,asc' :
          filters.value.sortByYear === 'yearDesc' ? 'year,desc' :
            'id,asc';

    const response = await axios.get('/api/movies', {
      params: {
        page: page - 1,
        size: itemsPerPage.value,
        sort: sortOrder,
        search: searchQuery,
      },
    });

    movies.value = response.data.content;
    totalPages.value = Math.ceil(response.data.totalElements / itemsPerPage.value);
    totalElements.value = response.data.totalElements;
    error.value = null;
  } catch (err) {
    error.value = 'There was an error loading the movies.';
  } finally {
    loading.value = false;
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

const updateFilters = (newFilters) => {
  filters.value = newFilters;
  currentPage.value = 1;
  fetchData(currentPage.value);
};

const goToPage = (page) => {
  if (page < 1 || page > totalPages.value) return;
  currentPage.value = page;
  fetchData(currentPage.value);
};

onMounted(() => {
  fetchGenres();
  fetchData(currentPage.value)
  });
</script>

<style scoped>
.movie-grid {
  display: flex;
  justify-content: space-between;
  flex-wrap: nowrap;
  padding: 0; 
}

.movie-item {
  flex: 1 1 auto; 
  max-width: calc(100% / 5);
  margin: 0 5px;
  box-sizing: border-box;
}

.movie-item:first-child {
  margin-left: 10px;
}

.movie-item:last-child {
  margin-right: 0;
}


</style>
