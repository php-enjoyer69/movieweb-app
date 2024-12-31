<template>
  <div class="main-container">
    <router-link to="/admin">
      <el-link :underline="false" :icon="ArrowLeft">
        Back to Admin Panel
      </el-link>
    </router-link>
    <h2>Manage movies</h2>

    <el-button @click="openModal" type="primary" class="add-product-btn" :icon="Plus">Add new movie</el-button>

    <MovieList />

    <div v-if="showModal">
      <AddMovie @close="closeModal" />
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { ArrowLeft, Close, Plus } from '@element-plus/icons-vue';
import MovieList from '@/components/MovieList.vue';
import AddMovie from '@/components/AddMovie.vue';

const showModal = ref(false);

const openModal = () => {
  showModal.value = true;
};

const closeModal = () => {
  showModal.value = false;
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