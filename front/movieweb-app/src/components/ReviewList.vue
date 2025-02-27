<template>
  <Skeleton v-if="loading" :rows="10" :animated="true" />
  <div v-else-if="error">{{ error }}</div>

  <el-table :data="filteredMovies" class="movie-table">
    <el-table-column type="expand">
      <template #default="{ row }">
        <div style="width: 1000px; margin-inline: auto">
          <h3>Movie Reviews</h3>
          <el-table :data="row.reviews">
            <el-table-column prop="id" label="ID" width="80" sortable></el-table-column>
            <el-table-column label="User" sortable>
              <template #default="{ row }">
                {{ row.userName }}
              </template>
            </el-table-column>
            <el-table-column label="Score" width="90" sortable>
              <template #default="{ row }">
    <span>
      <Icon icon="mdi:star" style="color: #9b4dca; vertical-align:middle; font-size: medium; margin: -3px 0 0 5px" />
    </span>
    {{ row.rating }}
  </template>
            </el-table-column>
            <el-table-column width="350">
              <template #default="{ row }">
                {{ row.content }}
              </template>
            </el-table-column>
            <el-table-column label="Created At" sortable>
              <template #default="{ row }">
                {{ formatDate(row.createdAt) }}
              </template>
            </el-table-column>
            <el-table-column width="200">
              <template #default="{ row }">
                <el-button 
                  type="danger" 
                  :icon="Delete" 
                  circle 
                  @click="handleDeleteReview(row.movieId, row.id)" 
                />
              </template>
            </el-table-column>
          </el-table>
        </div>
      </template>
    </el-table-column>

    <el-table-column prop="id" label="ID" width="70" sortable></el-table-column>
    <el-table-column width="90">
      <template #default="{ row }">
        <img :src="getImageUrl(row.img)" alt="Movie cover" class="thumbnail" />
      </template>
    </el-table-column>
    <el-table-column prop="title" label="Title" width="250" sortable></el-table-column>
    <el-table-column prop="year" label="Premiere" width="120" sortable></el-table-column>
    <el-table-column width="250">
      <template #default="{ row }">
        {{ row.genres.map(genre => genre.name).join(', ') || 'No genres' }}
      </template>
    </el-table-column>
    <el-table-column label="avg Score" sortable>
  <template #default="{ row }">
    <span>
      <Icon icon="mdi:star" style="color: #9b4dca; vertical-align:middle; font-size: medium; margin: -3px 0 0 30px" />
    </span>
    {{ row.averageRating ? row.averageRating.toFixed(2) : 'N/A' }}
  </template>
</el-table-column>
    <el-table-column width="200">
  <template #default="{ row }">
    <router-link :to="`/admin/manage-movies`">
      <el-button class="edit" :icon="ArrowRight" circle />
    </router-link>
  </template>
</el-table-column>

  </el-table>
</template>

<script setup>
import { ref, computed } from 'vue';
import { Edit, Delete } from '@element-plus/icons-vue';
import { ElNotification } from 'element-plus';
import Skeleton from './Skeleton.vue';
import { ArrowRight } from '@element-plus/icons-vue';
import { Icon } from '@iconify/vue';

const props = defineProps({
  movies: Array,
  loading: Boolean,
  error: String,
  filters: Object,
});

const emit = defineEmits(['delete-review']);

const getImageUrl = (imageName) => `/api/image/${imageName}`;

const formatDate = (timestamp) => {
  if (!timestamp) return '-';
  const date = new Date(timestamp);
  return `${date.getDate()}.${date.getMonth() + 1}.${date.getFullYear()} ${date.getHours()}:${date.getMinutes()}`;
};

const filteredMovies = computed(() => {
  const { title, minYear, maxYear, genres } = props.filters;

  return props.movies.filter((movie) => {
    const matchesTitle = title ? movie.title.includes(title) : true;
    const matchesYear = (minYear ? movie.year >= minYear : true) && (maxYear ? movie.year <= maxYear : true);
    const matchesGenres = genres.length ? movie.genres.some(genre => genres.includes(genre.id)) : true;

    return matchesTitle && matchesYear && matchesGenres;
  });
});

const handleDeleteReview = (movieId, reviewId) => {
  if (!movieId || !reviewId) {
    ElNotification.error({
      title: 'Error',
      message: 'Invalid movieId or reviewId',
    });
    return;
  }

  emit('delete-review', movieId, reviewId);
};
</script>
<style scoped>
.movie-table {
  margin-top: 35px;
  margin-bottom: 35px;
}

.actions {
  display: flex;
  align-items: center;
}

.actions .el-button {
  margin-right: 10px;
}

.thumbnail {
  width: 60px;
  height: 60px;
  object-fit: cover;
  border-radius: 1px;
  border: 1px solid #ddd;
}

.edit {
  background-color: var(--logo);
  border-color: var(--logo);
}

.edit:hover {
  background-color: var(--light-purple);
  border-color: var(--light-purple);
  color: #fff;
}
</style>
