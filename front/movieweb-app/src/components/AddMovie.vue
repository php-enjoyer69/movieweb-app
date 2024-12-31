<template>
  <div class="overlay">
    <div class="add-movie-container">
      <Icon icon="mdi:close" class="close" @click="closeDialog" />
      <h2 class="form-title">Add New Movie</h2>

      <el-form :model="movie">
        <el-form-item label="Title" prop="title" :error="error?.title" :show-message="!!error?.title">
          <el-input v-model="movie.title" placeholder="Enter movie title" class="add-movie-input" />
        </el-form-item>

        <el-form-item label="Description" prop="description" :error="error?.description"
          :show-message="!!error?.description">
          <el-input type="textarea" v-model="movie.description" placeholder="Enter movie description" rows="3"
            class="add-movie-input" />
        </el-form-item>

        <el-form-item label="Year" prop="year" :error="error?.year" :show-message="!!error?.year">
          <el-input-number v-model="movie.year" :min="1900" :max="2024" placeholder="Enter release year"
            class="add-movie-input" :step="2" />
        </el-form-item>

        <el-form-item label="Genres" prop="genres" :error="error?.genres" :show-message="!!error?.genres">
          <el-select v-model="movie.genres" multiple placeholder="Select genres" value-key="id" class="add-movie-input">
            <el-option v-for="genre in genres" :key="genre.id" :label="genre.name" :value="genre" />
          </el-select>
        </el-form-item>

        <div prop="image">
          <el-upload class="upload-demo" drag action="#" :on-change="handleImage" :show-file-list="false">
            <el-icon class="el-icon--upload"><upload-filled /></el-icon>
            <div class="el-upload__text">
              Drop file here or <em>click to upload</em>
            </div>
            <template #tip>
              <div class="el-upload__tip">
                jpg/png files with a size less than 500kb
              </div>
            </template>
          </el-upload>
          <div v-if="uploadedFileName" class="uploaded-file-name">
            Uploaded file: {{ uploadedFileName }}
          </div>
        </div>

        <div class="actions">
          <el-button type="primary" @click="submitMovie" class="add-movie-button">Create</el-button>
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
import { UploadFilled } from '@element-plus/icons-vue';

const emit = defineEmits();

const image = ref(null);
const error = ref({});

const movie = reactive({
  title: '',
  description: '',
  year: null,
  rating: 0.0,
  genres: [],
});

const genres = ref([]);

const fetchGenres = async () => {
  try {
    const response = await axios.get('/api/movie-genres');
    genres.value = response.data.content;
  } catch (error) {
    console.error('Error fetching genres:', error);
    ElNotification.error({
      title: 'Error',
      message: 'Failed to fetch genres.',
      type: 'error',
      duration: 3000,
    });
  }
};

onMounted(() => {
  fetchGenres();
});

const uploadedFileName = ref(null);

const handleImage = (event) => {
  const file = event.raw;
  if (file) {
    image.value = file;
    uploadedFileName.value = file.name;
  }
};

const submitMovie = async () => {
  try {
    error.value = {};

    const genresToSend = movie.genres.map((genre) => ({ id: genre.id }));

    const movieToSend = {
      ...movie,
      genres: genresToSend,
    };

    const formData = new FormData();
    const json = JSON.stringify(movieToSend);
    const blob = new Blob([json], { type: 'application/json' });
    formData.append('movie', blob);

    if (image.value) {
      formData.append('image', image.value);
    }

    await axios.post('http://127.0.0.1:8000/movie', formData);

    ElNotification.success({
      title: 'Success',
      message: 'Movie added successfully.',
      type: 'success',
      duration: 3000,
    });

    emit('close');
    window.location.reload();
  } catch (err) {
    if (err.response && err.response.data && err.response.data.errors) {
      error.value = err.response.data.errors;
    } else {
      ElNotification.error({
        title: 'Error',
        message: 'Failed to add movie. Please try again.',
        type: 'error',
        duration: 3000,
      });
    }
  }
};

const closeDialog = () => {
  movie.title = '';
  movie.description = '';
  movie.year = null;
  movie.rating = 0.0;
  movie.genres = [];
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
