<template>
  <div class="admin-panel">
    <el-card class="admin-card" shadow="hover">
      <h1 class="card-title">Admin Panel</h1>
      <div class="button-container">
        <router-link to="/admin/manage-movies" class="link">
          <div class="center">
            <Icon icon="mdi:movie-edit-outline" class="icon icon-pink" />
            <el-button class="button-pink">
              <span>Manage movies</span>
            </el-button>
          </div>
        </router-link>
        <router-link to="/admin/manage-users" class="link">
          <div class="center">
            <Icon icon="mdi:account-plus" class="icon icon-purple" />
            <el-button class="button-purple">
              <span>Manage users</span>
            </el-button>
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
}

.card-title {
text-align: center;
margin-bottom: 30px;
}

.button-container {
display: flex;
justify-content: center;
gap: 15px;
}

.button-pink {
background-color: #ff81d7;
border-color: #ff81d7;
color: white;
}

.button-pink:hover {
background-color: #e770bf;
border-color: #e770bf;
color: white;
}

.button-purple {
background-color: #9B59B6;
border-color: #9B59B6;
color: white;
}

.button-purple:hover {
background-color: #8E44AD;
border-color: #8E44AD;
color: white;
}

.close:hover {
color: #e770bf;
}

.el-button {
font-family: "Quicksand", sans-serif;
outline: none;
}

.el-button .iconify {
margin-right: 8px;
}

.el-button:focus {
box-shadow: 0 0 0 0.2rem rgba(255, 129, 215, 0.5);
}

.el-pagination {
display: flex;
justify-content: center;
margin-top: 1em;
}

.center {
display: flex;
flex-direction: column;
justify-content: center;
align-items: center;
gap: 10px;
}

.icon {
font-size: 50px;
}

.link{
text-decoration: none;
}

.icon-pink {
color: #ff81d7;
}

.icon-purple {
color: #9B59B6;
}
</style>
