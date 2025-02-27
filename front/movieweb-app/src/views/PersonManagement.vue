<template>
  <div class="main-container">
    <router-link to="/admin">
      <el-link :underline="false" :icon="ArrowLeft">
        Back to Admin Panel
      </el-link>
    </router-link>
    <h2>Manage celebrities</h2>

    <el-button @click="openModal" type="success" class="add-product-btn">
      <Icon icon="mdi:plus" style="margin-right: 4px; font-size: large; color: #9b4dca;" /> New person
    </el-button>

    <el-button @click="openRoleModal" type="success" class="add-product-btn" >
      <Icon icon="mdi:plus" style="margin-right: 4px; font-size: large; color: #9b4dca;" /> New roles
    </el-button>
    
    <AdminPersonFilter/>

    <PersonList />

    <div v-if="showModal">
      <AddPerson @close="closeModal" />
    </div>

    <div v-if="showRoleModal">
      <AssignRolesToMovie @close="closeRoleModal" />
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { ArrowLeft, Close, Plus } from '@element-plus/icons-vue';
import PersonList from '@/components/PersonList.vue';
import AddPerson from '@/components/AddPerson.vue';
import { Icon } from '@iconify/vue';
import AssignRolesToMovie from '@/components/AssignRolesToMovie.vue';
import AdminPersonFilter from '@/components/AdminPersonFilter.vue';

const showModal = ref(false);
const showRoleModal = ref(false);

const openModal = () => {
  showModal.value = true;
};
const closeModal = () => {
  showModal.value = false;
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