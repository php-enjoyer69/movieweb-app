<template>
  <div class="overlay">
    <div class="add-person-container">
      <Icon icon="mdi:close" class="close" @click="closeDialog" />
      <h2 class="form-title">Add New Person</h2>

      <el-form :model="form">
        <el-form-item label="Name" prop="name" :error="error?.name" :show-message="!!error?.name">
          <el-input v-model="form.name" placeholder="Enter person's name" class="add-person-input" />
        </el-form-item>

        <el-form-item label="Surname" prop="surname" :error="error?.surname" :show-message="!!error?.surname">
          <el-input v-model="form.surname" placeholder="Enter person's surname" class="add-person-input" />
        </el-form-item>

        <el-form-item label="Bio" prop="biography" :error="error?.biography" :show-message="!!error?.biography">
          <el-input type="textarea" v-model="form.biography" placeholder="Enter biography (optional)" rows="3"
            class="add-person-input" />
        </el-form-item>

        <el-form-item label="Country of Origin" prop="countryOfOrigin" :error="error?.countryOfOrigin"
          :show-message="!!error?.countryOfOrigin">
          <el-select
            v-model="form.countryOfOrigin"
            placeholder="Select country of origin"
            class="add-person-input"
            filterable
            allow-create
            :create-method="handleCreateCountry"
          >
            <el-option v-for="country in countries" :key="country" :label="country" :value="country" />
          </el-select>
        </el-form-item>

        <el-form-item label="Birth Date" prop="birthDate" :error="error?.birthDate" :show-message="!!error?.birthDate">
          <el-date-picker
            v-model="form.birthDate"
            type="date"
            placeholder="Select birth date"
            format="yyyy-MM-dd"
            class="add-person-input"
          />
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
          <el-button type="primary" @click="savePerson" class="add-person-button">Save</el-button>
          <el-button @click="closeDialog" type="info" class="add-person-button">Cancel</el-button>
        </div>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue';
import axios from 'axios';
import { ElNotification } from 'element-plus';
import { Icon } from '@iconify/vue';
import { UploadFilled } from '@element-plus/icons-vue';

const emit = defineEmits();

const form = reactive({
  name: '',
  surname: '',
  biography: null,
  countryOfOrigin: '',
  birthDate: null,
  image: null,
});

const error = ref({});
const image = ref(null);

const countries = [
  'United States', 'Canada', 'United Kingdom', 'Germany', 'France', 'Australia', 'Japan', 'India', 'Brazil', 'Mexico',
  'Russia', 'China', 'South Korea', 'Italy', 'Spain', 'Poland', 'Argentina', 'South Africa', 'Egypt', 'Nigeria'
];

const handleCreateCountry = (queryString) => {
  if (queryString !== '') {
    const newCountry = queryString;
    countries.push(newCountry);
    form.countryOfOrigin = newCountry;
  }
};

const savePerson = async () => {
  try {
    error.value = {};

    const formData = new FormData();

    const personToSend = {
      name: form.name,
      surname: form.surname,
      biography: form.biography || '',
      countryOfOrigin: form.countryOfOrigin || '',
      birthDate: form.birthDate || '',
    };

    const json = JSON.stringify(personToSend);
    const blob = new Blob([json], {
      type: 'application/json',
    });

    formData.append('person', blob);

    if (image.value) {
      formData.append('image', image.value);
    }

    await axios.post('http://127.0.0.1:8000/person', formData);

    emit('refresh');
    closeDialog();

    ElNotification.success({
      title: 'Success',
      message: 'Person added successfully.',
    });
  } catch (err) {
    if (err.response && err.response.data && err.response.data.errors) {
      error.value = err.response.data.errors;
    } else {
      ElNotification.error({
        title: 'Error',
        message: 'Failed to save person. Please try again.',
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

.add-person-container {
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

.add-person-input {
  width: 100%;
}

.add-person-button {
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
