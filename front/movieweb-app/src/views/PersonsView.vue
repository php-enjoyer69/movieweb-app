<template>
  <div class="person-view-page">
    <h1>List of Celebrities</h1>
    <h3>Here you can meet top people in the movie industry</h3>
    <div class="person-list">
      <!-- PersonFilter emits filters update -->
      <PersonFilter @updateFilters="updateFilters" :countries="countries"/>

      <Skeleton v-if="loading" :rows="10" :animated="true" />
      <div v-else-if="error">{{ error }}</div>

      <div v-else-if="persons && persons.length === 0">
        <el-empty description="No persons match your search criteria :(" />
      </div>

      <div v-else-if="persons">
        <div class="person-grid">
          <div class="person-item" v-for="person in persons" :key="person.id">
            <Person :person="person" />
          </div>
        </div>
      </div>

      <Pagination v-if="totalPages > 1" :current-page="currentPage" :total-elements="totalElements"
        :items-per-page="itemsPerPage" @page-change="goToPage" />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue';
import axios from 'axios';
import Person from '@/components/Person.vue';
import Skeleton from '@/components/Skeleton.vue';
import Pagination from '@/components/Pagination.vue';
import PersonFilter from '@/components/PersonFilter.vue';

const persons = ref([]);
const loading = ref(true);
const error = ref(null);
const currentPage = ref(1);
const itemsPerPage = ref(10);
const totalPages = ref(0);
const totalElements = ref(0);
const countries = ref([]);

const filters = ref({
  personName: '',
  country: null,
  sortByRating: null,
  sortByYear: null,
  genders: [],
});

const fetchData = async (page) => {
  try {
    loading.value = true;
    let results = [];

    const searchQueries = buildSearchQueries();

    const sortOrder =
  filters.value.sortByPrice === 'averageRatingAsc'
    ? 'averageRating,asc'
    : filters.value.sortByPrice === 'averageRatingDesc'
    ? 'averageRating,desc'
    : filters.value.sortByPrice === 'voteCountAsc'
    ? 'voteCount,asc'
    : filters.value.sortByPrice === 'voteCountDesc'
    ? 'voteCount,desc'
    : 'id,asc';

    if (searchQueries.length === 0) {
      const response = await axios.get('/api/persons', {
        params: {
          page: page - 1,
          size: itemsPerPage.value,
          sort: sortOrder,
        },
      });
      results = response.data.content;
      totalElements.value = response.data.totalElements;
    } else {
      for (const query of searchQueries) {
        const response = await axios.get('/api/persons', {
          params: {
            page: page - 1,
            size: itemsPerPage.value,
            search: query,
            sort: filters.value.sortByRating || 'id,asc',
          },
        });
        results = [...results, ...response.data.content];
        totalElements.value = response.data.totalElements;
      }
      results = Array.from(new Map(results.map((person) => [person.id, person])).values());
    }

    persons.value = results;
    totalPages.value = Math.ceil(totalElements.value / itemsPerPage.value);
    error.value = null;
  } catch (err) {
    console.error(err);
    error.value = 'There was an error loading the persons.';
  } finally {
    loading.value = false;
  }
};

const buildSearchQueries = () => {
  const queries = [];

  if (filters.value.personName) {
    queries.push(`name:*${filters.value.personName}*`);
    queries.push(`surname:*${filters.value.personName}*`);
  }

  if (filters.value.country) {
    queries.push(`countryOfOrigin:*${filters.value.country}*`);
  }

  if (filters.value.genders.length > 0) {
    queries.push(`gender:*${filters.value.genders}*`);
  }

  return queries;
};

const updateFilters = (newFilters) => {
  filters.value = newFilters;
  currentPage.value = 1;
  fetchData(currentPage.value);
};

const goToPage = (page) => {
  if (page < 1 || page > totalPages.value) return;
  currentPage.value = page;
  fetchData(currentPage.value);
};

watch(() => filters.value, () => {
  fetchData(currentPage.value);
}, { deep: true });

onMounted(() => {
  fetchData(currentPage.value);
});
</script>
<style scoped>
h1 {
  font-size: 2em;
  color: #333;
}

h3 {
  color: #575757;
  margin-bottom: 45px;
}

.person-view-page {
  padding: 20px;
  margin: 0 auto;
  max-width: 1250px;
  background-color: #fff;
  border-radius: 8px;
}

.person-grid {
  display: grid;
  grid-template-columns: repeat(5, 1fr);
  gap: 10px;
  padding: 0;
}

.person-item {
  box-sizing: border-box;
  margin: 0;
}

.person-item:first-child {
  margin-left: 0;
}

.person-item:last-child {
  margin-right: 0;
}
</style>