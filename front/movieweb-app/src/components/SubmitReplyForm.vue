<template>
    <el-row>
        <el-col :span="24">
            <textarea v-model="replyContent" class="seamless-textarea" rows="3" placeholder="Write your reply..." />

            <div class="modal-footer">
                <el-link :underline="false" @click="submitReply" class="submit-button">
                    <Icon icon="mdi:check" width="24" height="24" class="icon" />
                    Submit
                </el-link>

                <el-link :underline="false" @click="cancelReply" class="cancel-button">
                    <Icon icon="mdi:close" width="24" height="24" class="icon" />
                    Cancel
                </el-link>
            </div>
        </el-col>
    </el-row>
</template>

<script setup>
import { ref } from 'vue';
import { ElMessage } from 'element-plus';
import { Icon } from '@iconify/vue';
import axios from 'axios';

const props = defineProps({
    commentId: {
        type: Number,
        required: true
    },
    userId: {
        type: Number,
        required: true
    },
    personId: {
        type: Number,
        required: true
    },
    userName: {
        type: String, 
        required: true
    },
    onReplySubmitted: {
        type: Function,
        required: true
    },
    onCancel: {
        type: Function,
        required: true
    }
});

const replyContent = ref(`@${props.userName} `);

const submitReply = async () => {
    if (!replyContent.value.trim()) {
        ElMessage.error('Please enter a reply!');
        return;
    }

    try {
        await axios.post(`/api/comment/${props.commentId}/reply/${props.userId}?personId=${props.personId}`, {
            content: replyContent.value
        });
        ElMessage.success("Reply added successfully!");
        props.onReplySubmitted(replyContent.value);

        cancelReply();
    } catch (err) {
        ElMessage.error('Failed to submit reply.');
        console.error('Error submitting reply:', err);
    }
};

const cancelReply = () => {
    props.onCancel();
};
</script>
<style scoped>
.seamless-textarea {
    width: 100%;
    margin-top: 15px;
    border: none;
    outline: none;
    background: transparent;
    font: inherit;
    color: inherit;
    padding: 0;
    resize: none;
}

.seamless-textarea::placeholder {
    color: inherit;
    opacity: 0.6;
}

.modal-footer {
    margin-top: 0px;
    margin-right: 10px;
    text-align: right;
}

.submit-button,
.cancel-button {
    font-weight: 400;
    font-size: large;
    margin: 0px 10px 10px 0;
    display: inline-flex;
    align-items: center;
}

.submit-button {
    color: #4CAF50;
}

.cancel-button {
    color: #f44336;
}

.submit-button:hover {
    color: #79e37d;
}

.cancel-button:hover {
    color: #ff7267;
}

.icon {
    margin-right: 10px;
}
</style>
