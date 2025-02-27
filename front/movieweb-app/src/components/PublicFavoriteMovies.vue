<template>
    <div class="saved-movies">
        <h2>Favorite Movies</h2>
        <p>{{ favoriteMovies.length }} favorite movies so far</p>
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
                    <span class="heart-icon disabled-heart" title="Favorites are private">
                        <Icon icon="mdi:heart" />
                    </span>
                </template>
            </el-table-column>
        </el-table>

        <el-empty v-else description="No favorite movies yet" />

        <Pagination :currentPage="currentPage" :totalElements="totalElements"
            :itemsPerPage="itemsPerPage" @page-change="goToPage" />
    </div>
</template>

<script setup>
import { ref, computed, onMounted, watch } from 'vue';
import axios from 'axios';
import Pagination from './Pagination.vue';
import { Icon } from '@iconify/vue';

const props = defineProps({
    userId: { type: Number, required: true },
    userName: { type: String, required: true }
});

const favoriteMovies = ref([]);
const currentPage = ref(1);
const itemsPerPage = ref(3);
const totalPages = ref(0);
const totalElements = ref(0);

const getImageUrl = (imageName) => `/api/image/${imageName}`;

const getFavoriteMovies = async () => {
    try {
        const response = await axios.get(`/api/movie/favorites/${props.userId}`);
        if (response.data && Array.isArray(response.data)) {
            totalElements.value = response.data.length;
            totalPages.value = Math.ceil(totalElements.value / itemsPerPage.value);
            const start = (currentPage.value - 1) * itemsPerPage.value;
            favoriteMovies.value = response.data.slice(start, start + itemsPerPage.value);
        } else {
            favoriteMovies.value = [];
        }
    } catch (error) {
        console.error("Error fetching favorite movies:", error);
        favoriteMovies.value = [];
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

watch(() => props.userId, () => {
    currentPage.value = 1;
    getFavoriteMovies();
});
</script>

<style scoped>
h1 {
    color: #4b0082;
    margin: 0 0 1rem;
}

p {
  font-size: smaller;
  color: #6e6e6e;
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
    transition: color 0.3s ease;
}

.disabled-heart {
    color: #9b4dca;
    cursor: disabled;
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