<template>
  <div class="admin-panel">
    <el-card class="admin-card" shadow="false">
      <h1 class="card-title">Admin Panel</h1>
      <div class="button-container">
        <router-link to="/admin/manage-movies" class="link">
          <div class="center">
            <Icon icon="mdi:movie-edit-outline" class="icon" />
            <el-link :underline="false" class="link-text">
              <span>Manage movies</span>
            </el-link>
          </div>
        </router-link>
        <router-link to="/admin/manage-persons" class="link">
          <div class="center">
            <Icon icon="mdi:star-circle" class="icon" />
            <el-link :underline="false" class="link-text">
              <span>Manage celebrities</span>
            </el-link>
          </div>
        </router-link>
        <router-link to="/admin/manage-reviews" class="link">
          <div class="center">
            <Icon icon="mdi:comment-edit-outline" class="icon" />
            <el-link :underline="false" class="link-text">
              <span>Manage reviews</span>
            </el-link>
          </div>
        </router-link>
        <router-link to="/admin/manage-users" class="link">
          <div class="center">
            <Icon icon="mdi:account-plus" class="icon" />
            <el-link :underline="false" class="link-text">
              <span>Manage users</span>
            </el-link>
          </div>
        </router-link>
        <router-link to="/admin/manage-comments" class="link">
          <div class="center">
            <Icon icon="mdi:comment-outline" class="icon" />
            <el-link :underline="false" class="link-text">
              <span>Manage comments</span>
            </el-link>
          </div>
        </router-link>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { Icon } from '@iconify/vue';
</script>

<style scoped>
.admin-panel {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 50vh;
  margin-bottom: 20rem;
}

.admin-card {
  width: 800px;
  padding: 5px;
  border-radius: 8px;
  box-shadow: none;
}

.card-title {
  text-align: center;
  margin-bottom: 30px;
}

.button-container {
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
  gap: 20px;
}

.el-link {
  color: #551a8b;
  font-size: 16px;
  font-family: "Quicksand", sans-serif;
}

.el-link:hover {
  color: #3a1063;
}

.center {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  gap: 10px;
  width: 150px;
}

.icon {
  font-size: 50px;
  color: #551a8b;
}

.link {
  text-decoration: none;
}

.link-text {
  color: #551a8b;
}

.link-text:hover {
  color: #3a1063;
}
</style>
