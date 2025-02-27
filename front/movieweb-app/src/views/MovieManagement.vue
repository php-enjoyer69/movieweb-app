<template>
  <div class="main-container">
    <router-link to="/admin">
      <el-link :underline="false" :icon="ArrowLeft">
        Back to Admin Panel
      </el-link>
    </router-link>
    <h2>Manage movies</h2>

    <el-button @click="openMovieModal" type="success" class="add-product-btn">
      <Icon icon="mdi:plus" style="margin-right: 4px; font-size: large; color: #9b4dca;" /> New movie
    </el-button>

    <el-button @click="openGenreModal" type="success" class="add-product-btn" >
      <Icon icon="mdi:plus" style="margin-right: 4px; font-size: large; color: #9b4dca;" /> New movie genre
    </el-button>
    
    <el-button @click="openRoleModal" type="success" class="add-product-btn" >
      <Icon icon="mdi:plus" style="margin-right: 4px; font-size: large; color: #9b4dca;" /> New roles
    </el-button>

    <MovieList />

    <div v-if="showMovieModal">
      <AddMovie @close="closeMovieModal" />
    </div>

    <div v-if="showGenreModal">
      <AddMovieGenre @close="closeGenreModal" />
    </div>

    <div v-if="showRoleModal">
      <AssignRolesToMovie @close="closeRoleModal" />
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { ArrowLeft } from '@element-plus/icons-vue';
import MovieList from '@/components/MovieList.vue';
import AddMovie from '@/components/AddMovie.vue';
import { Icon } from '@iconify/vue';
import AssignRolesToMovie from '@/components/AssignRolesToMovie.vue';
import AddMovieGenre from '@/components/AddMovieGenre.vue';

const showMovieModal = ref(false);
const showRoleModal = ref(false);
const showGenreModal = ref(false);

const openMovieModal = () => {
  showMovieModal.value = true;
};
const closeMovieModal = () => {
  showMovieModal.value = false;
};
const closeGenreModal = () => {
  showGenreModal.value = false;
};

const openGenreModal = () => {
  showGenreModal.value = true;
};
const openRoleModal = () => {
  showRoleModal.value = true;
};
const closeRoleModal = () => {
  showRoleModal.value = false;
};
</script>

<style scoped>
.main-container {
  width: 1200px;
  margin: 50px auto 8rem auto;
  padding: 20px;
  border: 1px solid #dcdfe6;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  background-color: #ffffff;
  justify-content: space-between;
  gap: 50px;
}

h2 {
  text-align: center;
  margin-bottom: 30px;
}

.add-product-btn {
  margin-left: 7.5%;
  margin-bottom: 5px;
}

.el-link {
  margin-top: 20px;
  margin-bottom: 20px;
  font-size: 16px;
  font-weight: 600;
  color: #333;
}
</style>
