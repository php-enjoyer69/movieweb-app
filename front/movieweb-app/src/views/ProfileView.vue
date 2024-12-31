<template>
  <el-card class="profile-page" shadow="hover">
    <h1>User Profile</h1>

    <div class="profile-picture-section">
      <el-avatar :size="100" :src="user.avatar || defaultAvatar" />
      <el-button type="text" @click="changeProfilePicture">Change Picture</el-button>
    </div>

    <div class="editable-field">
      <el-form :model="user" class="edit-form">
        <el-form-item label="Username">
          <el-input v-model="user.name" placeholder="Enter your username" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="saveChanges">Save Changes</el-button>
        </el-form-item>
      </el-form>
    </div>

    <p><strong>Email:</strong> {{ user.email }}</p>

    <div class="saved-movies">
      <h2>Saved Movies</h2>
      <el-table :data="user.savedMovies" style="width: 100%">
        <el-table-column prop="title" label="Title" width="200" />
        <el-table-column prop="year" label="Year" width="80" />
        <el-table-column prop="rating" label="Rating" width="80" />
      </el-table>
    </div>

    <el-button type="primary" @click="goHome">Go Home</el-button>
  </el-card>
</template>

<script>
import { useAuthStore } from '../stores/auth.store';

export default {
  name: 'ProfileView',
  data() {
    return {
      defaultAvatar: 'https://via.placeholder.com/100',
    };
  },
  computed: {
    user() {
      const authStore = useAuthStore();
      return authStore.user;
    },
  },
  methods: {
    changeProfilePicture() {
      console.log('Change profile picture clicked');
    },
    saveChanges() {
      console.log('Save changes clicked');
    },
    goHome() {
      this.$router.push('/');
    },
  },
};
</script>

<style scoped>
.profile-page {
  max-width: 800px;
  margin: 2rem auto 14rem;
  padding: 2rem;
  background-color: #fff;
}

h1 {
  color: #4b0082;
  margin: 0 0 1rem;
}

.profile-picture-section {
  display: flex;
  align-items: center;
  margin-bottom: 1.5rem;
}

.profile-picture-section el-avatar {
  margin-right: 1rem;
}

.editable-field {
  margin-bottom: 1.5rem;
}

.saved-movies {
  margin-top: 2rem;
}

.saved-movies h2 {
  color: #4b0082;
  margin-bottom: 1rem;
}

p {
  margin-top: 1rem;
  font-size: 1.1rem;
  line-height: 1.6;
}
</style>
