<template>
  <div class="overlay">
    <div class="assign-role-container">
      <h2 class="form-title">Assign Roles to Movie</h2>

      <el-form :model="roles" ref="roleForm">
        <el-form-item label="Movie" prop="movie" style="margin-bottom: 25px;">
          <el-select v-model="selectedMovie" placeholder="Select movie" class="assign-role-input" filterable>
            <el-option v-for="movie in movies" :key="movie.id" :label="movie.title" :value="movie" />
          </el-select>
        </el-form-item>

        <div v-for="(role, index) in roles" :key="index" class="role-entry">
          <el-form-item label="Person">
            <el-select v-model="role.person" placeholder="Select person" class="assign-role-input" size="large" filterable>
              <el-option v-for="person in persons" :key="person.id" :label="`${person.name} ${person.surname}`" :value="person" />
            </el-select>
          </el-form-item>

          <el-form-item label="Role">
            <el-select v-model="role.role" placeholder="Select role" class="assign-role-input" size="large">
              <el-option v-for="roleOption in roleEnums" :key="roleOption" :label="roleOption" :value="roleOption" />
            </el-select>
          </el-form-item>

          <el-link :underline="false" type="danger" circle @click="removeRole(index)" style="margin-top: -15px;">
            <Icon icon="mdi:close" style="font-size: x-large;" />
          </el-link>
        </div>

        <div class="actions">
          <el-link :underline="false" @click="addRole">
            <Icon icon="mdi:plus" style="font-size: large; margin-right: 5px;"/> Add Another Role
          </el-link>

          <el-button type="success" @click="submitRoles" style="margin-left: -85px;">
            <Icon icon="mdi:check" style="font-size: large; margin-right: 5px;"/> Submit
          </el-button>

          <el-link :underline="false" @click="closeDialog">
           Cancel
          </el-link>
        </div>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import { reactive, ref, onMounted } from 'vue';
import axios from 'axios';
import { ElNotification } from 'element-plus';
import { Icon } from '@iconify/vue';

const emit = defineEmits();

const selectedMovie = ref(null);
const roles = reactive([]);
const movies = ref([]);
const persons = ref([]);
const roleEnums = ref([]);

const fetchMovies = async () => {
  try {
    const response = await axios.get('/api/movies');
    movies.value = response.data.content;
  } catch (error) {
    console.error('Error fetching movies:', error);
  }
};

const fetchPersons = async () => {
  try {
    const response = await axios.get('/api/persons');
    persons.value = response.data.content;
  } catch (error) {
    console.error('Error fetching persons:', error);
  }
};

const fetchRolesEnum = async () => {
  try {
    const response = await axios.get('/api/roles');
    roleEnums.value = response.data;
  } catch (error) {
    console.error('Error fetching roles:', error);
  }
};

onMounted(() => {
  fetchMovies();
  fetchPersons();
  fetchRolesEnum();
});

const addRole = () => {
  roles.push({ person: null, role: '' });
};

const removeRole = (index) => {
  roles.splice(index, 1);
};

const submitRoles = async () => {
  if (!selectedMovie.value) {
    ElNotification.error({ title: 'Error', message: 'Please select a movie.' });
    return;
  }

  if (roles.length === 0) {
    ElNotification.error({ title: 'Error', message: 'Add at least one role.' });
    return;
  }

  try {
    const rolesToSend = roles.map((role) => ({
      personId: role.person.id,
      role: role.role,
    }));

    await axios.post(`http://127.0.0.1:8000/movie/${selectedMovie.value.id}/roles`, rolesToSend);

    ElNotification.success({ title: 'Success', message: 'Roles assigned successfully!' });

    closeDialog();
  } catch (err) {
    ElNotification.error({ title: 'Error', message: 'Failed to assign roles.' });
  }
};

const closeDialog = () => {
  selectedMovie.value = null;
  roles.splice(0, roles.length);
  emit('close');
};
</script>

<style scoped>
.overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  z-index: 999;
  display: flex;
  justify-content: center;
  align-items: center;
}

.assign-role-container {
  width: 700px;
  padding: 30px;
  background-color: #ffffff;
  border-radius: 12px;
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.2);
  position: relative;
}

.close {
  position: absolute;
  top: 10px;
  right: 10px;
  font-size: 1.8em;
  cursor: pointer;
}

.form-title {
  text-align: center;
  margin-bottom: 25px;
}

.assign-role-input {
  width: 100%;
  font-size: 1.2em;
}

.role-entry {
  display: flex;
  align-items: center;
  gap: 15px;
  margin-bottom: 5px;
}

.role-entry .el-form-item {
  flex: 1;
}

.el-select {
  width: 100%;
}

.actions {
  display: flex;
  justify-content: space-between;
  margin-top: 20px;
}
</style>
