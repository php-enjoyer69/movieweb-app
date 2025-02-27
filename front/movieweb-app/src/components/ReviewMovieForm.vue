<template>
  <div class="rating-form">
    <h1 class="title">Rate this movie</h1>

    <h3 class="subtitle">Your score</h3>
    <div class="rating-input">
      <el-input-number v-model="rating" :min="1" :max="10" class="input-number" />
      <el-rate v-model="rating" :max="10" show-text disabled size="large" :colors="colors" :texts="texts" />
    </div>

    <p class="review-label">Leave a review (optional)</p>
    <el-input type="textarea" v-model="content" placeholder="Write your review here..." rows="4" />

    <div class="checkbox-group">
      <el-checkbox v-model="wantsToWatchAgain" label="Will watch again" />
    </div>

    <div class="modal-footer">
      <el-link :underline="false" @click="submitReview" class="submit-button">
        <Icon icon="mdi:check" width="24" height="24" class="icon" />
        Submit
      </el-link>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import axios from 'axios';
import { useAuthStore } from '@/stores/auth.store';
import { ElMessage } from 'element-plus';
import { storeToRefs } from 'pinia';
import { Icon } from '@iconify/vue';

const props = defineProps({
  movieId: Number,
});

const rating = ref(0);
const content = ref('');
const wantsToWatchAgain = ref(false);
const authStore = useAuthStore();
const { user } = storeToRefs(useAuthStore());

const colors = ref([
  '#ff81d7', '#f95dd1', '#f02ac4', '#e500b7', '#d300a9', '#c1009c', '#b1008f', '#a20082', '#9b0075', '#9b4dca'
]);

const texts = ref([
  'terrible', 'disappointing', 'bad', 'below average', 'average', 'ok', 'good', 'very good', 'excellent', 'masterpiece'
]);

const submitReview = async () => {
  if (rating.value === 0) {
    ElMessage.error('Please provide a rating!');
    return;
  }

  try {
    const response = await axios.post(
      `http://127.0.0.1:8000/movie/${props.movieId}/review/${user.value.id}`,
      {
        rating: rating.value,
        content: content.value,
        wantsToWatchAgain: wantsToWatchAgain.value ? true : null, 
      },
      {
        headers: {
          Authorization: `Bearer ${authStore.token}`,
        },
      }
    );

    if (response.status === 200) {
      ElMessage({
        message: 'You have successfully rated the movie',
        type: 'success',
        plain: true,
        duration: 1500,
      });
      setTimeout(() => {
        window.location.reload();
      }, 700);
    } else {
      alert('Failed to submit review');
    }
  } catch (error) {
    if (error.response && error.response.status === 409) {
      ElMessage({
        message: 'You have already rated this movie',
        type: 'error',
        plain: true,
        duration: 1500,
      });
    } else {
      console.error('Error submitting review:', error);
      alert('An error occurred. Please try again.');
    }
  }
};
</script>

<style scoped>
.rating-form {
  margin-top: 20px;
  background-color: #f4f4f4;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
}

.title {
  color: #333;
  margin-bottom: 35px;
}

.subtitle {
  color: #333;
}

.rating-input {
  display: flex;
  align-items: center;
}

.input-number {
  margin-right: 10px;
}

.review-label {
  color: #333;
  margin: 30px 0 10px;
}

.checkbox-group {
  margin-top: 20px;
}

.modal-footer {
  margin-top: 20px;
  margin-right: 10px;
  text-align: right;
}

.submit-button {
  font-weight: 400;
  font-size: large;
  margin: 30px 0 10px;
}

.icon {
  margin-right: 10px;
}
</style>
