<template>
    <div class="watchlist-movies">
        <h2>Movies to Watch</h2>
        <el-table v-if="watchlistMovies.length" :data="watchlistMovies" style="width: 100%">
            <el-table-column>
                <template #default="{ row }">
                    <router-link :to="`/movie/${row.movie.id}`">
                    <div class="thumbnail-container">
                        <img :src="getImageUrl(row.movie.img)" alt="Movie cover" class="thumbnail" />
                    </div>
                </router-link>
                </template>
            </el-table-column>

            <el-table-column width="550">
                <template #default="{ row }">
                    <router-link :to="`/movie/${row.movie.id}`" class="movie-title">
                        {{ row.movie.title }}
                    </router-link>
                </template>
            </el-table-column>

            <el-table-column width="120">
                <template #default="{ row }">
                    <span class="check-icon" :title="row.isInWatchlist ? 'Remove from watchlist' : 'Add to watchlist'"
                        @click="toggleWatchlist(row)">
                        <Icon :icon="row.isInWatchlist ? 'mdi:check' : 'mdi:plus'" />
                    </span>
                </template>
            </el-table-column>
        </el-table>

        <el-empty v-else description="No movies in watchlist yet" />

        <Pagination v-if="totalPages > 1" :currentPage="currentPage" :totalElements="totalElements"
            :itemsPerPage="itemsPerPage" @page-change="goToPage" />
    </div>
</template>

<script setup>
import { ref, computed, onMounted, watch } from 'vue';
import { useAuthStore } from '../stores/auth.store';
import axios from 'axios';
import Pagination from './Pagination.vue';
import { ElMessage } from 'element-plus';
import { Icon } from '@iconify/vue';

const watchlistMovies = ref([]);
const authStore = useAuthStore();
const user = computed(() => authStore.user);
const currentPage = ref(1);
const itemsPerPage = ref(3);
const totalPages = ref(0);
const totalElements = ref(0);

const getImageUrl = (imageName) => `/api/image/${imageName}`;

const getWatchlistMovies = async () => {
    if (!user.value || !user.value.id) return;
    try {
        const response = await axios.get(`/api/movie/watchlist/${user.value.id}`);
        if (response.data && Array.isArray(response.data)) {
            totalElements.value = response.data.length;
            totalPages.value = Math.ceil(totalElements.value / itemsPerPage.value);
            const start = (currentPage.value - 1) * itemsPerPage.value;
            watchlistMovies.value = response.data.slice(start, start + itemsPerPage.value);

            watchlistMovies.value.forEach(movie => {
                movie.isInWatchlist = true;
            });
        } else {
            watchlistMovies.value = [];
        }
    } catch (error) {
        console.error("Error fetching watchlist movies:", error);
        watchlistMovies.value = [];
    }
};

const toggleWatchlist = async (row) => {
    if (!user.value) return showLoginModal();

    try {
        const url = `/api/movie/watchlist/${row.movie.id}`;
        const method = row.isInWatchlist ? 'delete' : 'post';

        await axios({
            method,
            url,
            params: { userId: user.value.id }
        });

        row.isInWatchlist = !row.isInWatchlist;

        ElMessage({
            message: row.isInWatchlist ? 'Movie added to watchlist!' : 'Movie removed from watchlist.',
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

const goToPage = (page) => {
    if (page < 1 || page > totalPages.value) return;
    currentPage.value = page;
    getWatchlistMovies();
};

onMounted(() => {
    getWatchlistMovies();
});

watch(user, () => {
    currentPage.value = 1;
    getWatchlistMovies();
});

</script>

<style scoped>
h1 {
    color: #4b0082;
    margin: 0 0 1rem;
}

.watchlist-movies {
    margin-top: 1rem;
}

.watchlist-movies h2 {
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

.check-icon {
    display: inline-flex;
    align-items: center;
    justify-content: center;
    font-size: 1.5em;
    color: #9b4dca;
    cursor: pointer;
    transition: color 0.3s ease;
}

.check-icon:hover {
    color: #f02ac4;
}

.check-icon:active {
    color: #f02ac4;
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