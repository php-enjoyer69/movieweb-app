<template>
    <h2>Movie Reviews</h2>

    <div v-if="loading">Loading...</div>
    <div v-else-if="error">{{ error }}</div>

    <div v-else-if="reviews.length === 0">
        <el-empty description="No reviews yet" />
    </div>

    <el-card v-for="review in reviews" :key="review.id" class="review-card">
        <el-row>
            <el-col :span="24">
                <h3 class="user-name">{{ user.name }}</h3>
                <span class="review-date"> at {{ formatDate(review.createdAt) }}</span>
            </el-col>
        </el-row>
        <el-row>
            <p class="rating"><Icon icon="mdi:star" style="color: #9b4dca;"/> {{ review.rating }}<span style="color: #888; font-size: 0.5em;">/10</span></p>
        </el-row>
        <el-row>
            <el-col :span="24">
                <p>{{ review.content }}</p>
            </el-col>
        </el-row>
    </el-card>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import axios from 'axios';
import { useAuthStore } from '@/stores/auth.store';
import { storeToRefs } from 'pinia';
import { Icon } from '@iconify/vue';

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

const formatDate = (timestamp) => {
  if (!timestamp) return '-';
  const date = new Date(timestamp);

  const padZero = (num) => (num < 10 ? `0${num}` : num);

  const year = date.getFullYear();
  const month = padZero(date.getMonth() + 1);
  const day = padZero(date.getDate());
  const hours = padZero(date.getHours());
  const minutes = padZero(date.getMinutes());
  const seconds = padZero(date.getSeconds());

  return `${hours}:${minutes}:${seconds} ${day}.${month}.${year}`;
};

const fetchReviews = async () => {
    try {
        const response = await axios.get(`/api/movie/${props.movieId}/reviews`);
        console.log(response.data);
        reviews.value = response.data;
    } catch (err) {
        error.value = 'Failed to fetch reviews. Please try again later.';
        console.error(err);
    } finally {
        loading.value = false;
    }
};

onMounted(() => {
    fetchReviews();
});
</script>

<style scoped>
.movie-reviews {
    font-family: Arial, sans-serif;
    padding: 20px;
}

.review-card {
    margin-bottom: 20px;
}

h3 {
    margin: 0;
}

p {
    margin: 5px 0 0;
}

.user-name {
    font-weight: bold;
    display: inline-block;
    margin-bottom: 15px;
}

.review-date {
    font-size: 0.85em;
    color: #888;
    margin-left: 10px; 
    vertical-align: middle;
}

.rating {
    font-size: 1.7em;
    margin-bottom: 3px;
}
</style>
