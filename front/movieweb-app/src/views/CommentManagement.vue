<template>
    <div class="main-container">
        <router-link to="/admin">
            <el-link :underline="false" :icon="ArrowLeft">
                Back to Admin Panel
            </el-link>
        </router-link>
        <h2>Manage user comments</h2>

         <CommentReports />

         <CommentList />

        <Pagination v-if="totalPages > 1" :totalElements="totalElements" :itemsPerPage="itemsPerPage"
            :currentPage="currentPage" @page-change="goToPage" />
    </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import axios from 'axios';
import { ElNotification } from 'element-plus';
import { ArrowLeft } from '@element-plus/icons-vue';
import Pagination from '@/components/Pagination.vue';
import CommentReports from '@/components/CommentReports.vue';
import CommentList from '@/components/CommentList.vue';


const comments = ref([]);
const loading = ref(true);
const error = ref(null);
const currentPage = ref(1);
const itemsPerPage = ref(15);
const totalPages = ref(0);
const totalElements = ref(0);

const fetchComments = async () => {
    try {
        loading.value = true;
        const response = await axios.get('/api/comments', {
            params: {
                page: currentPage.value - 1,
                size: itemsPerPage.value,
            },
        });
        comments.value = response.data.content;
        totalElements.value = response.data.totalElements;
        totalPages.value = Math.ceil(response.data.totalElements / itemsPerPage.value);
        error.value = null;
    } catch (err) {
        error.value = 'Failed to load data from the database';
        ElNotification.error({
            title: 'Error',
            message: 'Failed to load comments. Please try again.',
        });
    } finally {
        loading.value = false;
    }
};

const goToPage = (page) => {
    if (page < 1 || page > totalPages.value) return;
    currentPage.value = page;
    fetchComments();
};

onMounted(() => {
    fetchComments(currentPage.value);
});
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
    margin-bottom: 50px;
}

.el-link {
    margin-top: 20px;
    margin-bottom: 20px;
    font-size: 16px;
    font-weight: 600;
    color: #333;
}
</style>