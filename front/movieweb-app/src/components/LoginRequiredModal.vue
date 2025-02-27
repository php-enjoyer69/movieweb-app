<template>
    <div v-if="visible" class="modal-overlay" @click="close">
        <div class="modal-content" @click.stop>
            <Icon icon="mdi:close" class="close" @click="close" />
            <h1 style="color:#4b0082">Wait!</h1>
            <h3>You need to be logged in to perform this action</h3>
            <p style="margin-top: -5px;">Please log in or sign up to continue.</p>
            <div class="modal-footer">
                <el-button type="primary" @click="goToLogin">
                    <Icon icon="material-symbols:login-rounded" style="font-size: 20px; margin-right: 8px; color: #9b4dca;" /> Log In
                </el-button>
                <el-button type="success" @click="goToRegister">Sign Up</el-button>
            </div>
        </div>
    </div>
</template>

<script setup>
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import { Icon } from '@iconify/vue';

const props = defineProps({
    visible: Boolean,
});

const emits = defineEmits(['close']);

const router = useRouter();

const close = () => {
    emits('close');
};

const goToLogin = () => {
    router.push('/login');
    close();
};

const goToRegister = () => {
    router.push('/register');
    close();
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
    margin-top: 60px;
    text-align: right;
}

.modal-footer el-button {
    margin-left: 10px;
}
</style>