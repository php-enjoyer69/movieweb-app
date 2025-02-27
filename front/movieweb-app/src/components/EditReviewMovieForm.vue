<template>
  <div class="rating-form">
    <h1 class="title">Edit your review</h1>
    <h3 class="subtitle">Your score</h3>
    <div class="rating-input">
      <el-input-number v-model="form.rating" :min="1" :max="10" class="input-number" />
      <el-rate v-model="form.rating" :max="10" show-text disabled size="large" :colors="colors" :texts="texts" />
    </div>

    <p class="review-label">Leave a review (optional)</p>
    <el-input type="textarea" v-model="form.content" placeholder="Edit your review" rows="4" />

    <div class="checkbox-group">
      <el-checkbox v-model="form.wantsToWatchAgain" label="Want to watch again" />
    </div>

    <div class="modal-footer">
      <el-link :underline="false" @click="updateReview" class="submit-button">
        <Icon icon="mdi:check" width="24" height="24" class="icon" />
        Update
      </el-link>
    </div>
  </div>
</template>

<script setup>
import { reactive, watch, ref } from 'vue';
import axios from 'axios';
import { ElMessage } from 'element-plus';
import { Icon } from '@iconify/vue';

const props = defineProps({
  movieId: Number,
  reviewId: Number,
  review: Object,
});

const form = reactive({
  rating: 0,
  content: '',
  wantsToWatchAgain: false,
  createdAt: '',
});

watch(() => props.review, (newReview) => {
  if (newReview) {
    form.rating = newReview.rating !== undefined ? newReview.rating : 0;
    form.content = newReview.content || '';

    form.wantsToWatchAgain = newReview.wantsToWatchAgain !== undefined ? Boolean(newReview.wantsToWatchAgain) : null;

    form.createdAt = newReview.createdAt || '';
  }
}, { immediate: true });

const updateReview = async () => {
  if (!props.reviewId) {
    console.error("No review ID provided.");
    return;
  }

  try {
    const response = await axios.patch(`/api/movie/${props.movieId}/review/${props.reviewId}`, {
      rating: form.rating,
      content: form.content,
      wantsToWatchAgain: form.wantsToWatchAgain,
      createdAt: new Date().toISOString(),
    });

    if (response.data.createdAt) {
      form.createdAt = response.data.createdAt;
    } else {
      form.createdAt = new Date().toISOString(); 
    }

    ElMessage.success('Review updated successfully!');
    setTimeout(() => {
      window.location.reload();
    }, 700);
  } catch (error) {
    console.error('Error updating review:', error);
    ElMessage.error('Error updating review. Please try again.');
  }
};


const colors = ref([ 
  '#ff81d7', '#f95dd1', '#f02ac4', '#e500b7', '#d300a9', '#c1009c', 
  '#b1008f', '#a20082', '#9b0075', '#9b4dca',
]);

const texts = ref([
  'terrible', 'disappointing', 'bad', 'below average', 'average', 'ok',
  'good', 'very good', 'excellent', 'masterpiece',
]);
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