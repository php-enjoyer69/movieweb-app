<template>
    <Skeleton v-if="loading" :rows="10" :animated="true" />
    <div v-else-if="error">{{ error }}</div>

    <h3>Reported reviews</h3>
    <el-table :data="reviewReports" class="report-table">
        <el-table-column type="expand">
            <template #default="{ row }">
                
                <div style="width: 1000px; margin-inline: auto">
                    <h3>Users who reported this review</h3>

                    <el-table v-if="row.reportedBy.length" :data="row.reportedBy">
                        <el-table-column prop="userId" label="User ID" width="120" sortable></el-table-column>
                        <el-table-column prop="userName" label="User Name" width="250" sortable></el-table-column>
                        <el-table-column prop="reportedAt" label="Reported At" width="200" sortable>
                            <template #default="{ row }">
                                {{ formatSecondDate(row.reportedAt) }}
                            </template>
                        </el-table-column>
                        <el-table-column prop="reportId" label="Report ID" width="120" sortable>
                            <template #default="{ row }">
                                {{ row.reportId }}
                            </template>
                        </el-table-column>
                        <el-table-column width="150">
                            <template #default="{ row }">
                                <el-button type="danger" :icon="Delete" circle @click="deleteReport(row.reportId)" />
                            </template>
                        </el-table-column>
                    </el-table>

                    <p v-else>No reports found</p>
                </div>
            </template>
        </el-table-column>

        <el-table-column prop="id" label="Review ID" width="130" sortable></el-table-column>
        <el-table-column prop="movieId" label="Movie ID" width="120" sortable></el-table-column>
        <el-table-column prop="userName" label="Author" width="100" sortable></el-table-column>
        
        <el-table-column prop="rating" label="Score" width="90" sortable>
            <template #default="{ row }">
                <span>
                    <Icon icon="mdi:star" style="color: #9b4dca; vertical-align: middle; font-size: medium; margin: -3px 0 0 5px" />
                </span>
                {{ row.rating }}
            </template>
        </el-table-column>

        <el-table-column prop="content" width="400"></el-table-column>
        <el-table-column prop="createdAt" label="Created At" width="150" sortable>
            <template #default="{ row }">
                {{ formatDate(row.createdAt) }}
            </template>
        </el-table-column>
        <el-table-column width="100">
            <template #default="{ row }">
                <el-button type="danger" :icon="Delete" circle @click="deleteReview(row.movieId, row.reviewId)" />
            </template>
        </el-table-column>
    </el-table>

    <Pagination style="margin-bottom: 60px;"
        v-if="totalPages > 1"
        :totalElements="totalElements"
        :itemsPerPage="itemsPerPage"
        :currentPage="currentPage"
        @page-change="goToPage"
    />
</template>

<script setup>
import { ref, onMounted, computed } from 'vue';
import axios from 'axios';
import { ElNotification } from 'element-plus';
import Skeleton from './Skeleton.vue';
import { Delete } from '@element-plus/icons-vue';
import Pagination from './Pagination.vue';
import { Icon } from '@iconify/vue';

const reviewReports = ref([]);
const loading = ref(true);
const error = ref(null);
const currentPage = ref(1);
const itemsPerPage = ref(10);
const totalPages = ref(0);
const totalElements = ref(0);

const fetchReviewReports = async () => {
    try {
        loading.value = true;
        const response = await axios.get('/api/reviews', {
            params: { page: currentPage.value - 1, size: itemsPerPage.value },
        });

        const reviews = response.data.content;
        const reportsResponse = await axios.get('/api/reports');
        const reports = reportsResponse.data;

        reviewReports.value = reviews
            .filter(review => reports.some(report => report.reviewId === review.id))
            .map(review => {
                const relevantReports = reports.filter(report => report.reviewId === review.id);

                return {
                    id: relevantReports[0]?.id,
                    reviewId: review.id,
                    movieId: review?.movieId,
                    userId: review?.userId || 'Unknown',
                    userName: review?.userName || `User ${review?.userId || 'Unknown'}`,
                    rating: review?.rating || 'N/A',
                    content: review?.content || '-',
                    createdAt: review?.createdAt || null,
                    reportedBy: relevantReports.map(r => ({
                        userId: r.userId,
                        userName: r.userName || `User ${r.userId}`,
                        reportedAt: r.reportedAt,
                        reportId: r.id,
                    })),
                };
            });

        totalElements.value = response.data.totalElements;
        totalPages.value = Math.ceil(totalElements.value / itemsPerPage.value);

        error.value = null;
    } catch (err) {
        error.value = 'Failed to load review reports';
        ElNotification.error({ title: 'Error', message: 'Failed to load review reports. Please try again.' });
    } finally {
        loading.value = false;
    }
};

const goToPage = (page) => {
    if (page < 1 || page > totalPages.value) return;
    currentPage.value = page;
    fetchReviewReports();
};

const deleteReport = async (reportId) => {
    try {
        await axios.delete(`/api/reports/${reportId}`);

        reviewReports.value.forEach(report => {
            report.reportedBy = report.reportedBy.filter(r => r.reportId !== reportId);
        });

        reviewReports.value = reviewReports.value.filter(report => report.reportedBy.length > 0);

        ElNotification.success({ title: 'Success', message: 'Report has been successfully deleted' });
    } catch (err) {
        ElNotification.error({ title: 'Error', message: 'Failed to delete the report. Please try again.' });
    }
};


const deleteReview = async (movieId, reviewId) => {
    try {
        await axios.delete(`/api/movie/${movieId}/review/${reviewId}`);
        reviewReports.value = reviewReports.value.filter(report => report.reviewId !== reviewId);
        ElNotification.success({ title: 'Success', message: 'Review has been successfully deleted' });
    } catch (err) {
        ElNotification.error({ title: 'Error', message: 'Failed to delete the review. Please try again.' });
    }
};

const formatDate = (timestamp) => {
    if (!timestamp) return '-';
    
    const date = timestamp instanceof Date ? timestamp : new Date(timestamp);

    if (isNaN(date.getTime())) return '-';

    return `${String(date.getDate()).padStart(2, '0')}.${String(date.getMonth() + 1).padStart(2, '0')}.${date.getFullYear()} 
            ${String(date.getHours()).padStart(2, '0')}:${String(date.getMinutes()).padStart(2, '0')}`;
};

const formatSecondDate = (timestamp) => {
    if (!timestamp) return '-';
    
    const date = new Date(timestamp[0], timestamp[1] - 1, timestamp[2], timestamp[3], timestamp[4], timestamp[5]);

    if (isNaN(date.getTime())) return '-';

    return `${String(date.getDate()).padStart(2, '0')}.${String(date.getMonth() + 1).padStart(2, '0')}.${date.getFullYear()} 
            ${String(date.getHours()).padStart(2, '0')}:${String(date.getMinutes()).padStart(2, '0')}`;
};


onMounted(fetchReviewReports);
</script>

<style scoped>
h3 {
color: #555;
}

.report-table {
    margin-top: 35px;
    margin-bottom: 35px;
}

.actions {
    display: flex;
    align-items: center;
}

.actions .el-button {
    margin-right: 10px;
}

.thumbnail {
    width: 60px;
    height: 60px;
    object-fit: cover;
    border-radius: 1px;
    border: 1px solid #ddd;
}

.edit {
    background-color: var(--logo);
    border-color: var(--logo);
}

.edit:hover {
    background-color: var(--light-purple);
    border-color: var(--light-purple);
    color: #fff;
}
</style>
