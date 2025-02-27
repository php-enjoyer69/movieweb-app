<template>
    <div class="top-celebrities-page">
        <h1>Top Celebrities</h1>
        <h3>Hot 10 of currently best voted people in the movie industry!</h3>

        <a @click="toggleDescriptions" class="description-toggle-link">
            <Icon icon="mdi:information" style="font-size: 20px; margin-right: 8px;" />
            {{ showDescriptions ? 'Hide Descriptions' : 'Show Descriptions' }}
        </a>

        <div class="browse-all-people">
            <router-link to="/people">
                <el-link :underline="false" class="browse-link">
                    Browse all people
                    <Icon icon="iconamoon:arrow-right-2-light" style="font-size: 20px;margin: 10px 0 8px" />
                </el-link>
            </router-link>
        </div>

        <Skeleton v-if="loading" :rows="10" :animated="true" />
        <div v-else-if="error">{{ error }}</div>
        <div v-else-if="persons.length === 0">
            <el-empty description="No trending celebrities available." />
        </div>

        <div v-else>
            <el-row :gutter="20">
                <el-col v-for="(person, index) in visiblePersons" :key="person.id" :span="8">
                    <el-card shadow="hover" class="person-card">
                        <router-link :to="`/person/${person.id}`" class="person-container">
                            <img :src="getImageUrl(person.img)" :alt="person.name" class="person-image" />
                            <div class="person-details">
                                <h2>{{ index + 1 }}. {{ person.name }} {{ person.surname }}</h2>
                                <h4 style="color: #888;">{{ person.movies?.join(', ') || 'No movies available' }}</h4>
                                <div v-show="showDescriptions" class="person-biography">
                                    <p>{{ person.biography }}</p>
                                </div>
                                <div class="rating">
                                    <p class="average-rating" 
                                       :class="{ 'positive-rating': person.averageRating > 0, 'negative-rating': person.averageRating < 0 }">
                                        {{ person.averageRating > 0 ? '+' : '' }}{{ person.averageRating }}
                                    </p>
                                    <p style="font-size: smaller;color: #888;"><strong>{{ person.voteCount }}</strong> votes total</p>
                                </div>
                            </div>
                        </router-link>
                    </el-card>
                </el-col>
            </el-row>
        </div>

        <div v-if="visiblePersons.length < persons.length" class="load-more" @click="loadMore">
            <Icon icon="mdi:chevron-down" style="font-size: 32px; cursor: pointer; color: #888;" />
        </div>
    </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import axios from 'axios';
import Skeleton from '@/components/Skeleton.vue';
import { Icon } from '@iconify/vue';

const persons = ref([]);
const visiblePersons = ref([]);
const loading = ref(true);
const error = ref(null);
const showDescriptions = ref(false);
const itemsToShow = ref(3);

const toggleDescriptions = () => {
    showDescriptions.value = !showDescriptions.value;
};

const getImageUrl = (imageName) => `/api/image/${imageName}`;

const fetchMoviesForPerson = async (personId) => {
    try {
        const response = await axios.get(`/api/person/${personId}/roles`);
        return response.data.slice(0, 2).map(role => role.movie.title); 
    } catch (err) {
        console.error(`Error fetching movies for person ${personId}:`, err);
        return [];
    }
};

const fetchTrendingPersons = async () => {
    try {
        loading.value = true;
        const response = await axios.get('/api/persons', {
            params: {
                sort: 'averageRating,desc',
                size: 10,
                page: 0,
            },
        });
        if (response.data.content) {
            const ratedPersons = response.data.content.filter(person => person.voteCount > 0);
            for (let person of ratedPersons) {
                person.movies = await fetchMoviesForPerson(person.id);
            }
            persons.value = ratedPersons;
            visiblePersons.value = persons.value.slice(0, itemsToShow.value);
        } else {
            error.value = 'No celebrities found.';
        }
        error.value = null;
    } catch (err) {
        error.value = 'There was an error loading trending celebrities.';
        console.error('Error fetching persons:', err);
    } finally {
        loading.value = false;
    }
};

const loadMore = () => {
    itemsToShow.value += 3;
    visiblePersons.value = persons.value.slice(0, itemsToShow.value);
};

onMounted(() => {
    fetchTrendingPersons();
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
    margin-bottom: 10px;
}

.top-celebrities-page {
    padding: 20px;
    margin: 0 auto;
    max-width: 1250px;
    background-color: #fff;
    border-radius: 8px;
}

.person-card {
    margin-bottom: 20px;
    transition: transform 0.3s ease;
    background-color: #f9f9f9;
}

.person-card:hover {
    transform: scale(1.05);
}

.person-container {
    display: flex;
    flex-direction: column;
    text-decoration: none;
    color: inherit;
    align-items: center;
    text-align: center;
}

.person-image {
    width: 120px;
    height: 120px;
    object-fit: cover;
    border-radius: 50%;
    box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
    background-color: #333;
    margin-bottom: 10px;
}

.person-details h4 {
    margin: 0;
    font-size: 1.1em;
}

.person-biography {
    margin-top: 10px;
    font-size: small;
    color: #333;
}

.rating {
    margin-top: 20px;
    text-align: center;
}

.average-rating {
    font-size: 32px;
    font-weight: bold;
}

.positive-rating {
    color: rgb(49, 155, 49);
}

.negative-rating {
    color: rgb(255, 67, 67);
}

.browse-all-people {
    text-align: right;
    margin-bottom: 20px;
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

.load-more {
    text-align: center;
    margin-top: 20px;
    cursor: pointer;
}
</style>

