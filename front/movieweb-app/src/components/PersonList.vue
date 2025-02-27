<template>
  <div>
    <Skeleton v-if="loading" :rows="20" :animated="true" />
    <div v-else-if="error">{{ error }}</div>

    <div v-else-if="people.length === 0">
      <el-empty description="No people match your search criteria :(" />
    </div>

    <div v-else style="width: 1150px">
      <el-table :data="people" class="person-table" @sort-change="handleSortChange">
        <el-table-column type="expand">
          <template #default="{ row }">
            <div style="width: 1000px; margin-inline: auto">
              <h3>Starring in</h3>
              <el-table :data="row.movies">
                <el-table-column prop="id" label="ID" width="80" sortable></el-table-column>
                <el-table-column label="Cover" width="90">
                  <template #default="{ row }">
                    <img :src="getImageUrl(row.img)" alt="Movie cover" class="thumbnail" />
                  </template>
                </el-table-column>
                <el-table-column prop="title" label="Title" sortable width="250">
                  <template #default="{ row }">
                    <span class="movie-title" @click="goToMoviePage(row.movieId)">{{ row.title }}</span>
                  </template>
                </el-table-column>
                <el-table-column prop="year" label="Premiere" width="120" sortable></el-table-column>
                <el-table-column label="Genres" width="300">
                  <template #default="{ row }">
                    {{ row.genres.map(genre => genre.name).join(', ') || 'No genres' }}
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
            </div>
          </template>
        </el-table-column>

        <el-table-column prop="id" label="ID" width="80" sortable></el-table-column>
        <el-table-column label="Picture" width="120">
          <template #default="{ row }">
            <img :src="getImageUrl(row.img)" alt="Person picture" class="thumbnail" />
          </template>
        </el-table-column>
        <el-table-column prop="name" label="Name" sortable></el-table-column>
        <el-table-column prop="surname" label="Surname" sortable></el-table-column>
        <el-table-column label="Birth Date" prop="birthDate" sortable>
          <template #default="{ row }">
            {{ formatDate(row.birthDate) }}
          </template>
        </el-table-column>
        <el-table-column label="Birthplace" prop="countryOfOrigin" sortable></el-table-column>
        <el-table-column label="Actions" width="200">
          <template #default="{ row }">
            <div class="actions">
              <el-button class="edit" :icon="Edit" circle @click="openEditDialog(row.id)"></el-button>
              <el-button type="danger" :icon="Delete" circle @click="deletePerson(row.id)" style="margin-left: 1px;">
              </el-button>
            </div>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <Pagination v-if="totalPages > 1" :totalElements="totalElements" :itemsPerPage="itemsPerPage"
      :currentPage="currentPage" @page-change="goToPage" />

    <div v-if="confirmDeleteModalVisible" class="modal-overlay" @click="resetDialog">
      <div class="modal-content" @click.stop>
        <h3>Confirm deletion</h3>
        <p>Do you wish to delete person {{ personIdToDelete }}?</p>
        <div class="modal-footer">
          <el-button type="danger" :icon="Delete" @click="confirmDeletePerson">Delete</el-button>
          <el-button type="info" @click="resetDialog">Cancel</el-button>
        </div>
      </div>
    </div>

    <div class="modal-content" v-if="editDialogVisible" title="Edit person">
      <EditPerson :personId="personToEdit" @close="editDialogVisible = false" @refresh="fetchData(currentPage)" />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import axios from 'axios';
import Pagination from '@/components/Pagination.vue';
import { ElNotification } from 'element-plus';
import { Edit, Delete, ArrowRight } from '@element-plus/icons-vue';
import EditPerson from './EditPerson.vue';
import Skeleton from '@/components/Skeleton.vue';

const editDialogVisible = ref(false);
const personToEdit = ref(null);
const confirmDeleteModalVisible = ref(false);
const personIdToDelete = ref(null);

const people = ref([]);
const loading = ref(true);
const error = ref(null);
const currentPage = ref(1);
const itemsPerPage = ref(15);
const totalPages = ref(0);
const totalElements = ref(0);

const formatDate = (timestamp) => {
  if (!timestamp) return '-';
  const date = new Date(timestamp);
  return date.toLocaleDateString();
};

const getImageUrl = (imageName) => `/api/image/${imageName}`;

const fetchMoviesForPerson = async (personId) => {
  try {
    const response = await axios.get(`http://127.0.0.1:8000/person/${personId}/roles`);
    return response.data.map(role => {
      const movie = role.movie;
      return {
        id: movie.id,
        movieId: movie.id,
        title: movie.title,
        year: movie.year,
        img: movie.img,
        genres: movie.genres
      };
    });
  } catch (error) {
    console.error("Error fetching movies data:", error);
    return [];
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

const fetchData = async (page) => {
  try {
    loading.value = true;

    const response = await axios.get('/api/persons', {
      params: {
        page: page - 1,
        size: itemsPerPage.value,
      },
    });

    people.value = response.data.content;

    for (let person of people.value) {
      const movies = await fetchMoviesForPerson(person.id);
      person.movies = movies;
    }

    totalPages.value = Math.ceil(response.data.totalElements / itemsPerPage.value);
    totalElements.value = response.data.totalElements;
    error.value = null;
  } catch (err) {
    error.value = 'There was an error loading the people.';
  } finally {
    loading.value = false;
  }
};


const currentSort = ref({ field: 'id', order: 'ascending' });

const handleSortChange = ({ prop, order }) => {
  currentSort.value = { field: prop, order };
  fetchData(currentPage.value);
};

const updateFilters = (newFilters) => {
  filters.value = newFilters;
  currentPage.value = 1;
  fetchData(currentPage.value);
};

const openEditDialog = (id) => {
  personToEdit.value = id;
  editDialogVisible.value = true;
};

const deletePerson = (personId) => {
  personIdToDelete.value = personId;
  confirmDeleteModalVisible.value = true;
};

const confirmDeletePerson = async () => {
  try {
    await axios.delete(`/api/person/${personIdToDelete.value}`);

    people.value = people.value.filter(person => person.id !== personIdToDelete.value);
    totalElements.value -= 1;

    ElNotification.success({
      title: 'Success',
      message: 'Person deleted successfully',
    });

    resetDialog();
  } catch (error) {
    ElNotification.error({
      title: 'Error',
      message: 'Could not delete the person. Try again later',
    });
  }
};

const goToPage = (page) => {
  if (page < 1 || page > totalPages.value) return;
  currentPage.value = page;
  fetchData(currentPage.value);
};

const resetDialog = () => {
  confirmDeleteModalVisible.value = false;
  personIdToDelete.value = null;
};

onMounted(() => {
  fetchData(currentPage.value);
});
</script>

<style scoped>
.person-table {
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