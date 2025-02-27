<template>
    <div class="profile-page">
        <div class="profile-header">
            <div class="profile-picture-section">
                <el-avatar :size="100" :src="user && user.img ? getImageUrl(user.img) : defaultAvatar" />
            </div>
            <div class="user-info">
                <h1 v-if="user">{{ user.name }}'s Profile</h1>
            </div>
        </div>

        <Skeleton v-if="loading" :rows="10" :animated="true" />
        <div v-else-if="error">{{ error }}</div>

        <div v-else>
            <PublicFavoriteMovies :userId="route.params.id"/>
            <UserVotedPeople :userId="route.params.id"/>
            <UserReviews :userId="route.params.id"/>
        </div>
    </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRoute } from 'vue-router';
import axios from 'axios';
import Skeleton from '@/components/Skeleton.vue';
import PublicFavoriteMovies from '@/components/PublicFavoriteMovies.vue';
import UserVotedPeople from '@/components/UserVotedPeople.vue';
import UserReviews from '@/components/UserReviews.vue';

const route = useRoute();
const user = ref(null);
const loading = ref(true);
const error = ref(null);

const getImageUrl = (imageName) => `/api/image/${imageName}`;
const defaultAvatar = 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQBsdZ-yU8qdxr0WrIA8HLa31tB_cCFZJt-2Q&s';

onMounted(async () => {
    try {
        const userId = route.params.id;
        const response = await axios.get(`/api/user/${userId}`);
        user.value = response.data;
    } catch (err) {
        error.value = 'Błąd ładowania użytkownika!';
    } finally {
        loading.value = false;
    }
});
</script>

<style scoped>
.profile-page {
    max-width: 950px;
    margin: 0 auto auto;
    padding: 2rem;
    background-color: #fff;
}

h1 {
    color: #303;
    margin: 0;
}

.profile-header {
    display: flex;
    align-items: center;
    margin-bottom: 1.5rem;
}

.profile-picture-section {
    margin-right: 1rem; 
}

.user-info {
    flex-grow: 1;
}
</style>