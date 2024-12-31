<template>
  <div class="movie-detail-content">
    <img :src="getImageUrl(movie.img)" alt="Movie Image" class="movie-img" />
    <div class="movie-info">
      <h1>{{ movie.title }}</h1>
      <h3>{{ movie.year }}</h3>
      <p>{{ movie.description }}</p>
      <MovieGenreTags :genres="movie.genres" />
      <p><strong>Director:</strong> {{ movie.director }}</p>

      <div class="movie-rating">
        <p><strong>Average rating:</strong></p>
        <el-rate v-model="averageRating" disabled show-score text-color="#ff9900" score-template="{value} points" />
      </div>

      <el-button type="primary" class="rate-movie-button" v-if="user" @click="openRateModal">
  <Icon icon="mdi:star" class="star-icon" />
  Rate this movie
</el-button>
      <el-button type="primary" class="rate-movie-button" v-if="user" @click="addToWatchlist">
        <Icon icon="mdi:plus" class="plus-icon" />
        Add to watchlist
      </el-button>

      <el-button type="primary" class="rate-movie-button" v-if="!user" @click="showLoginModal">
        <Icon icon="mdi:star" class="star-icon" />
        Rate this movie
      </el-button>
      <el-button type="primary" class="rate-movie-button" v-if="!user" @click="showLoginModal">
        <Icon icon="mdi:plus" class="plus-icon" />
        Add to watchlist
      </el-button>
    </div>
  </div>

  <MovieReviews :movieId="movie.id" />

  <div v-if="loginModalVisible" class="modal-overlay" @click="closeModal">
    <LoginModal :visible="loginModalVisible" @close="closeModal" />
  </div>

  <div v-if="rateModalVisible" class="modal-overlay" @click="closeModal">
    <RateModal :visible="rateModalVisible" :movieId="movie.id" @close="closeModal" />
  </div>

</template>

<script setup>
import { defineProps, ref } from 'vue';
import { useRouter } from 'vue-router';
import { Icon } from '@iconify/vue';
import MovieGenreTags from '@/components/MovieGenreTags.vue';
import { useAuthStore } from '@/stores/auth.store';
import { storeToRefs } from 'pinia';
import LoginModal from './LoginModal.vue';
import RateModal from './RateModal.vue';
import MovieReviews from './MovieReviews.vue';

const authStore = useAuthStore();
const { user } = storeToRefs(authStore);

const props = defineProps({
  movie: Object,
  user: Object,
});

const router = useRouter();
const loginModalVisible = ref(false);
const rateModalVisible = ref(false);

const showLoginModal = () => {
  loginModalVisible.value = true;
};

const closeModal = () => {
  loginModalVisible.value = false;
  rateModalVisible.value = false;
};

const openRateModal = () => {
  console.log("Rate Modal Visible:", rateModalVisible.value);
  rateModalVisible.value = true;
};

const addToWatchlist = () => {
  // Your code to add the movie to watchlist
};

const getImageUrl = (imageName) => `/api/image/${imageName}`;

const averageRating = ref(props.movie.averageRating || 0);

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

h3 {
  margin-top: -10px;
  margin-bottom: 40px;
  color: #6d6d6d;
}

p {
  margin: 10px 0;
  line-height: 1.6;
}

.movie-rating {
  margin: 10px 0;
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
  margin-left: -2px;
  margin-right: 5px;
  color: #d7b0ff;
}
</style>
