<template>
    <div>
        <h3>Comments: {{ totalComments }}</h3>
        <el-link :underline="false" v-if="user" @click="toggleCommentForm" :type="showCommentForm ? 'info' : 'success'">
            <Icon :icon="showCommentForm ? 'formkit:arrowleft' : 'iconamoon:comment'" />
            {{ showCommentForm ? 'Cancel' : 'New comment' }}
        </el-link>

        <transition name="slide-up" mode="out-in">
            <CommentPersonForm v-if="showCommentForm && person" :person="person" @commentAdded="fetchComments" />
        </transition>

        <Skeleton v-if="loading" :rows="10" :animated="true" />
        <div v-else-if="error">{{ error }}</div>

        <div v-else-if="comments.length === 0">
            <el-empty description="No comments yet" />
        </div>

        <el-card v-for="comment in paginatedComments" :key="comment.id" class="comment-card">
            <el-row>
                <el-col :span="24">
                    <el-avatar :size="40" :src="getUserAvatar(comment)" class="user-avatar" />

                    <router-link :to="`/profile/${comment.userId}`" class="user-name">
                        {{ comment.userName }}
                    </router-link>
                    <span class="comment-date"> at {{ formatDate(comment.createdAt) }}</span>

                    <el-tooltip class="item" effect="dark" content="Reply to comment" placement="top">
                        <Icon icon="material-symbols:reply" v-if="user" size="mini"
                            @click="toggleReplyForm(comment.id, comment.userName)" class="reply-icon" />
                    </el-tooltip>

                    <el-tooltip class="item" effect="dark" content="Edit comment" placement="top">
                        <Icon icon="ri:edit-fill" v-if="user && user.id === comment.userId"
                            @click="editComment(comment)" class="edit-icon" />
                    </el-tooltip>

                    <el-tooltip class="item" effect="dark" content="Delete comment" placement="top">
                        <Icon icon="mdi:trash-outline" v-if="user && user.id === comment.userId"
                            @click="deleteComment(comment.id)" class="delete-icon" />
                    </el-tooltip>

                    <el-tooltip class="item" effect="dark" content="Report this comment" placement="top">
                        <Icon 
                            :icon="isReported(comment.id) ? 'stash:flag-duotone' : 'stash:flag'" 
                            :style="{ color: isReported(comment.id) ? 'rgb(255, 67, 67)' : 'gray', cursor: 'pointer' }" 
                            @click="toggleReport(comment.id)"
                            class="flag-icon"
                        />
                    </el-tooltip>
                </el-col>
            </el-row>

            <EditCommentPersonForm v-if="editingCommentId === comment.id" :comment="comment" @save="saveEditedComment"
                @cancel="cancelEdit" />

            <el-row v-else>
                <el-col :span="24">
                    <p>{{ comment.content }}</p>
                </el-col>
            </el-row>
            <SubmitReplyForm v-if="replyFormVisible === comment.id" :commentId="comment.id" :userId="user.id"
                :personId="person.id" :userName="comment.userName" @replySubmitted="fetchComments"
                @cancel="toggleReplyForm(comment.id)" />

            <div v-if="comment.replies && comment.replies.length > 0">
                <el-card v-for="reply in comment.replies" :key="reply.id" class="reply-card">
                    <el-row>
                        <el-col :span="24" class="reply-col">
                            <Icon icon="mage:l-arrow-down-right" class="reply-arrow-icon" />
                            <el-avatar :size="40" :src="getUserAvatar(comment)" class="user-avatar" />

                            <router-link :to="`/profile/${reply.userId}`" class="user-name">
                                {{ reply.userName }}
                            </router-link>
                            <span class="comment-date"> at {{ formatDate(reply.createdAt) }}</span>

                            <el-tooltip class="item" effect="dark" content="Reply Edit" placement="top">
                                <Icon icon="ri:edit-fill" v-if="user && user.id === reply.userId"
                                    @click="editReply(reply)" class="edit-icon" />
                            </el-tooltip>

                            <el-tooltip class="item" effect="dark" content="Delete reply" placement="top">
                                <Icon icon="mdi:trash-outline" v-if="user && user.id === reply.userId"
                                    @click="deleteComment(reply.id)" class="delete-icon" />
                            </el-tooltip>

                            <el-tooltip class="item" effect="dark" content="Report this comment" placement="top">
                        <Icon 
                            :icon="isReported(reply.id) ? 'stash:flag-duotone' : 'stash:flag'" 
                            :style="{ color: isReported(reply.id) ? 'rgb(255, 67, 67)' : 'gray', cursor: 'pointer' }" 
                            @click="toggleReport(reply.id)"
                            class="flag-icon"
                        />
                    </el-tooltip>
                        </el-col>
                    </el-row>

                    <EditCommentPersonForm v-if="editingReplyId === reply.id" :comment="reply" @save="saveEditedReply"
                        @cancel="cancelEdit" />

                    <el-row v-else>
                        <el-col :span="24">
                            <p>{{ reply.content }}</p>
                        </el-col>
                    </el-row>
                </el-card>
            </div>
        </el-card>

        <Pagination v-if="totalPages > 1" :current-page="currentPage" :total-elements="comments.length" :items-per-page="itemsPerPage"
            @page-change="goToPage" />
    </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue';
import axios from 'axios';
import { useAuthStore } from '@/stores/auth.store';
import { storeToRefs } from 'pinia';
import Pagination from '@/components/Pagination.vue';
import Skeleton from './Skeleton.vue';
import { ElMessage } from 'element-plus';
import CommentPersonForm from './CommentPersonForm.vue';
import EditCommentPersonForm from './EditCommentPersonForm.vue';
import { Icon } from '@iconify/vue';
import SubmitReplyForm from './SubmitReplyForm.vue';

const props = defineProps({
    personId: {
        type: Number,
        required: true
    },
    person: Object,
});

const showCommentForm = ref(false);
const authStore = useAuthStore();
const { user } = storeToRefs(authStore);
const comments = ref([]);
const loading = ref(true);
const error = ref(null);
const currentPage = ref(1);
const itemsPerPage = ref(15);
const totalPages = ref(0);
const editingCommentId = ref(null);
const editingReplyId = ref(null);
const replyContent = ref("");
const replyFormVisible = ref(null);
const person = ref(null);
const reports = ref({});

const defaultAvatar = 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQBsdZ-yU8qdxr0WrIA8HLa31tB_cCFZJt-2Q&s';
const getUserAvatar = (comment) => {
  return comment.userImg ? `/api/image/${comment.userImg}` : defaultAvatar;
};

const formatDate = (timestamp) => {
    if (!timestamp || timestamp.length < 6) return '-';
    const [year, month, day, hours, minutes, seconds] = timestamp;
    const padZero = (num) => (num < 10 ? `0${num}` : num);
    const months = ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sept', 'Oct', 'Nov', 'Dec'];
    return `${padZero(hours)}:${padZero(minutes)}:${padZero(seconds)}, ${padZero(day)} ${months[month - 1]} ${year}`;
};

const totalComments = computed(() => {
    return comments.value.reduce((total, comment) => {
        return total + (comment.replies ? comment.replies.length : 0) + 1;
    }, 0);
});

const fetchComments = async () => {
    try {
        const response = await axios.get(`/api/person/${props.personId}/comments`);
        comments.value = response.data;
        const mainComments = comments.value.filter(comment => !comment.parentCommentId);
        totalPages.value = Math.ceil(mainComments.length / itemsPerPage.value);
        comments.value = mainComments;

        const reportPromises = comments.value.map(async (comment) => {
            try {
                const reportResponse = await axios.get(`/api/comment-reports/${comment.id}/user/${user.value.id}/exists`);
                reports.value[comment.id] = reportResponse.data;
            } catch (err) {
                console.error(`Error checking report for comment ${comment.id}:`, err);
            }
        });
        await Promise.all(reportPromises);

    } catch (err) {
        error.value = 'Failed to fetch comments. Please try again later.';
        console.error('Error fetching comments:', err);
    } finally {
        loading.value = false;
    }
};

const toggleReport = async (commentId) => {
    if (!user.value) {
        ElMessage.error('Please log in to report a comment.');
        return;
    }

    if (isReported(commentId)) return;

    try {
        await axios.post(`/api/comment-reports/${commentId}/user/${user.value.id}`);
        reports.value[commentId] = true;
        ElMessage.success('Comment reported successfully!');
    } catch (err) {
        console.error('Error reporting comment:', err);
        ElMessage.error('Failed to report comment.');
    }
};

const isReported = (commentId) => reports.value[commentId] || false;
const fetchPerson = async () => {
    try {
        const response = await axios.get(`/api/person/${props.personId}`);
        person.value = response.data;
    } catch (err) {
        console.error("Error fetching person data:", err);
        error.value = "Failed to load person data.";
    }
};

const editComment = (comment) => {
    editingCommentId.value = comment.id;
};

const saveEditedComment = (editedContent) => {
    const updatedComment = comments.value.find(c => c.id === editingCommentId.value);
    if (updatedComment) {
        updatedComment.content = editedContent;
    }
    cancelEdit();
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

const editReply = (reply) => {
    editingReplyId.value = reply.id;
};

const saveEditedReply = (editedContent) => {
    const updatedReply = comments.value
        .flatMap(comment => comment.replies)
        .find(reply => reply.id === editingReplyId.value);

    if (updatedReply) {
        updatedReply.content = editedContent;
    }
    cancelEdit();
};

const cancelEdit = () => {
    editingCommentId.value = null;
    editingReplyId.value = null;
};

const toggleReplyForm = (commentId, userName) => {
    if (replyFormVisible.value === commentId) {
        replyFormVisible.value = null; 
        replyContent.value = ""; 
    } else {
        replyFormVisible.value = commentId; 
        replyContent.value = `@${userName} `;
    }
};

const paginatedComments = computed(() => {
    const start = (currentPage.value - 1) * itemsPerPage.value;
    const end = start + itemsPerPage.value;
    return comments.value.slice(start, end);
});

const goToPage = (page) => {
    if (page < 1 || page > totalPages.value) return;
    currentPage.value = page;
};

const toggleCommentForm = () => {
    user.value ? showCommentForm.value = !showCommentForm.value : showLoginModal();
};

onMounted(async () => {
    await fetchPerson();
    if (person.value) {
        await fetchComments();
    }
});
</script>
<style scoped>
h3 {
    margin: 0 0 10px;
    color: #333;
}

p {
    margin: 10px 0;
}

.el-link {
    font-size: medium;
}

.comment-date {
    font-size: 0.85em;
    color: #888;
    margin-left: 10px;
    vertical-align: middle;
    flex-grow: 1;
}

.comment-card {
    margin: 10px 0 -5px;
    box-shadow: none !important;
    border-radius: 5px;
    position: relative;
}

.reply-card {
    margin-top: 10px;
    margin-left: 30px;
    padding: 10px;
    background-color: #f9f9f9;
    border-radius: 8px;
    box-shadow: none !important;
}

.edit-icon,
.delete-icon,
.reply-icon, .flag-icon {
    cursor: pointer;
    margin-left: 10px;
    font-size: large;
    outline: none;
}

.delete-icon {
    color: #ff4d4f;
}

.reply-icon {
    color: #69c443;
}

.edit-icon {
    color: #888;
    font-size: medium;
}

.flag-icon:disabled {
  outline: none;
  cursor: not-allowed;
}

.el-input[type='textarea'] {
    margin-bottom: 10px;
}

.comment-card {
    margin: 20px 0 -5px;
    box-shadow: none !important;
    border-radius: 15px;
    position: relative;
}

.reply-card {
    margin-top: 10px;
    margin-left: 80px;
    padding: 0px;
    background-color: #fcfcfc;
    border-radius: 8px;
    box-shadow: none !important;
}

.user-name:hover {
    color: #9b4dca;
}

.el-row {
    display: flex;
    justify-content: space-between;
    align-items: center;
    width: 100%;
}

.el-col {
    display: flex;
    align-items: center;
    width: 100%;
}

.user-name {
    font-weight: bold;
    display: inline-block;
    margin-bottom: 0;
    text-decoration: none;
    color: #333;
    transition: color 0.3s ease;
    margin-left: 5px;
}

.comment-date {
    font-size: 0.85em;
    color: #888;
    margin-left: 10px;
    vertical-align: middle;
}

.el-tooltip {
    font-size: 12px;
    margin-left: 10px;
}

.user-avatar {
    vertical-align: middle;
}

.reply-card .reply-col {
    display: flex;
    align-items: center;
}

.reply-arrow-icon {
    margin-right: 10px;
    color: #888; 
    font-size: 20px; 
}
</style>