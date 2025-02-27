<template>
    <div class="voted-people">
        <h2>People Voted</h2>
        <p>{{ votedPeople.length }} voted people so far</p>
        <el-table v-if="votedPeople.length" :data="votedPeople" style="width: 100%">
            <el-table-column>
                <template #default="{ row }">
                    <router-link :to="`/person/${row.id}`">
                        <div class="thumbnail-container">
                            <img :src="getImageUrl(row.img)" alt="Person's photo" class="thumbnail" />
                        </div>
                    </router-link>
                </template>
            </el-table-column>
            <el-table-column prop="name" width="500">
                <template #default="{ row }">
                    <router-link :to="`/person/${row.id}`" class="person-name">
                        {{ row.name }} {{ row.surname }}
                    </router-link>
                </template>
            </el-table-column>
            <el-table-column width="200">
    <template #default="{ row }">
        <div class="vote-section">
            <router-link :to="`/person/${row.id}`">
            <el-link :underline="false"
                v-if="hasVoted(row.id) !== 'DOWNVOTE'"
                :type="hasVoted(row.id) === 'UPVOTE' ? 'success' : 'default'"
                :style="hasVoted(row.id) === 'UPVOTE' ? { color: 'rgb(49, 155, 49)' } : {}"
            >
                <Icon icon="mdi:arrow-up" style="font-size: 24px; color: rgb(49, 155, 49);" /> Upvoted
            </el-link>
            </router-link>
            <router-link :to="`/person/${row.id}`">
            <el-link :underline="false"
                v-if="hasVoted(row.id) !== 'UPVOTE'"
                :type="hasVoted(row.id) === 'DOWNVOTE' ? 'danger' : 'default'"
                :style="hasVoted(row.id) === 'DOWNVOTE' ? { color: 'rgb(255, 67, 67)' } : {}"
            >
                <Icon icon="mdi:arrow-down" style="font-size: 24px; color: rgb(255, 67, 67);" /> Downvoted
            </el-link>
            </router-link>
        </div>
    </template>
</el-table-column>

        </el-table>
        <el-empty v-else description="No voted celebrities yet" />

        <Pagination :currentPage="currentPage" :totalElements="totalElements"
            :itemsPerPage="itemsPerPage" @page-change="goToPage" />
    </div>
</template>

<script setup>
import { ref, computed, onMounted, watch } from 'vue';
import { useAuthStore } from '../stores/auth.store';
import axios from 'axios';
import Pagination from './Pagination.vue';
import { ElMessage } from 'element-plus';
import { Icon } from '@iconify/vue';

const votedPeople = ref([]);
const userVotes = ref({});
const authStore = useAuthStore();
const user = computed(() => authStore.user);
const currentPage = ref(1);
const itemsPerPage = ref(3);
const totalPages = ref(0);
const totalElements = ref(0);

const getImageUrl = (imageName) => `/api/image/${imageName}`;

const hasVoted = (personId) => {
    return userVotes.value[personId] || null;
};

const getVotedPeople = async () => {
    try {
        const response = await axios.get(`/api/persons/voted-by/${user.value.id}`);
        if (response.data && Array.isArray(response.data)) {
            totalElements.value = response.data.length;
            totalPages.value = Math.ceil(totalElements.value / itemsPerPage.value);
            const start = (currentPage.value - 1) * itemsPerPage.value;
            votedPeople.value = response.data.slice(start, start + itemsPerPage.value);

            await fetchUserVotes();
        } else {
            votedPeople.value = [];
        }
    } catch (error) {
        console.error("Error fetching voted people:", error);
        votedPeople.value = [];
    }
};

const fetchUserVotes = async () => {
    for (const person of votedPeople.value) {
        try {
            const response = await axios.get(`/api/person/${person.id}/vote/${user.value.id}`);
            if (response.data && response.data.hasVoted) {
                userVotes.value[person.id] = response.data.voteType;
            } else {
                userVotes.value[person.id] = null; 
            }
        } catch (error) {
            console.error(`Error fetching vote for person ${person.id}:`, error);
            userVotes.value[person.id] = null;
        }
    }
};

const goToPage = (page) => {
    if (page < 1 || page > totalPages.value) return;
    currentPage.value = page;
    getVotedPeople();
};

onMounted(() => {
    getVotedPeople();
});

watch(user, () => {
    currentPage.value = 1;
    getVotedPeople();
});
</script>

<style scoped>
h2 {
    color: #4b0082;
    margin: 0 0 1rem;
}

p {
  font-size: smaller;
  color: #6e6e6e;
}

.voted-people {
    margin-top: 1rem;
}

.voted-people h2 {
    color: #4b0082;
}

.el-table {
    margin-top: 1rem;
}

.thumbnail-container {
    display: flex;
    justify-content: center;
    align-items: center;
}

.thumbnail {
    width: 50px;
    height: 50px;
    object-fit: cover;
    border-radius: 50%;
    border: 1px solid #ddd;
}

.person-name {
    color: #5f5f5f;
    text-decoration: none;
    transition: color 0.3s ease;
}

.person-name:hover {
    color: #acacac;
}

.vote-section {
    margin-top: 10px;
    display: flex;
    justify-content: center;
    gap: 10px;
    align-items: center;
    width: 100%;
}

</style>
