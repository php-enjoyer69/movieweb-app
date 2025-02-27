<template>
  <div>
    <h3>Reviews: {{ reviews.length }}</h3>
    <Skeleton v-if="loading" :rows="10" :animated="true" />
    <div v-else-if="error">{{ error }}</div>

    <div v-else-if="reviews.length === 0">
      <el-empty description="No reviews yet" />
    </div>

    <el-card v-for="review in paginatedReviews" :key="review.id" class="review-card">
      <el-tooltip 
        v-if="isReported(review.id)" 
        content="Review has been sent for moderation" 
        placement="top">
        <Icon 
          :icon="'stash:flag-duotone'" 
          :style="{ color: 'rgb(255, 67, 67)', cursor: 'not-allowed' }" 
          class="flag-icon"
          :disabled="true"
        />
      </el-tooltip>

      <el-tooltip 
        v-else 
        content="Report this review" 
        placement="top">
        <Icon 
          :icon="'stash:flag'" 
          :style="{ color: 'gray', cursor: 'pointer' }" 
          @click="toggleReport(review.id)" 
          class="flag-icon"
        />
      </el-tooltip>

      <el-row>
        <el-col :span="24">
          <el-avatar :size="40" :src="getUserAvatar(review)" class="user-avatar" />
          <router-link :to="`/profile/${review.userId}`" class="user-name">
            {{ review.userName }}
          </router-link>
          <span class="review-date"> at {{ formatDate(review.createdAt) }}</span>
        </el-col>
      </el-row>
      <el-row>
        <p class="rating">
          <Icon icon="mdi:star" style="color: #9b4dca;" /> {{ review.rating }}<span
            style="color: #888; font-size: 0.5em;">/10</span>
        </p>
      </el-row>
      <el-row>
        <el-col :span="24">
          <p>{{ review.content }}</p>
          <p v-if="review.wantsToWatchAgain !== null" class="watch-again">
            <Icon :icon="review.wantsToWatchAgain ? 'mdi:check-circle' : 'mdi:close-circle'"
              :style="{ color: '#9b4dca' }" />
            {{ review.wantsToWatchAgain ? 'Will watch again' : 'Will not watch again' }}
          </p>
        </el-col>
      </el-row>

      <el-row class="like-dislike-buttons">
  <span class="count">{{ review.likeCount }}</span>
  <el-tooltip content="Like this review" placement="top">
    <Icon 
      :icon="review.userLike ? 'mdi:thumb-up-outline' : 'mdi:thumb-up-outline'" 
      :style="{
        color: review.userLike ? '#4CAF50' : 'gray',
        cursor: review.userLike === undefined ? 'default' : 'pointer',
        fontSize: '24px',
        outline: 'none'
      }" 
      @click="likeReview(review.id)" 
    />
  </el-tooltip>
  <span class="count">{{ review.dislikeCount }}</span>
  <el-tooltip content="Dislike this review" placement="top">
    <Icon 
      :icon="review.userDislike ? 'mdi:thumb-down-outline' : 'mdi:thumb-down-outline'" 
      :style="{
        color: review.userDislike ? '#f44336' : 'gray',
        cursor: review.userDislike === undefined ? 'default' : 'pointer',
        fontSize: '24px',
        outline: 'none'
      }" 
      @click="dislikeReview(review.id)" 
    />
  </el-tooltip>
</el-row>
    </el-card>

    <Pagination :current-page="currentPage" :total-elements="reviews.length"
      :items-per-page="itemsPerPage" @page-change="goToPage" />

    <LoginRequiredModal v-if="loginModalVisible" :visible="loginModalVisible" @close="closeModal" />
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue';
import axios from 'axios';
import { useAuthStore } from '@/stores/auth.store';
import { storeToRefs } from 'pinia';
import { Icon } from '@iconify/vue';
import Pagination from '@/components/Pagination.vue';
import Skeleton from './Skeleton.vue';
import { ElMessage } from 'element-plus';
import LoginRequiredModal from '@/components/LoginRequiredModal.vue';

const props = defineProps({
  movieId: {
    type: Number,
    required: true
  }
});

const authStore = useAuthStore();
const { user } = storeToRefs(authStore);
const reviews = ref([]);
const loading = ref(true);
const error = ref(null);
const currentPage = ref(1);
const itemsPerPage = ref(5);
const totalPages = ref(0);
const reports = ref({});
const loginModalVisible = ref(false);

const defaultAvatar = 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQBsdZ-yU8qdxr0WrIA8HLa31tB_cCFZJt-2Q&s';

const formatDate = (timestamp) => {
  if (!timestamp) return '-';
  const date = new Date(timestamp);
  const padZero = (num) => (num < 10 ? `0${num}` : num);
  const months = [
    'Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun',
    'Jul', 'Aug', 'Sept', 'Oct', 'Nov', 'Dec'
  ];

  const year = date.getFullYear();
  const month = months[date.getMonth()];
  const day = padZero(date.getDate());
  const hours = padZero(date.getHours());
  const minutes = padZero(date.getMinutes());
  const seconds = padZero(date.getSeconds());

  return `${hours}:${minutes}:${seconds}, ${day} ${month} ${year}`;
};

const fetchReviews = async () => {
  try {
    const response = await axios.get(`/api/movie/${props.movieId}/reviews`);
    reviews.value = response.data;
    totalPages.value = Math.ceil(reviews.value.length / itemsPerPage.value);

    const likePromises = reviews.value.map(async (review) => {
      try {
        const likeResponse = await axios.get(`/api/review/${review.id}/like/${user.value.id}/exists`);

        review.userLike = false;
        review.userDislike = false;

        if (likeResponse.data === 'LIKE') {
          review.userLike = true;
        } else if (likeResponse.data === 'DISLIKE') {
          review.userDislike = true;
        } else if (likeResponse.data === 'NONE') {
          review.userLike = false;
          review.userDislike = false;
        }
      } catch (err) {
        console.error(`Error checking like/dislike for review ${review.id}:`, err);
      }
    });

    await Promise.all(likePromises);
  } catch (err) {
    error.value = 'Failed to fetch reviews. Please try again later.';
    console.error('Error fetching reviews:', err);
  } finally {
    loading.value = false;
  }
};

const getUserAvatar = (review) => {
  return review.userImg ? `/api/image/${review.userImg}` : defaultAvatar;
};

onMounted(() => {
  fetchReviews();
});

const paginatedReviews = computed(() => {
  const start = (currentPage.value - 1) * itemsPerPage.value;
  const end = start + itemsPerPage.value;
  return reviews.value.slice(start, end);
});

const goToPage = (page) => {
  if (page < 1 || page > totalPages.value) return;
  currentPage.value = page;
};

const isReported = (reviewId) => {
  return reports.value[reviewId] ?? false;
};

const toggleReport = async (reviewId) => {
  if (!user.value) {
    loginModalVisible.value = true;
    return;
  }
  if (isReported(reviewId)) return;

  try {
    const reportUrl = `/api/reports/${reviewId}/user/${user.value.id}`;
    await axios.post(reportUrl);

    reports.value[reviewId] = true;

    ElMessage({
      message: 'Review reported successfully',
      type: 'success',
      duration: 3000,
    });
  } catch (error) {
    console.error('Error reporting review:', error);
    ElMessage.error('An error occurred while reporting the review.');
  }
};

const likeReview = async (reviewId) => {
  if (!user.value) {
    loginModalVisible.value = true;
    return;
  }

  try {
    const response = await axios.post(`/api/review/${reviewId}/like`, null, {
      params: {
        userId: user.value.id,
        likeType: 'LIKE',
      },
    });

    await fetchReviews();
    ElMessage.success('Likes updated!');
  } catch (error) {
    console.error('Error liking review:', error);
    ElMessage.error('An error occurred while liking the review.');
  }
};

const dislikeReview = async (reviewId) => {
  if (!user.value) {
    loginModalVisible.value = true;
    return;
  }

  try {
    const response = await axios.post(`/api/review/${reviewId}/like`, null, {
      params: {
        userId: user.value.id,
        likeType: 'DISLIKE',
      },
    });

    await fetchReviews();
    ElMessage.success('Likes updated!');
  } catch (error) {
    console.error('Error disliking review:', error);
    ElMessage.error('An error occurred while disliking the review.');
  }
};

const closeModal = () => {
  loginModalVisible.value = false;
};
</script>

<style scoped>
.review-card {
  margin: 20px 0 -5px;
  box-shadow: none !important;
  border-radius: 2px;
  position: relative;
}

h3 {
  margin: 0;
  color: #333;
}

p {
  margin: 5px 0 10px;
}

.review-date {
  font-size: 0.85em;
  color: #888;
  margin-left: 10px;
}

.rating {
  font-size: 1.7em;
  margin-bottom: 5px;
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

.user-name {
  font-weight: bold;
  display: inline-block;
  margin-bottom: 15px;
  text-decoration: none;
  color: #333;
  transition: color 0.3s ease;
  margin-left: 5px;
}

.user-name:hover {
  color: #9b4dca;
}

.user-avatar {
  vertical-align: middle;
  margin-right: 5px;
}

.flag-icon {
  position: absolute;
  top: 10px;
  right: 10px;
  font-size: 1.5em;
  cursor: pointer;
  outline: none;
}

.flag-icon:disabled {
  outline: none;
  cursor: not-allowed;
}

.like-dislike-buttons {
  margin-top: 10px;
  display: flex;
  gap: 10px;
}

.like-dislike-buttons .el-button {
  width: 80px; 
}

.like-dislike-buttons .count {
  color: #5f5f5f;
  font-size: 1em;
  vertical-align: middle;
  margin-top: 2px;
}
</style>
