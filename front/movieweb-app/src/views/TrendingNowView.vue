<template>
    <div class="trending-now-page">
        <h1>Trending Now</h1>
        <h3>Top 10 Highest-Rated Movies right now!</h3>

        <a @click="toggleDescriptions" class="description-toggle-link">
            <Icon icon="mdi:information" style="font-size: 20px; margin-right: 8px;" />
            {{ showDescriptions ? 'Hide Descriptions' : 'Show Descriptions' }}
        </a>

        <div class="browse-all-movies">
            <router-link to="/movies">
                <el-link :underline="false" class="browse-link">
                    Browse all movies
                    <Icon icon="iconamoon:arrow-right-2-light" style="font-size: 20px;margin: 10px 0 8px" />
                </el-link>
            </router-link>
        </div>

        <Skeleton v-if="loading" :rows="10" :animated="true" />
        <div v-else-if="error">{{ error }}</div>
        <div v-else-if="movies.length === 0">
            <el-empty description="No trending movies available." />
        </div>

        <div v-else>
            <ul class="movie-list">
                <li v-for="(movie, index) in movies" :key="movie.id" class="movie-item">
                    <router-link :to="`/movie/${movie.id}`" class="movie-container">
                        <img :src="getImageUrl(movie.img)" :alt="movie.title" class="movie-image" />
                        <div class="movie-details">
                            <div class="movie-title-container">
                                <span class="movie-index">{{ index + 1 }}.</span>
                                <h2 class="movie-title">{{ movie.title }}</h2>
                            </div>
                            <h4 style="color: #888;font-size: 1em;margin: -10px 0 7px;">{{ movie.year }}</h4>
                            <p>
                                <MovieGenreTags :genres="movie.genres" />
                            </p>
                            <div v-show="showDescriptions" class="movie-description">
                                <p>{{ truncateText(movie.description, 320) }}</p>
                            </div>
                            <h1 class="rating">
                                <Icon icon="mdi:star" style="color: #9b4dca;" /> {{ movie.averageRating ?
                                    movie.averageRating.toFixed(2) : 'N/A' }}<span
                                    style="color: #888; font-size: 0.5em;">/10</span>
                                    <span class="rating-count">rated {{ movie.ratingCount }} times in total</span>
                            </h1>
                        </div>

                    </router-link>
                </li>
            </ul>
            <Pagination v-if="totalPages > 1" :totalElements="totalElements" :itemsPerPage="itemsPerPage"
                :currentPage="currentPage" @page-change="goToPage" />
        </div>
    </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import axios from 'axios';
import MovieGenreTags from '@/components/MovieGenreTags.vue';
import Pagination from '@/components/Pagination.vue';
import Skeleton from '@/components/Skeleton.vue';
import { Icon } from '@iconify/vue';

const movies = ref([]);
const loading = ref(true);
const error = ref(null);
const currentPage = ref(1);
const itemsPerPage = ref(10);
const totalPages = ref(0);
const totalElements = ref(0);
const showDescriptions = ref(false);

const toggleDescriptions = () => {
    showDescriptions.value = !showDescriptions.value;
};

const truncateText = (text, maxLength) => {
    if (!text) return '';
    return text.length > maxLength ? text.slice(0, maxLength) + '...' : text;
};

const getImageUrl = (imageName) => `/api/image/${imageName}`;

const fetchTrendingMovies = async () => {
    try {
        loading.value = true;
        const response = await axios.get('/api/movies', {
            params: {
                sort: 'averageRating,desc',
                size: itemsPerPage.value,
                page: currentPage.value - 1,
            },
        });
        if (response.data.content) {
            const ratedMovies = response.data.content.filter(movie => movie.averageRating > 0);
            movies.value = ratedMovies;
            totalElements.value = ratedMovies.length;
            totalPages.value = Math.ceil(totalElements.value / itemsPerPage.value);
        } else {
            error.value = 'No movies found.';
        }
        error.value = null;
    } catch (err) {
        error.value = 'There was an error loading trending movies.';
        console.error('Error fetching movies:', err);
    } finally {
        loading.value = false;
    }
};

const goToPage = (page) => {
    if (page < 1 || page > totalPages.value) return;
    currentPage.value = page;
    fetchTrendingMovies();
};

onMounted(() => {
    fetchTrendingMovies();
});
</script>

<style scoped>
h1 {
    font-size: 2em;
    color: #333;
}

h2 {
    font-size: 1.2em;
    color: #333;
}

h3 {
    color: #575757;
    margin-bottom: 60px;
}

h4 {
    color: #888;
    font-size: 1em;
    margin-top: -10px;
    margin-bottom: 30px;
}

.trending-now-page {
    padding: 20px;
    margin: 0 auto;
    max-width: 1250px;
    background-color: #fff;
    border-radius: 8px;
}

.movie-list {
    list-style-type: none;
    padding: 0;
}

.movie-item {
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
    border-radius: 8px;
    margin-bottom: 30px;
    max-height: 220px;
    overflow: hidden;
    transition: box-shadow 0.3s ease;
    background-color: #f4f4f4;
}

.movie-item:hover {
    box-shadow: 0 8px 16px rgba(0, 0, 0, 0.2);
}

.movie-container {
    display: flex;
    flex-direction: row;
    text-decoration: none;
    color: inherit;
    width: 100%;
}

.movie-image {
    width: 120px;
    height: 180px;
    object-fit: cover;
    border-radius: 8px;
    box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
    background-color: #333;
    margin: 20px 20px 20px 20px;
}

.movie-details h4 {
    margin: 0;
    font-size: 1.2em;
    color: #333;
}

.movie-details p {
    margin: 5px 0;
}

.movie-details span {
    font-weight: bold;
    color: #555;
}

.movie-title-container {
    display: flex;
    align-items: center;
}

.movie-index {
    font-size: 1.2em;
    font-weight: bold;
    color: #000;
    margin-right: 10px;
    transition: color 0.3s ease;
}

.movie-title {
    transition: color 0.3s ease;
    font-size: 1.3em;
}

.movie-container:hover .movie-title {
    color: #888;
}

.movie-container:hover .movie-index {
    color: #888;
}

.browse-all-movies {
    text-align: right;
    margin-bottom: 20px;
}

.browse-link {
    font-size: 1em;
    display: inline-flex;
    align-items: center;
    color: #333;
}

.description-toggle-link {
    display: inline-flex;
    align-items: center;
    font-size: 1em;
    color: #6e6e6e;
    cursor: pointer;
    transition: color 0.3s ease;
    text-decoration: none;
    margin-bottom: 20px;
}

.description-toggle-link:hover {
    text-decoration: none;
    color: #4a4a4a;
}

.movie-details {
    flex: 1;
    position: relative;
}

.rating {
    position: absolute;
    bottom: 10px;
    left: 0px;
    font-size: 1.6em;
}

.movie-description {
    margin-top: -25px;
    margin-bottom: -10px;
    font-size: small;
    margin-right: 20px;
    color: #333;
    z-index: 1;
}

.rating-count {
  font-size: 0.5em;
  color: #888 !important;
  font-weight: 400 !important;
  margin-left: 5px;
}
</style>