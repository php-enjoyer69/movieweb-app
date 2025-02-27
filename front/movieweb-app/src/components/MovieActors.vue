<template>
  <div>
    <div v-if="actors.length > 0" class="movie-roles">
      <h3>Cast</h3>
      <div class="actor-list">
        <div 
          v-for="role in paginatedActors" 
          :key="role.id" 
          class="actor-tile" 
          @click="redirectToPerson(role.person.id)"
        >
          <img :src="getImageUrl(role.person.img)" alt="Actor Image" class="actor-img" />
          <div class="actor-info">
            <p class="actor-name">{{ role.person.name }} {{ role.person.surname }}</p>
            <p class="actor-role">as {{ role.name }}</p>
          </div>
        </div>
      </div>
    </div>

    <Pagination style="margin-top: -5px;"
      v-if="totalPages > 1" 
      :current-page="currentPage" 
      :total-elements="actors.length" 
      :items-per-page="itemsPerPage" 
      @page-change="goToPage" 
    />
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue';
import axios from 'axios';
import { useRouter } from 'vue-router';
import Pagination from '@/components/Pagination.vue';

const props = defineProps({
  movieId: Number,
});

const actors = ref([]);
const roles = ref([]);
const selectedRoles = ref([]);
const currentPage = ref(1);
const itemsPerPage = ref(6); 
const totalPages = ref(0);
const router = useRouter();

const fetchRoles = async (movieId) => {
  try {
    const response = await axios.get(`/api/movie/${movieId}/roles`);
    const rolesData = response.data;

    actors.value = rolesData.filter(role => role.role === 'actor');
    totalPages.value = Math.ceil(actors.value.length / itemsPerPage.value);
  } catch (error) {
    console.error('Error fetching roles:', error);
  }
};

onMounted(() => {
  fetchRoles(props.movieId);
});

const getImageUrl = (imageName) => `/api/image/${imageName}`;

const redirectToPerson = (personId) => {
  router.push(`/person/${personId}`);
};

const filteredActors = computed(() => {
  if (selectedRoles.value.length === 0) {
    return actors.value;
  }
  return actors.value.filter(role => 
    selectedRoles.value.includes(role.role)
  );
});

const paginatedActors = computed(() => {
  const start = (currentPage.value - 1) * itemsPerPage.value;
  const end = start + itemsPerPage.value;
  return filteredActors.value.slice(start, end);
});

const goToPage = (page) => {
  if (page < 1 || page > totalPages.value) return;
  currentPage.value = page;
};
</script>

<style scoped>
.movie-roles {
  margin-top: 50px;
  min-height: 400px;
}

.actor-list {
  display: flex;
  flex-wrap: wrap;
  gap: 15px;
  margin-top: 10px;
  justify-content: space-between;
}

.actor-tile {
  display: flex;
  align-items: center;
  width: 49%;
  background-color: #f4f4f4;
  border-radius: 8px;
  padding: 15px;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
  box-sizing: border-box;
  cursor: pointer;
  transition: transform 0.3s ease, box-shadow 0.3s ease;
  height: 100px;
  min-height: 100px; 
}

.actor-tile:hover {
  transform: scale(1.05);
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.15);
}

.actor-img {
  width: 80px;
  height: 80px;
  object-fit: cover;
  border-radius: 50%;
  margin-right: 15px;
}

.actor-info {
  text-align: left;
}

.actor-name {
  font-size: 1.1em;
  font-weight: bold;
  color: #333;
  margin: 0;
}

.actor-role {
  font-size: 0.9em;
  color: #777;
  margin: 0;
}

h3 {
  color: #333;
}

.role-filters {
  margin-bottom: 20px;
}
</style>
