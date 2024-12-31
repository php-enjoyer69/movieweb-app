<template>
  <div>
    <AdminMovieFilter @updateFilters="updateFilters" :genres="genres"/>

    <div v-if="loading">Loading...</div>
      <div v-else-if="error">{{ error }}</div>

      <div v-else-if="movies.length === 0">
        <el-empty description="No movies match your search criteria :(" />
      </div>

    <div v-else style="width: 1150px">
      <el-table :data="movies" class="movie-table" @sort-change="handleSortChange">
        <el-table-column prop="id" label="ID" width="80" sortable></el-table-column>
        <el-table-column label="Cover" width="120">
          <template #default="{ row }">
            <img :src="getImageUrl(row.img)" alt="Movie cover" class="thumbnail" />
          </template>
        </el-table-column>
        <el-table-column prop="title" label="Title" sortable></el-table-column>
        <el-table-column prop="director" label="Director" sortable> TBU</el-table-column>
        <el-table-column prop="year" label="Premiere" sortable></el-table-column>
        <el-table-column label="Genres">
          <template #default="{ row }">
            {{ row.genres.map(genre => genre.name).join(', ') || 'No genres' }}
          </template>
        </el-table-column>
        <el-table-column label="Actions" width="200">
          <template #default="{ row }">
            <div class="actions">
              <el-button class="edit" :icon="Edit" circle @click="openEditDialog(row.id)"></el-button>
              <el-button type="danger" :icon="Delete" circle @click="deleteMovie(row.id)" style="margin-left: 1px;">
              </el-button>
            </div>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <Pagination v-if="totalPages > 1" :totalElements="totalElements" :itemsPerPage="itemsPerPage" :currentPage="currentPage"
      @page-change="goToPage" />

    <div v-if="confirmDeleteModalVisible" class="modal-overlay" @click="resetDialog">
      <div class="modal-content" @click.stop>
        <h3>Confirm deletion</h3>
        <p>Do you wish to delete movie {{ movieIdToDelete }}?</p>
        <div class="modal-footer">
          <el-button type="danger" :icon="Delete" @click="confirmDeleteMovie">Delete</el-button>
          <el-button type="info" @click="resetDialog">Cancel</el-button>
        </div>
      </div>
    </div>

    <div class="modal-content" v-if="editDialogVisible" title="Edit movie">
      <EditMovie :movieId="movieToEdit" @close="editDialogVisible = false" @refresh="fetchData(currentPage)" />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import axios from 'axios';
import Pagination from '@/components/Pagination.vue';
import { ElNotification } from 'element-plus';
import { Edit, Delete } from '@element-plus/icons-vue';
import AdminMovieFilter from './AdminMovieFilter.vue';
import EditMovie from './EditMovie.vue';

const editDialogVisible = ref(false);
const movieToEdit = ref(null);
const confirmDeleteModalVisible = ref(false);
const movieIdToDelete = ref(null);

const movies = ref([]);
const loading = ref(true);
const error = ref(null);
const currentPage = ref(1);
const itemsPerPage = ref(15);
const totalPages = ref(0);
const totalElements = ref(0);
const genres = ref([]);

const filters = ref({
  title: '',
  minYear: null,
  maxYear: null,
  sortByRating: null,
  sortByYear: null,
  genres: [],
});

const getImageUrl = (imageName) => `/api/image/${imageName}`;

const buildSearchQuery = () => {
  const queries = [];
  if (filters.value.title) {
    queries.push(`title:*${filters.value.title}*`);
  }
  if (filters.value.minYear !== null) {
    queries.push(`year>${filters.value.minYear}`);
  }
  if (filters.value.maxYear !== null) {
    queries.push(`year<${filters.value.maxYear}`);
  }
  if (filters.value.genres.length > 0) {
    const genreQueries = filters.value.genres.map(genre => `genres;${genre}`);
    queries.push(`(${genreQueries.join(',')})`);
  }

  return queries.join(',');
};

const currentSort = ref({ field: 'id', order: 'ascending' });

const handleSortChange = ({ prop, order }) => {
  currentSort.value = { field: prop, order };
  fetchData(currentPage.value);
};

const fetchGenres = async () => {
  try {
    const response = await axios.get('/api/movie-genres', {
      params: { size: 9999 },
    });
    genres.value = response.data.content || [];
  } catch (error) {
    ElNotification.error({
      title: 'Error',
      message: 'There was an error loading the movie genres.',
    });
    genres.value = [];
  }
};

const fetchData = async (page) => {
  try {
    loading.value = true;
    const searchQuery = buildSearchQuery();

    const sortOrder = currentSort.value.order === 'ascending' ? `${currentSort.value.field},asc` : `${currentSort.value.field},desc`;

    const response = await axios.get('/api/movies', {
      params: {
        page: page - 1,
        size: itemsPerPage.value,
        sort: sortOrder,
        search: searchQuery,
      },
    });

    movies.value = response.data.content;
    totalPages.value = Math.ceil(response.data.totalElements / itemsPerPage.value);
    totalElements.value = response.data.totalElements;
    error.value = null;
  } catch (err) {
    error.value = 'There was an error loading the movies.';
  } finally {
    loading.value = false;
  }
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

const openEditDialog = (id) => {
  movieToEdit.value = id;
  editDialogVisible.value = true;
};

const deleteMovie = (movieId) => {
  movieIdToDelete.value = movieId;
  confirmDeleteModalVisible.value = true;
};

const confirmDeleteMovie = async () => {
  try {
    await axios.delete(`/api/movie/${movieIdToDelete.value}`);

    movies.value = movies.value.filter(movie => movie.id !== movieIdToDelete.value);
    totalElements.value -= 1;

    ElNotification.success({
      title: 'Success',
      message: 'Movie deleted succesfully',
    });

    resetDialog();
  } catch (error) {
    ElNotification.error({
      title: 'Error',
      message: 'Could not delete the movie. Try again later',
    });
  }
};

const resetDialog = () => {
  confirmDeleteModalVisible.value = false;
  movieIdToDelete.value = null;
};

onMounted(() => {
  fetchGenres();
  fetchData(currentPage.value);
});

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

.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 9999;
}

.modal-content {
  background-color: white;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  width: 500px;
}

.modal-footer {
  margin-top: 20px;
  text-align: right;
}

.modal-footer el-button {
  margin-left: 10px;
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