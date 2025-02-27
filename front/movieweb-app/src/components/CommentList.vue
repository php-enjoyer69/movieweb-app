<template>
    <UserFilter :filters="filters" @updateFilters="updateFilters" />

    <Skeleton v-if="loading" :rows="10" :animated="true" />
    <div v-else-if="error">{{ error }}</div>

    <h3>All Comments</h3>
    <el-table :data="comments" class="comments-table">
        <el-table-column prop="id" label="Comment ID" width="140" sortable></el-table-column>
        <el-table-column prop="personId" label="Celebrity ID" width="140" sortable></el-table-column>
        <el-table-column prop="userName" label="User" width="150" sortable></el-table-column>
        <el-table-column prop="content" label="Content" width="400"></el-table-column>
        <el-table-column prop="createdAt" label="Created At" width="150" sortable>
            <template #default="{ row }">
                {{ formatDate(row.createdAt) }}
            </template>
        </el-table-column>
        <el-table-column width="100">
            <template #default="{ row }">
                <el-button type="danger" :icon="Delete" circle @click="deleteComment(row.id)" />
            </template>
        </el-table-column>
    </el-table>

    <Pagination style="margin-bottom: 60px;" v-if="totalPages > 1" :totalElements="totalElements"
        :itemsPerPage="itemsPerPage" :currentPage="currentPage" @page-change="goToPage" />
</template>

<script setup>
import { ref, onMounted, watch } from 'vue';
import axios from 'axios';
import { ElNotification, ElMessage } from 'element-plus';
import Skeleton from './Skeleton.vue';
import { Delete } from '@element-plus/icons-vue';
import Pagination from './Pagination.vue';
import UserFilter from './UserFilter.vue'; 

const comments = ref([]);
const loading = ref(true);
const error = ref(null);
const currentPage = ref(1);
const itemsPerPage = ref(10);
const totalPages = ref(0);
const totalElements = ref(0);
const filters = ref({
  name: ''
});

const buildSearchQuery = () => {
  const queries = [];
  if (filters.value.name) {
    queries.push(`userName:*${filters.value.name}*`);
  }
  return queries.join(',');
};

const fetchComments = async () => {
    try {
        loading.value = true;
        const response = await axios.get('/api/comments', {
            params: {
                page: currentPage.value - 1,
                size: itemsPerPage.value,
                userName: filters.value.name || '',
            }
        });

        const fetchedComments = response.data.content;
        comments.value = fetchedComments;
        totalElements.value = fetchedComments.length;
        totalPages.value = Math.ceil(totalElements.value / itemsPerPage.value);

        error.value = null;
    } catch (err) {
        error.value = 'Failed to load comments';
        ElNotification.error({ title: 'Error', message: 'Failed to load comments. Please try again.' });
    } finally {
        loading.value = false;
    }
};

const updateFilters = (newFilters) => {
    filters.value = newFilters;
    fetchComments(); 
};

const goToPage = (page) => {
    if (page < 1 || page > totalPages.value) return;
    currentPage.value = page;
    fetchComments();
};

const deleteComment = async (commentId) => {
    try {
        await axios.delete(`/api/comment/${commentId}`);
        ElMessage.success('Comment deleted successfully');
        fetchComments();
    } catch (err) {
        ElMessage.error('Failed to delete comment');
        console.error('Error deleting comment:', err);
    }
};


const formatDate = (timestamp) => {
    if (!timestamp) return '-';
    const date = new Date(timestamp[0], timestamp[1] - 1, timestamp[2], timestamp[3], timestamp[4], timestamp[5]);
    if (isNaN(date.getTime())) return '-';
    return `${String(date.getDate()).padStart(2, '0')}.${String(date.getMonth() + 1).padStart(2, '0')}.${date.getFullYear()}
            ${String(date.getHours()).padStart(2, '0')}:${String(date.getMinutes()).padStart(2, '0')}`;
};


onMounted(fetchComments);
</script>

<style scoped>
h3 {
    color: #555;
}

.comments-table {
    margin-top: 35px;
    margin-bottom: 35px;
}

.el-button {
    margin-right: 10px;
}
</style>
