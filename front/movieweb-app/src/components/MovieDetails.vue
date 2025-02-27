<template>
  <div class="movie-detail-content">
    <img :src="getImageUrl(movie.img)" alt="Movie Image" class="movie-img" />
    <div class="movie-info">
      <h1>{{ movie.title }}</h1>
      <h3>{{ movie.year }}</h3>
      <el-collapse-transition>
        <p v-if="showFullDescription && movie.description">{{ movie.description }}</p>
        <p v-else>{{ truncatedDescription }}...</p>
      </el-collapse-transition>

      <el-link :underline="false" type="text" @click="toggleDescription">
        {{ showFullDescription ? 'Show less' : 'Show more' }}
        <el-icon>
          <i :class="showFullDescription ? ArrowUp : ArrowDown"></i>
        </el-icon>
      </el-link>
      <div class="movie-tags">
        <MovieGenreTags :genres="movie.genres" />
      </div>
      <MovieCrew :movieId="movie.id" />

      <div class="movie-rating">
        <p style="margin: 0 0 -20px;"><strong>Average score</strong></p>
        <h2 class="rating">
          <Icon icon="mdi:star" style="color: #9b4dca;margin-bottom:-6px;font-size: xx-large;" />
          {{ averageRating ? averageRating.toFixed(2) : 'N/A' }}
          <span style="color: #888;margin:2px -3px; font-size: large;">/10</span>
          <span class="rating-count">{{ movie.ratingCount }} reviews total</span>
        </h2>
      </div>


      <MovieDetailsButtons :hasReviewed="hasReviewed" :toggleRatingForm="toggleRatingForm"
        :toggleWatchlist="toggleWatchlist" :toggleFavorite="toggleFavorite" :showLoginModal="showLoginModal"
        :isInWatchlist="isInWatchlist" :isFavorite="isFavorite" :showRatingForm="showRatingForm"
        :toggleEditReviewForm="toggleEditReviewForm" :showEditReviewForm="showEditReviewForm" :deleteReview="deleteReview"/>
    </div>
  </div>

  <transition name="slide-up" mode="out-in">
    <ReviewMovieForm v-if="showRatingForm" :movieId="movie.id" />
  </transition>

  <transition name="slide-up" mode="out-in">
    <EditReviewMovieForm v-if="showEditReviewForm && hasReviewed" :movieId="movie.id" :reviewId="userReview?.id"
      :review="userReview" />
  </transition>

  <MovieActors :movieId="movie.id" />
  <MovieReviews :movieId="movie.id" />
  <LoginRequiredModal v-if="loginModalVisible" :visible="loginModalVisible" @close="closeModal" />
</template>

<script setup>
import axios from 'axios';
import { ref, onMounted } from 'vue';
import { Icon } from '@iconify/vue';
import { useAuthStore } from '@/stores/auth.store';
import { storeToRefs } from 'pinia';
import MovieGenreTags from '@/components/MovieGenreTags.vue';
import MovieDetailsButtons from '@/components/MovieDetailsButtons.vue';
import MovieReviews from '@/components/MovieReviews.vue';
import LoginRequiredModal from '@/components/LoginRequiredModal.vue';
import ReviewMovieForm from './ReviewMovieForm.vue';
import EditReviewMovieForm from './EditReviewMovieForm.vue';
import MovieActors from '@/components/MovieActors.vue';
import MovieCrew from './MovieCrew.vue';
import { ElMessage } from 'element-plus';

const { user } = storeToRefs(useAuthStore());
const props = defineProps({ movie: Object });

const showRatingForm = ref(false);
const hasReviewed = ref(false);
const loginModalVisible = ref(false);
const showFullDescription = ref(false);
const truncatedDescription = ref(props.movie.description?.slice(0, 200) || '');
const isFavorite = ref(false);
const isInWatchlist = ref(false);
const averageRating = ref(props.movie.averageRating || 0);
const showEditReviewForm = ref(false);
const userReview = ref(null);

onMounted(async () => {
  if (!user.value) {
    return;
  }
  try {
    const [watchlistRes, favoriteRes, reviewRes] = await Promise.all([
      axios.get(`/api/movie/watchlist/${user.value.id}/isInWatchlist/${props.movie.id}`),
      axios.get(`/api/movie/favorites/${user.value.id}/isFavorite/${props.movie.id}`),
      axios.get(`/api/movie/${props.movie.id}/review/${user.value.id}`)
    ]);

    isInWatchlist.value = watchlistRes.data;
    isFavorite.value = favoriteRes.data;

    if (reviewRes.data) {
      hasReviewed.value = true;
      userReview.value = reviewRes.data;
    } else {
      hasReviewed.value = false;
      userReview.value = null;
    }
  } catch (error) {
    console.error("Error fetching movie status", error);
  }
});

const toggleWatchlist = async () => {
  try {
    const url = `/api/movie/watchlist/${props.movie.id}`;
    const response = await axios({
      method: isInWatchlist.value ? 'delete' : 'post',
      url: url,
      params: { userId: user.value.id },
    });

    isInWatchlist.value = !isInWatchlist.value;

    ElMessage({
      message: isInWatchlist.value ? 'Movie added to watchlist!' : 'Movie removed from watchlist.',
      type: 'success',
      duration: 3000,
    });
  } catch (error) {
    console.error("Error toggling watchlist status", error);
    ElMessage({
      message: 'An error occurred while updating the watchlist. Please try again.',
      type: 'error',
      duration: 3000,
    });
  }
};

const toggleFavorite = async () => {
  if (!user.value) return showLoginModal();
  try {
    const url = `/api/movie/favorites/${props.movie.id}`;
    isFavorite.value
      ? await axios.delete(url, { params: { userId: user.value.id } })
      : await axios.post(url, null, { params: { userId: user.value.id } });

    isFavorite.value = !isFavorite.value;

    ElMessage({
      message: isFavorite.value ? 'Movie added to favorites!' : 'Movie removed from favorites.',
      type: 'success',
      duration: 3000,
    });

  } catch (error) {
    console.error("Error toggling favorite status", error);
  }
};

const deleteReview = async () => {
  if (!user.value || !userReview.value) return;

  try {
    await axios.delete(`/api/movie/${props.movie.id}/review/${userReview.value.id}`);

    hasReviewed.value = false;
    userReview.value = null;
    showEditReviewForm.value = false;

    ElMessage({
      message: 'Review deleted successfully',
      type: 'success',
      duration: 3000,
    });
    setTimeout(() => {
        window.location.reload();
      }, 700);
  } catch (error) {
    console.error("Error deleting the review", error);
    ElMessage({
      message: 'An error occurred while deleting the review. Please try again',
      type: 'error',
      duration: 3000,
    });
  }
};

const showLoginModal = () => {
  loginModalVisible.value = true;
};

const closeModal = () => {
  loginModalVisible.value = false;
};

const toggleDescription = () => {
  showFullDescription.value = !showFullDescription.value;
};

const toggleRatingForm = () => {
  user.value ? showRatingForm.value = !showRatingForm.value : showLoginModal();
};

const toggleEditReviewForm = () => {
  if (!user.value) {
    showLoginModal();
    return;
  }
  showEditReviewForm.value = !showEditReviewForm.value;
};

const getImageUrl = (imageName) => `/api/image/${imageName}`;
</script>

<style scoped>
.movie-detail-content {
  display: flex;
  gap: 20px;
}

.movie-img {
  width: 300px;
  height: 450px;
  border-radius: 8px;
  object-fit: cover;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
}

.movie-info {
  flex: 1;
  overflow: hidden;
}

h1 {
  font-size: 2em;
  margin-top: -1px;
}

h3 {
  color: #6d6d6d;
  margin-top: -15px;
}

.el-link {
  margin-bottom: 25px;
}

.movie-tags {
  margin-bottom: -20px;
}

.movie-info p {
  white-space: normal;
  word-wrap: break-word;
  overflow-wrap: break-word;
  margin-bottom: 20px;
}

.el-collapse-transition {
  transition: max-height 0.3s ease-out;
  max-height: 400px;
}

.movie-info p {
  max-height: 300px;
  overflow-y: auto;
}

.rating {
  font-size: xx-large;
}

.rating-count {
  font-size: 0.4em;
  color: #888;
  font-weight: 400;
  margin-left: 15px;
}
</style>
