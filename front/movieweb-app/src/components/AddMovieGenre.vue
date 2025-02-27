<template>
    <div class="overlay">
      <div class="add-movie-container">
        <Icon icon="mdi:close" class="close" @click="closeDialog" />
        <h2 class="form-title">Add New Movie Genre</h2>
  
        <el-form :model="genre">
          <el-form-item label="Genre name" prop="name" :error="error?.name" :show-message="!!error?.name">
            <el-input v-model="genre.name" placeholder="Enter genre name" class="add-movie-input" />
          </el-form-item>
  
          <div class="actions">
            <el-button type="primary" @click="submitGenre" class="add-movie-button">Create</el-button>
            <el-button @click="closeDialog" type="info" class="add-movie-button">Cancel</el-button>
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
  
  const error = ref({});
  
  const genre = reactive({
    name: '',
  });
  
  const submitGenre = async () => {
    try {
      error.value = {};
  
      const genreToSend = {
        ...genre,
      };
  
      await axios.post('/api/movie-genre', genreToSend);
  
      ElNotification.success({
        title: 'Success',
        message: 'Movie genre added successfully.',
        type: 'success',
        duration: 3000,
      });
  
      emit('close');
  
    } catch (err) {
      if (err.response && err.response.data && err.response.data.errors) {
        error.value = err.response.data.errors;
      } else {
        ElNotification.error({
          title: 'Error',
          message: 'Failed to add movie genre. Please try again.',
          type: 'error',
          duration: 3000,
        });
      }
    }
  };
  
  const closeDialog = () => {
    genre.name = '';
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
  
  .add-movie-container {
    width: 500px;
    padding: 20px;
    border: 1px solid #dcdfe6;
    border-radius: 8px;
    background-color: #ffffff;
    box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
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
    margin-bottom: 20px;
  }
  
  .add-movie-input {
    width: 100%;
  }
  
  .add-movie-button {
    width: 100%;
    margin-top: 20px;
  }
  
  .upload-demo {
    margin-bottom: 20px;
  }
  
  .actions {
    display: flex;
    justify-content: space-between;
  }
  
  .uploaded-file-name {
    margin-top: 10px;
    font-size: 14px;
    color: #333;
    font-weight: 500;
  }
  </style>
  