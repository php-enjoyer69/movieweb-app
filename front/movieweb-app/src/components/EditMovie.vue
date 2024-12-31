<template>
  <div class="overlay">
    <div class="edit-movie-container">
      <Icon icon="mdi:close" class="close" @click="closeDialog" />
      <h2 class="form-title">Edit Movie</h2>

      <el-form :model="form">
        <el-form-item label="Title" prop="title" :error="error?.title" :show-message="!!error?.title">
          <el-input v-model="form.title" placeholder="Enter movie title" class="edit-movie-input" />
        </el-form-item>

        <el-form-item label="Description" prop="description" :error="error?.description"
          :show-message="!!error?.description">
          <el-input type="textarea" v-model="form.description" placeholder="Enter movie description" rows="3"
            class="edit-movie-input" />
        </el-form-item>

        <el-form-item label="Year" prop="year" :error="error?.year" :show-message="!!error?.year">
          <el-input-number v-model="form.year" :min="1900" placeholder="Enter release year" class="edit-movie-input"
            :step="2" />
        </el-form-item>

        <el-form-item label="Genres" prop="genres" :error="error?.genres" :show-message="!!error?.genres">
          <el-select v-model="form.genres" multiple placeholder="Select genres" value-key="id" class="edit-movie-input">
            <el-option v-for="genre in genres" :key="genre.id" :label="genre.name" :value="genre.id" />
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
          <el-button type="primary" @click="saveMovie" class="edit-movie-button">Save</el-button>
          <el-button @click="closeDialog" type="info" class="edit-movie-button">Cancel</el-button>
        </div>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import { ref, watch, reactive } from 'vue';
import axios from 'axios';
import { ElNotification } from 'element-plus';
import { Icon } from '@iconify/vue';
import { UploadFilled } from '@element-plus/icons-vue';

const props = defineProps({
  movieId: {
    type: Number,
    required: true,
  },
});

const emit = defineEmits(['close', 'refresh']);

const form = reactive({
  title: '',
  description: '',
  year: null,
  genres: [],
  image: null,
});

const error = ref({});
const image = ref(null);
const genres = ref([]);

const loadGenres = async () => {
  try {
    const response = await axios.get('/api/movie-genres');
    genres.value = response.data.content;
  } catch (error) {
    console.error('Error loading genres:', error);
    ElNotification.error({
      title: 'Error',
      message: 'Failed to load genres.',
    });
  }
};

const loadMovie = async () => {
  try {
    const response = await axios.get(`http://127.0.0.1:8000/movie/${props.movieId}`);
    const movieData = response.data;

    form.title = movieData.title;
    form.description = movieData.description;
    form.year = movieData.year;
    form.genres = movieData.genres.map(genre => genre.id);
  } catch (error) {
    console.error('Error loading movie:', error);
    ElNotification.error({
      title: 'Error',
      message: 'Failed to load movie details.',
    });
  }
};

const saveMovie = async () => {
  try {
    error.value = {};

    const formData = new FormData();

    const movieToSend = {
      title: form.title,
      description: form.description,
      year: form.year,
      genres: form.genres.map(id => ({ id })),
    };

    const json = JSON.stringify(movieToSend);
    const blob = new Blob([json], {
      type: 'application/json',
    });

    formData.append('movie', blob);

    if (image.value) {
      formData.append('image', image.value);
    }

    await axios.patch(`http://127.0.0.1:8000/movie/${props.movieId}`, formData);

    emit('refresh');
    closeDialog();

    ElNotification.success({
      title: 'Success',
      message: 'Movie updated successfully.',
    });
  } catch (err) {
    if (err.response && err.response.data && err.response.data.errors) {
      error.value = err.response.data.errors;
    } else {
      ElNotification.error({
        title: 'Error',
        message: 'Failed to save movie. Please try again.',
      });
    }
  }
};

const closeDialog = () => {
  emit('close');
};

const uploadedFileName = ref(null);

const handleImage = (event) => {
  const file = event.raw;
  if (file) {
    image.value = file;
    uploadedFileName.value = file.name;
  }
};

watch(
  () => props.movieId,
  (newMovieId) => {
    if (newMovieId) {
      loadMovie();
    }
  },
  { immediate: true }
);

loadGenres();
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

.edit-movie-container {
  width: 500px;
  max-width: 90%;
  padding: 20px;
  border: 1px solid #dcdfe6;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  background-color: #ffffff;
  z-index: 1000;
  display: flex;
  flex-direction: column;
  align-items: stretch;
  justify-content: flex-start;
  position: relative;
}

.close {
  position: absolute;
  top: 10px;
  right: 10px;
  font-size: 1.8em;
  color: #333;
  cursor: pointer;
}

.form-title {
  text-align: center;
  margin-bottom: 20px;
}

.edit-movie-input {
  width: 100%;
  font-family: Quicksand;
}

.edit-movie-button {
  width: 100%;
  margin-top: 20px;
}

.el-form-item {
  width: 100%;
}

.el-form-item .el-form-item__label {
  text-align: left;
  margin-bottom: 5px;
  font-weight: 600;
}

.el-form-item .el-input,
.el-form-item .el-input-number,
.el-form-item .el-input__inner {
  width: 100%;
}

.el-form-item .el-button {
  width: 100%;
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