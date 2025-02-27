<template>
  <div class="profile-page">
    <h1>Your Profile</h1>

    <Skeleton v-if="loading" :rows="20" :animated="true" />
    <div v-else-if="error">{{ error }}</div>

    <EditUserDetails :user="user" />
    <UserFavoriteMovies />
    <UserWatchlist />
    <UserVotedPeople />

    <UserReviews :reviews="reviews" />
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue';
import axios from 'axios'; 
import { useAuthStore } from '../stores/auth.store';
import EditUserDetails from '@/components/EditUserDetails.vue';
import UserFavoriteMovies from '../components/UserFavoriteMovies.vue';
import UserWatchlist from '../components/UserWatchlist.vue';
import UserReviews from '@/components/UserReviews.vue'; 
import Skeleton from '@/components/Skeleton.vue';
import UserVotedPeople from '@/components/UserVotedPeople.vue';

const authStore = useAuthStore();
const user = computed(() => authStore.user);
const loading = ref(true);  
const error = ref(null);  
const reviews = ref([]);

onMounted(async () => {
  try {
    const response = await axios.get(`/api/user/${user.value.id}/reviews`);
    reviews.value = response.data;
  } catch (err) {
    error.value = 'Failed to load reviews';
  } finally {
    loading.value = false;
  }
});
</script>

<style scoped>
.profile-page {
  max-width: 950px;
  margin: 0 auto 14rem;
  padding: 2rem;
  background-color: #fff;
}

h1 {
  color: #303;
  margin: 0 0 1rem;
}

h2 {
  margin-top: 20px;
  font-size: 1.6em;
  color: #303;
}
</style>
