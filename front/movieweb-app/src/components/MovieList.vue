<template>
  <div class="movie-list">
    <MovieFilter @updateFilters="updateFilters" />

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

const movies = ref([]);
const loading = ref(true);
const error = ref(null);
const currentPage = ref(1);
const itemsPerPage = ref(5);
const totalPages = ref(0);
const totalElements = ref(0);

const filters = ref({
  title: '',
});

const buildSearchQuery = () => {
  const queries = [];
  if (filters.value.title) {
    queries.push(`title:*${filters.value.title}*`);
  }
  return queries.join(',');
};

const fetchData = async (page) => {
  try {
    loading.value = true;
    const searchQuery = buildSearchQuery();

    const response = await axios.get('/api/movies', {
      params: {
        page: page - 1,
        size: itemsPerPage.value,
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

onMounted(() => fetchData(currentPage.value));
</script>

<style scoped>
.movie-grid {
  display: flex;
  justify-content: space-between; /* Odstępy między elementami */
  flex-wrap: nowrap; /* Jedna linia */
  padding: 0; /* Bez marginesów wokół siatki */
}

.movie-item {
  flex: 1 1 auto; /* Automatyczne skalowanie w jednej linii */
  max-width: calc(100% / 5); /* 6 elementów na jedną linię */
  margin: 0 5px; /* Marginesy między elementami */
  box-sizing: border-box;
}

.movie-item:first-child {
  margin-left: 10px; /* Brak marginesu z lewej strony */
}

.movie-item:last-child {
  margin-right: 0; /* Brak marginesu z prawej strony */
}


</style>
