<template>
    <div class="modal-overlay" @click="close">
        <div class="modal-content" @click.stop>
            <Icon icon="mdi:close" class="close" @click="close" />
            <h1 style="color:#4b0082">Rate this movie</h1>

            <h3>Your score:</h3>
            <!-- <el-rate v-model="rating" :colors="colors" allow-half show-text clearable /> -->
            <el-input-number v-model="rating" :min="1" :max="10"/>

            <p style="margin-top: -5px;">Write a review (optional)</p>
            <el-input type="textarea" v-model="content" placeholder="Write your review here..." rows="4" />

            <div class="modal-footer">
                <el-button type="primary" @click="submitReview">
                    Submit
                </el-button>
            </div>
        </div>
    </div>
</template>

<script setup>
import { ref } from 'vue';
import axios from 'axios';
import { useAuthStore } from '@/stores/auth.store';
import { Icon } from '@iconify/vue';
import { ElMessage } from 'element-plus';

const props = defineProps({
    visible: Boolean,
    movieId: Number,
});

const visible = ref(false);
const rating = ref(0);
const content = ref('');
const authStore = useAuthStore();
const userId = ref(authStore.user ? authStore.user.id : null);

const emits = defineEmits(['close']);

const close = () => {
    emits('close');
};

const submitReview = async () => {
    if (rating.value === 0) {
        alert('Please provide a rating!');
        return;
    }

    try {
        const response = await axios.post(
            `http://127.0.0.1:8000/movie/${props.movieId}/review/${userId.value}`,
            {
                rating: rating.value,
                content: content.value,
            },
            {
                headers: {
                    Authorization: `Bearer ${authStore.token}`, 
                },
            }
        );

        if (response.status === 200) {
            ElMessage({
            message: 'You have successfully rated the movie',
            type: 'success',
            plain: true,
            duration: 1500
        });
        } else {
            alert('Failed to submit review');
        }
    } catch (error) {
        console.error('Error submitting review:', error);
        alert('An error occurred. Please try again.');
    }
};

</script>

<style scoped>
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
    width: 550px;
    z-index: 1000;
    display: flex;
    flex-direction: column;
    align-items: stretch;
    justify-content: flex-start;
    position: relative;
}

.modal-footer {
    margin-top: 20px;
    text-align: right;
}

.modal-footer el-button {
    margin-left: 10px;
}
</style>