<template>
    <div>
        <h3>Leave a comment</h3>
        <div class="rating-form">
            <el-input class="comment-input" type="textarea" v-model="commentContent"
                placeholder="Write your comment here..." rows="4" />
            <div class="modal-footer">
                <el-tooltip content="Submit comment" placement="top">
                    <Icon icon='solar:map-arrow-right-bold-duotone' class="submit-btn" @click="submitComment" />
                </el-tooltip>
            </div>
        </div>
    </div>
</template>

<script setup>
import { ref } from 'vue';
import axios from 'axios';
import { ElMessage } from 'element-plus';
import { useAuthStore } from '@/stores/auth.store';
import { storeToRefs } from 'pinia';
import { Icon } from '@iconify/vue';

const props = defineProps({
    person: Object
});

const emit = defineEmits(['commentAdded']);

const commentContent = ref('');
const { user } = storeToRefs(useAuthStore());

const submitComment = async () => {
    if (!props.person || !props.person.id) {
        ElMessage.error('Person data is missing!');
        return;
    }

    if (!user.value || !user.value.id) {
        ElMessage.error('You must be logged in to comment!');
        return;
    }

    if (!commentContent.value.trim()) {
        ElMessage.error('Please write a comment before submitting!');
        return;
    }

    try {
        await axios.post(`/api/person/${props.person.id}/comment/${user.value.id}`, {
            content: commentContent.value
        });

        ElMessage.success('Comment submitted successfully!');
        commentContent.value = '';

        emit('commentAdded');
    } catch (error) {
        console.error('Error submitting comment:', error);
        ElMessage.error('An error occurred while submitting your comment.');
    }
};
</script>

<style scoped>
.rating-form {
    margin-top: 20px;
    max-width: 900px;
    background-color: #f7f7f7;
    padding: 10px;
    border-radius: 8px;
    box-shadow: none;
    display: flex;
    flex-direction: column;
    align-items: flex-start;
}

.modal-footer {
    display: flex;
    justify-content: flex-end;
    width: 100%;
    margin-top: 10px;
}

.submit-btn {
    width: auto; 
    font-size: 25px;
    cursor: pointer;
    margin-right: 1px;
    color: #4b0082;
}

.submit-btn:hover {
    color: #555;
}

.comment-input {
    width: 100%;
    max-width: 900px;
    margin: 0 auto;
    border-radius: 15px;
}
</style>

