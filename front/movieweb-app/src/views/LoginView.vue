<template>
    <div class="login-container">
        <h2 class="login-title">Log in</h2>
        <el-form @submit.native.prevent="handleLogin" :model="form" ref="loginForm" label-position="top">
            <el-form-item label="Email" prop="email" :error="error?.email" :show-message="error?.email ? true : false">
                <el-input v-model="form.email" class="login-input" placeholder="Email" />
            </el-form-item>
            <el-form-item label="Password" prop="password" :error="error?.password"
                :show-message="error?.password ? true : false">
                <el-input v-model="form.password" class="login-input" type="password" placeholder="Password"
                    show-password />
            </el-form-item>
            <el-form-item>
                <el-button type="primary" native-type="submit" class="login-button" :disabled="loading"
                    :class="{ 'is-loading': loading }">
                    Log in
                </el-button>
            </el-form-item>
            <el-form-item>
                <router-link to="/" class="login-button">
                    <el-button type="default" class="cancel-button">Cancel</el-button>
                </router-link>
            </el-form-item>
        </el-form>

        <div v-if="loading" class="loading-indicator">Loading...</div>
    </div>
</template>

<script setup>
import { ref } from 'vue';
import { useAuthStore } from '@/stores/auth.store';
import { useRouter } from 'vue-router';
import { ElMessage } from 'element-plus';

const router = useRouter();
const authStore = useAuthStore();
const form = ref({
    email: '',
    password: ''
});
const loading = ref(false);
const error = ref(null);
const userdata = ref(null);

const handleLogin = async () => {
    loading.value = true;
    error.value = null;

    try {
        await authStore.login(form.value.email, form.value.password);
        userdata.value = authStore.user;
        
        ElMessage({
            message: 'You have successfully logged in!',
            type: 'success',
            plain: true,
            duration: 1500
        });

        router.push('/admin');
    } catch (err) {
        error.value = err.response.data.errors;
    } finally {
        loading.value = false;
    }
};
</script>

<style scoped>
.login-container {
    max-width: 400px;
    margin: 50px auto;
    padding: 20px;
    border: 1px solid #dcdfe6;
    border-radius: 8px;
    box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
    background-color: #ffffff;
}

.login-title {
    text-align: center;
    margin-bottom: 20px;
}

.login-input {
    width: 100%;
    margin-bottom: 15px;
}

.login-button {
    width: 100%;
}

.login-button.is-loading {
    background-color: #ccc;
    border-color: #ccc;
    cursor: not-allowed;
}

.cancel-button {
    width: 100%;
    color: #4b0082; 
    border: 1px solid #3d0066; 
    background-color: transparent;
    font-weight: 600;
}

.cancel-button:hover {
    background-color: #f7e8ff; 
    color: #4b0082; 
    border-color: #4b0082; 
}

.loading-indicator {
    text-align: center;
    margin-top: 15px;
}
</style>
