<template>
  <div>
    <div v-if="roles.length > 0" class="movie-roles">
      <h3>Filmography</h3>
      <div class="role-filter">
        <el-radio-group v-model="selectedRole" class="role-radio-group">
          <el-radio label="">all roles</el-radio>
          <el-radio label="director">director</el-radio>
          <el-radio label="producer">producer</el-radio>
          <el-radio label="actor">actor</el-radio>
          <el-radio label="screenwriter">screenwriter</el-radio>
        </el-radio-group>
      </div>
      <div class="role-list">
        <div v-for="role in paginatedRoles" :key="role.movie.id" class="role-tile"
          @click="redirectToMovie(role.movie.id)">
          <img :src="getImageUrl(role.movie.img)" alt="Movie Image" class="movie-img" />
          <div class="role-info">
            <p class="movie-title">{{ role.movie.title }}</p>
            <p class="movie-year">{{ role.movie.year }}</p>
            <p class="movie-rating">
              <Icon icon="mdi:star" style="color: #9b4dca; font-size: large;" />
              {{ role.movie && role.movie.averageRating ? role.movie.averageRating.toFixed(2) : 'N/A' }}
            </p>
            <p class="role">
              <span v-if="role.role === 'actor'">
                as {{ role.name }}
              </span>
              <span v-else>
                {{ role.role }}
              </span>
            </p>

          </div>
        </div>
      </div>
    </div>

    <div :style="paginationMargin">
      <Pagination v-if="totalPages > 1" :current-page="currentPage" :total-elements="filteredRoles.length"
        :items-per-page="itemsPerPage" @page-change="goToPage" />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue';
import axios from 'axios';
import { useRouter } from 'vue-router';
import { Icon } from '@iconify/vue';
import Pagination from '@/components/Pagination.vue';
import { ElRadio, ElRadioGroup } from 'element-plus';

const props = defineProps({
  personId: Number,
});

const roles = ref([]);
const currentPage = ref(1);
const itemsPerPage = ref(6);
const selectedRole = ref('');
const router = useRouter();

const buildSearchQuery = () => {
  let query = '';
  if (selectedRole.value) {
    query = `roles;*${selectedRole.value.toLowerCase()}*`;
  }
  return query;
};

const fetchRoles = async (personId) => {
  try {
    const searchQuery = buildSearchQuery();
    const response = await axios.get(`/api/person/${personId}/roles?search=${searchQuery}`);
    roles.value = response.data;
  } catch (error) {
    console.error('Error fetching roles:', error);
  }
};

onMounted(() => {
  fetchRoles(props.personId);
});

const getImageUrl = (imageName) => `/api/image/${imageName}`;

const redirectToMovie = (movieId) => {
  router.push(`/movie/${movieId}`);
};

const filteredRoles = computed(() => {
  if (!selectedRole.value) return roles.value;
  return roles.value.filter(role => role.role === selectedRole.value);
});

const paginatedRoles = computed(() => {
  const start = (currentPage.value - 1) * itemsPerPage.value;
  const end = start + itemsPerPage.value;
  return filteredRoles.value.slice(start, end);
});

const totalPages = computed(() => Math.ceil(filteredRoles.value.length / itemsPerPage.value));

const goToPage = (page) => {
  if (page < 1 || page > totalPages.value) return;
  currentPage.value = page;
};

const paginationMargin = computed(() => {
  return totalPages.value > 1 ? 'margin-top: 0px;' : 'margin-top: 190px;';
});
</script>
<style scoped>
.movie-roles {
  margin-top: 50px;
  min-height: 400px;
}

.role-filter {
  display: flex;
  gap: 20px;
  margin-bottom: 20px;
}

.role-radio-group {
  display: flex;
  gap: 20px;
}

.role-list {
  display: flex;
  flex-wrap: wrap;
  gap: 15px;
  margin-top: 10px;
  justify-content: space-between;
}

.role-tile {
  display: flex;
  align-items: center;
  width: 100%;
  max-width: 620px;
  background-color: #f4f4f4;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
  box-sizing: border-box;
  cursor: pointer;
  transition: transform 0.3s ease, box-shadow 0.3s ease;
  height: 150px;
  min-height: 150px;
}

.role-tile:hover {
  transform: scale(1.05);
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.15);
}

.movie-img {
  width: 90px;
  height: 140px;
  object-fit: cover;
  border-radius: 8px;
  margin-right: 15px;
}

.role-info {
  text-align: left;
}

.movie-title {
  font-size: 1.1em;
  font-weight: bold;
  color: #333;
  margin: 0;
}

.movie-year {
  font-size: 1em;
  color: #777;
  margin: 5px 0;
}

.movie-rating {
  font-size: 0.9em;
  color: #777;
  margin: 5px 0;
  display: flex;
  align-items: center;
}

.movie-rating span {
  color: #888;
  margin-left: 5px;
}

.role {
  font-size: 0.9em;
  color: #777;
  margin: 0;
}

h3 {
  color: #333;
}
</style>
