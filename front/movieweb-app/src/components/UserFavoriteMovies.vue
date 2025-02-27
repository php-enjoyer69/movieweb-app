<template>
  <div class="saved-movies">
    <h2>Favorite Movies</h2>
    <el-table v-if="favoriteMovies.length" :data="favoriteMovies" style="width: 100%">
      <el-table-column>
        <template #default="{ row }">
          <router-link :to="`/movie/${row.id}`">
            <div class="thumbnail-container">
              <img :src="getImageUrl(row.img)" alt="Movie cover" class="thumbnail" />
            </div>
          </router-link>
        </template>
      </el-table-column>
      <el-table-column prop="title" width="550">
        <template #default="{ row }">
          <router-link :to="`/movie/${row.id}`" class="movie-title">
            {{ row.title }}
          </router-link>
        </template>
      </el-table-column>
      <el-table-column width="120">
        <template #default="{ row }">
          <span 
            class="heart-icon" 
            :title="row.isFavorite ? 'Remove from favorites' : 'Add to favorites'"
            @click="toggleFavorite(row)"
          >
            <Icon :icon="row.isFavorite ? 'mdi:heart' : 'mdi:heart-outline'" />
          </span>
        </template>
      </el-table-column>
    </el-table>
    <el-empty v-else description="No favorite movies yet" />

    <Pagination
      :currentPage="currentPage"
      :totalElements="totalElements"
      :itemsPerPage="itemsPerPage"
      @page-change="goToPage"
    />
  </div>
</template>

<script setup>
import { ref, computed, onMounted, watch } from 'vue';
import { useAuthStore } from '../stores/auth.store';
import axios from 'axios';
import Pagination from './Pagination.vue';
import { ElMessage } from 'element-plus';
import { Icon } from '@iconify/vue';

const favoriteMovies = ref([]);
const authStore = useAuthStore();
const user = computed(() => authStore.user);
const currentPage = ref(1);
const itemsPerPage = ref(3);
const totalPages = ref(0);
const totalElements = ref(0);

const getImageUrl = (imageName) => `/api/image/${imageName}`;

const getFavoriteMovies = async () => {
  if (!user.value || !user.value.id) return;
  try {
    const response = await axios.get(`/api/movie/favorites/${user.value.id}`);
    if (response.data && Array.isArray(response.data)) {
      totalElements.value = response.data.length;
      totalPages.value = Math.ceil(totalElements.value / itemsPerPage.value);
      const start = (currentPage.value - 1) * itemsPerPage.value;
      favoriteMovies.value = response.data.slice(start, start + itemsPerPage.value);

      favoriteMovies.value.forEach(movie => {
        movie.isFavorite = true;
      });
    } else {
      favoriteMovies.value = [];
    }
  } catch (error) {
    console.error("Error fetching favorite movies:", error);
    favoriteMovies.value = [];
  }
};

const toggleFavorite = async (movie) => {
  if (!user.value) return showLoginModal();

  try {
    const url = `/api/movie/favorites/${movie.id}`;
    movie.isFavorite
      ? await axios.delete(url, { params: { userId: user.value.id } })
      : await axios.post(url, null, { params: { userId: user.value.id } });

    movie.isFavorite = !movie.isFavorite;

    ElMessage({
      message: movie.isFavorite ? 'Movie added to favorites!' : 'Movie removed from favorites.',
      type: 'success',
      duration: 3000,
    });
  } catch (error) {
    console.error("Error toggling favorite status", error);
    ElMessage({
      message: 'An error occurred while updating the favorites. Please try again.',
      type: 'error',
      duration: 3000,
    });
  }
};

const goToPage = (page) => {
  if (page < 1 || page > totalPages.value) return;
  currentPage.value = page;
  getFavoriteMovies();
};

onMounted(() => {
  getFavoriteMovies();
});

watch(user, () => {
  currentPage.value = 1;
  getFavoriteMovies();
});
</script>

<style scoped>
h1 {
  color: #4b0082;
  margin: 0 0 1rem;
}

.saved-movies {
  margin-top: 1rem;
}

.saved-movies h2 {
  color: #4b0082;
}

.el-table {
  margin-top: 1rem;
}

.thumbnail-container {
  display: flex;
  justify-content: center;
  align-items: center;
}

.thumbnail {
  width: 50px;  
  height: 70px;
  object-fit: cover;
  border-radius: 5px;
  border: 1px solid #ddd;
}

.heart-icon {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  font-size: 1.5em;
  color: #9b4dca;
  cursor: pointer;
  transition: color 0.3s ease;
}

.heart-icon:hover {
  color: #f02ac4;
}

.heart-icon:active {
  color: #f02ac4;
}

.movie-title {
  color: #5f5f5f;
  text-decoration: none;
  transition: color 0.3s ease;
}

.movie-title:hover {
  color: #acacac;
}
</style>
