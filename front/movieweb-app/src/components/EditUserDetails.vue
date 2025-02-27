<template>
  <div class="profile-header">
    <div class="profile-picture-section">
      <el-avatar :size="100" :src="user.img ? getImageUrl(user.img) : defaultAvatar" />
      <div class="change-picture-section">
        <el-link :underline="false" type="primary" class="change-picture-link" @click="showUploadModal = true">
          <el-icon class="el-icon--change-picture"><upload-filled /></el-icon>
          Change Picture
        </el-link>
      </div>


      <el-dialog v-model="showUploadModal" title="Upload Profile Picture" width="30%">
        <el-upload class="upload-demo" drag action="#" :on-change="handleImage" :show-file-list="false">
          <el-icon class="el-icon--upload"><upload-filled /></el-icon>
          <div class="el-upload__text">Drop file here or <em>click to upload</em></div>
          <template #tip>
            <div class="el-upload__tip">jpg/png files with a size less than 500kb</div>
          </template>
        </el-upload>
        <div v-if="uploadedFileName" class="uploaded-file-name">
          Uploaded file: {{ uploadedFileName }}
        </div>
        <template #footer>
          <el-button type="success" @click="saveChangesAndClose">
            <Icon icon="mdi:check" width="24" height="24" />
            Save Changes
          </el-button>
          <el-button @click="showUploadModal = false">Cancel</el-button>
        </template>
      </el-dialog>
    </div>

    <div class="user-info">
      <p><strong>Email:</strong> {{ user.email }}</p>
      <div class="editable-field">
        <el-form v-if="userForm.name" :model="userForm" class="edit-form">
          <el-form-item label="Username">
            <el-input v-model="userForm.name" placeholder="Enter your username" />
          </el-form-item>

          <el-form-item label="New Password">
            <el-input v-model="userForm.password" type="password" placeholder="Enter new password" show-password />
          </el-form-item>
        </el-form>
      </div>

      <el-form-item>
        <el-button type="success" @click="saveChanges">
          <Icon icon="mdi:check" width="24" height="24" />
          Save Changes
        </el-button>
      </el-form-item>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, reactive } from 'vue';
import { useAuthStore } from '../stores/auth.store';
import axios from 'axios';
import { Icon } from '@iconify/vue';
import { UploadFilled } from '@element-plus/icons-vue';
import { ElMessage, ElLink, ElDialog, ElButton } from 'element-plus';

const getImageUrl = (imageName) => `/api/image/${imageName}`;
const defaultAvatar = 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQBsdZ-yU8qdxr0WrIA8HLa31tB_cCFZJt-2Q&s';
const authStore = useAuthStore();
const user = computed(() => authStore.user);

const userForm = reactive({
  name: user.value.name || '',
  password: '',
});

const image = ref(null);
const uploadedFileName = ref(null);
const showUploadModal = ref(false);

const handleImage = (event) => {
  const file = event.raw;
  if (file) {
    image.value = file;
    uploadedFileName.value = file.name;
  }
};

const saveChangesAndClose = async () => {
  await saveChanges();
  showUploadModal.value = false;
};

const saveChanges = async () => {
  try {
    const formData = new FormData();
    const data = { name: userForm.name };

    if (userForm.password) {
      data.password = userForm.password;
    }

    const json = JSON.stringify(data);
    const blob = new Blob([json], { type: 'application/json' });
    formData.append('user', blob);

    if (image.value) {
      formData.append('image', image.value);
    }

    const response = await axios.patch(`/api/user/${user.value.id}`, formData);
    authStore.user = response.data;
    localStorage.setItem('user', JSON.stringify(response.data));

    ElMessage.success('User details updated successfully');
  } catch (error) {
    console.error('Error updating user details:', error);
    ElMessage.error('Failed to update user details');
  }
};
</script>

<style scoped>
.upload-demo {
  margin-top: 10px;
}

.uploaded-file-name {
  margin-top: 10px;
  font-size: 14px;
  color: #333;
  font-weight: 500;
}

.user-info {
  flex-grow: 1;
}

.profile-header {
  display: flex;
  align-items: center;
  margin-bottom: 1.5rem;
}

.profile-picture-section {
  margin-right: 2rem;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.change-picture-section {
  margin-top: 10px;
}

.change-picture-link {
  color: #888;
  font-weight: 500;
  font-size: 14px;
}

.change-picture-link:hover {
  color: #555;
}

.change-picture-link .el-icon--change-picture {
  margin-right: 6px;
}
</style>
