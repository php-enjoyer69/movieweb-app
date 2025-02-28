<template>
    <div class="register-container">
        <div class="register">
            <router-link to="/admin">
                <el-link :underline="false" :icon="ArrowLeft">
                    Back to Admin Panel
                </el-link>
            </router-link>
            <h2 class="register-title">Add new user</h2>
            <el-form @submit.native.prevent="handleRegister" :model="form" ref="registerForm" label-position="top">
                <el-form-item label="Email" prop="email" :error="error?.email"
                    :show-message="error?.email ? true : false" class="register-form-item">
                    <el-input v-model="form.email" class="register-input" placeholder="Email address" />
                </el-form-item>
                <el-form-item label="Username" prop="name" :error="error?.name"
                    :show-message="error?.name ? true : false" class="register-form-item">
                    <el-input v-model="form.name" class="register-input" placeholder="Username" />
                </el-form-item>
                <el-form-item label="Password" prop="password" :error="error?.password"
                    :show-message="error?.password ? true : false" class="register-form-item">
                    <el-input v-model="form.password" class="register-input" type="password" placeholder="Password"
                        show-password />
                </el-form-item>
                <el-form-item label="Confirm password" prop="passwordConfirmation" :error="error?.passwordConfirmation"
                    :show-message="error?.passwordConfirmation ? true : false" class="register-form-item">
                    <el-input v-model="form.passwordConfirmation" class="register-input" type="password"
                        placeholder="Confirm password" show-password />
                </el-form-item>
                <el-form-item prop="role" :error="error?.role" :show-message="error?.role ? true : false"
                    class="register-form-item">
                    <el-radio-group v-model="form.role" placeholder="Select role">
                        <el-radio label="ADMIN">ADMIN</el-radio>
                        <el-radio label="USER">USER</el-radio>
                    </el-radio-group>
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" native-type="submit" class="register-button">Add</el-button>
                </el-form-item>
                <el-form-item>
                    <el-button type="info" class="cancel-button" @click="resetForm">Cancel</el-button>
                </el-form-item>
            </el-form>
            <div v-if="loading" class="loading-indicator">Loading...</div>
        </div>
        <div class="users">
            <UserFilter @updateFilters="updateFilters" />
            <Skeleton v-if="loading" :rows="20" :animated="true" />
            <div v-else-if="error">{{ error }}</div>
            <div v-else-if="usersError">{{ usersError }}</div>
            <div v-else-if="users.length === 0">
                <el-empty description="No users" />
            </div>
            <div v-else>
                <el-table :data="users" class="user-table" @sort-change="handleSortChange">
                    <el-table-column prop="id" label="ID" width="80" sortable></el-table-column>
                    <el-table-column prop="role" label="Access" width="100" sortable></el-table-column>
                    <el-table-column prop="email" label="Email address" sortable></el-table-column>
                    <el-table-column prop="name" label="Username" sortable></el-table-column>
                    <el-table-column label="" width="100">
                        <template #default="{ row }">
                            <div class="actions">
                                <el-button type="danger" :icon="Delete" circle @click="deleteUser(row.id)"
                                    style="margin-left: 1px;">
                                </el-button>
                            </div>
                        </template>
                    </el-table-column>
                </el-table>
            </div>
            <Pagination v-if="totalPages > 1" :current-page="currentPage" :total-elements="totalElements"
                :items-per-page="itemsPerPage" @page-change="goToPage" />
        </div>
    </div>
    <div v-if="confirmDeleteModalVisible" class="modal-overlay" @click="resetDialog">
        <div class="modal-content" @click.stop>
            <h3>Confirm deletion</h3>
            <p>Do you wish to delete user {{ userIdToDelete }}?</p>
            <div class="modal-footer">
                <el-button type="danger" :icon="Delete" @click="confirmDeleteUser">Delete</el-button>
                <el-button type="info" @click="resetDialog">Cancel</el-button>
            </div>
        </div>
    </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import axios from 'axios';
import { useAuthStore } from '@/stores/auth.store';
import { useRouter } from 'vue-router';
import { ElMessage, ElNotification } from 'element-plus';
import { Delete } from '@element-plus/icons-vue';
import { ArrowLeft } from '@element-plus/icons-vue';
import UserFilter from '@/components/UserFilter.vue';
import Pagination from '@/components/Pagination.vue';
import Skeleton from '@/components/Skeleton.vue';

const router = useRouter();
const authStore = useAuthStore();
const form = ref({
    email: '',
    password: '',
    name: '',
    passwordConfirmation: '',
    role: ''
});
const loading = ref(false);
const error = ref(null);
const usersError = ref(null);
const users = ref([]);
const confirmDeleteModalVisible = ref(false);
const userIdToDelete = ref(null);
const currentPage = ref(1);
const itemsPerPage = ref(20); 
const totalPages = ref(0);
const totalElements = ref(0);

const filters = ref({
    name: ''
});

const success = () => {
    ElNotification({
        message: 'User added successfully',
        type: 'success',
    });
};

const handleRegister = async () => {
    loading.value = true;
    error.value = null;

    try {
        await authStore.register(form.value.email, form.value.password, form.value.name, form.value.passwordConfirmation, form.value.role);
        success();
        fetchData();
        router.push('/admin/manage-users');
    } catch (err) {
        console.error('Registration Error:', err.response ? err.response.data : err);
        error.value = err.response.data.errors;
    } finally {
        loading.value = false;
    }
};

const deleteUser = (id) => {
    userIdToDelete.value = id;
    confirmDeleteModalVisible.value = true;
    fetchData();
};

const confirmDeleteUser = async () => {
    try {
        await axios.delete(`/api/user/${userIdToDelete.value}`);
        users.value = users.value.filter(user => user.id !== userIdToDelete.value);

        ElNotification.success({
            title: 'Success',
            message: 'Account deleted successfully',
        });

        resetDialog();
    } catch (err) {
        ElNotification.error({
            title: 'Error',
            message: 'Could not delete the account. Try again later',
        });
    }
};

const resetDialog = () => {
    confirmDeleteModalVisible.value = false;
    userIdToDelete.value = null;
};

const updateFilters = (newFilters) => {
    filters.value = newFilters;
    fetchData();
};

const buildSearchQuery = () => {
  const queries = [];
  if (filters.value.name) {
    queries.push(`name:*${filters.value.name}*`);
  }
  return queries.join(',');
};

const fetchData = async () => {
    try {
        loading.value = true;
        const searchQuery = buildSearchQuery();

        const response = await axios.get('/api/users', {
            params: {
                page: currentPage.value - 1,
                size: itemsPerPage.value,
                search: searchQuery
            }
        });
        users.value = response.data.content;
        totalPages.value = Math.ceil(response.data.totalElements / itemsPerPage.value);
        totalElements.value = response.data.totalElements;
        usersError.value = null;
    } catch (err) {
        console.log(err);
        usersError.value = 'Could not load the data';
        ElNotification.error({
            title: 'Error',
            message: 'Could not load the data. Try again later',
        });
    } finally {
        loading.value = false;
    }
};

const goToPage = (page) => {
    if (page < 1 || page > totalPages.value) return;
    currentPage.value = page;
    fetchData();
};

onMounted(() => fetchData());
</script>
<style scoped>
.register-container {
    width: 1200px;
    margin: 50px auto 8rem auto;
    padding: 20px;
    border: 1px solid #dcdfe6;
    border-radius: 8px;
    box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
    background-color: #ffffff;
    display: flex;
    justify-content: space-between;
    gap: 50px;
}

.register {
    flex: 1;
}

.users {
    flex: 2;
}

.modal-overlay {
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background-color: rgba(0, 0, 0, 0.5);
    display: flex;
    align-items: center;
    justify-content: center;
    z-index: 9999;
}

.modal-content {
    background-color: white;
    padding: 20px;
    border-radius: 8px;
    box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
    width: 500px;
}

.modal-footer {
    margin-top: 20px;
    text-align: right;
}

.modal-footer el-button {
    margin-left: 10px;
}

.register-title {
    text-align: center;
    margin-top: 30px;
    margin-bottom: 20px;
}

.register-input {
    width: 100%;
}

.register-button {
    width: 100%;
}

.loading-indicator {
    text-align: center;
    margin-top: 15px;
}

.error-message {
    color: red;
    text-align: center;
    margin-top: 15px;
}

.user-info {
    margin-top: 15px;
    list-style-type: none;
    padding: 0;
}

.cancel-button {
    width: 100%;
    font-weight: 600;
}

.loading-indicator {
    text-align: center;
    margin-top: 15px;
}

.register-form-item {
    margin-bottom: 30px;
}

.el-link {
    margin-top: 20px;
    margin-bottom: 20px;
    font-size: 16px;
    font-weight: 600;
    color: #333;
}
</style>