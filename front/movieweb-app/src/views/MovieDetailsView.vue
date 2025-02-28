<template>
  <div class="movie-detail-page">
    <el-link :underline="false" :icon="ArrowLeft" @click="goBack">
      Back to Movie List
    </el-link>

    <el-button-group class="navigation-buttons">
      <el-button type="success" @click="goToPreviousMovie">
        <el-icon class="button-icon">
          <Icon icon="mdi:chevron-left" />
        </el-icon>
        Previous Movie
      </el-button>

      <el-button type="success" @click="goToNextMovie">
        Next Movie
        <el-icon class="button-icon button-icon--right">
          <Icon icon="mdi:chevron-right" />
        </el-icon>
      </el-button>
    </el-button-group>

    <Skeleton v-if="loading" :rows="10" :animated="true" />
    <div v-else-if="error">{{ error }}</div>

    <MovieDetails v-else :movie="movie" :user="user" />
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import axios from 'axios';
import { Icon } from '@iconify/vue';
import { ElButtonGroup, ElButton, ElIcon } from 'element-plus';
import { ArrowLeft, ArrowRight } from '@element-plus/icons-vue';
import MovieDetails from '@/components/MovieDetails.vue';
import Skeleton from '@/components/Skeleton.vue';

const route = useRoute();
const router = useRouter();
const movie = ref(null);
const loading = ref(true);
const error = ref(null);

const fetchMovie = async () => {
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
  fetchMovie();
});

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

onMounted(fetchMovie);
</script>

<style scoped>
.movie-detail-page {
  padding: 20px;
  margin-left: 200px;
  margin-right: 200px;
  background-color: #fff;
}

.el-link {
  margin-top: 20px;
  margin-bottom: 30px;
  font-size: 16px;
  font-weight: 600;
  color: #333;
}

.navigation-buttons {
  display: flex;
  margin-top: 5px;
  margin-bottom: 10px;
  width: 23.5%;
}

.el-button {
  flex: 1;
}
</style>
