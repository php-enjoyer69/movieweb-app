<template>
    <div class="register-container">
        <h2 class="register-title">Create an account</h2>
        <el-form @submit.native.prevent="handleRegister" :model="form" ref="registerForm" label-position="top">
            <el-form-item label="Email" prop="email" :error="error?.email" :show-message="error?.email ? true : false"
                class="register-form-item">
                <el-input v-model="form.email" class="register-input" placeholder="Your email" />
            </el-form-item>
            <el-form-item label="Name" prop="username" :error="error?.username" :show-message="error?.username ? true : false"
                class="register-form-item">
                <el-input v-model="form.username" class="register-input" placeholder="Your username" />
            </el-form-item>
            <el-form-item label="Password" prop="password" :error="error?.password"
                :show-message="error?.password ? true : false" class="register-form-item">
                <el-input v-model="form.password" class="register-input" type="password" placeholder="Password"
                    show-password />
            </el-form-item>
            <el-form-item label="Confirm password" prop="passwordConfirmation" :error="error?.passwordConfirmation"
                :show-message="error?.passwordConfirmation ? true : false" class="register-form-item">
                <el-input v-model="form.passwordConfirmation" class="register-input" type="password"
                    placeholder="Confirm password" show-password />
            </el-form-item>
            <el-form-item>
                <el-button type="primary" native-type="submit" class="register-button">Sign up</el-button>
            </el-form-item>
            <el-form-item>
                <router-link to="/" class="register-button">
                    <el-button type="info" class="cancel-button">Cancel</el-button>
                </router-link>
            </el-form-item>
        </el-form>

        <div v-if="loading" class="loading-indicator">Ładowanie...</div>
    </div>
</template>

<script setup>
import { ref } from 'vue';
import { useAuthStore } from '@/stores/auth.store';
import { useRouter } from 'vue-router';
import { ElMessage } from 'element-plus'

const router = useRouter();
const authStore = useAuthStore();
const form = ref({
    email: '',
    password: '',
    username: '',
    passwordConfirmation: ''
});
const loading = ref(false);
const error = ref(null);
const userdata = ref(null);
const success = () => {
  ElMessage({
    message: 'Registered successfully',
    type: 'success',
  })
}

const handleRegister = async () => {
    loading.value = true;
    error.value = null;

    try {
        await authStore.register(form.value.email, form.value.password, form.value.username, form.value.passwordConfirmation);
        userdata.value = authStore.user;
        success();
        form.value.email = "";
        form.value.password = "";
        form.value.username = '';
        form.value.passwordConfirmation = '';
    } catch (err) {
        error.value = err.response.data.errors;
    } finally {
        loading.value = false;
    }
};
</script>

<style scoped>
.register-container {
    max-width: 400px;
    margin: 50px auto;
    padding: 20px;
    border: 1px solid #dcdfe6;
    border-radius: 8px;
    box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
    background-color: #ffffff;
}

.register-title {
    text-align: center;
    margin-bottom: 20px;
}

.register-input {
    width: 100%;
}

.register-button {
    width: 100%;
}

.loading-indicator {
    text-align: center;
    margin-top: 15px;
}

.error-message {
    color: red;
    text-align: center;
    margin-top: 15px;
}

.user-info {
    margin-top: 15px;
    list-style-type: none;
    padding: 0;
}

.cancel-button {
    width: 100%;
    color: #4caf50;
    border: 1px solid #4caf50;
    background-color: transparent;
    font-weight: 600;
}

.cancel-button:hover {
    background-color: #e8f5e9;
    color: #4caf50;
    border-color: #4caf50;
}

.loading-indicator {
    text-align: center;
    margin-top: 15px;
}


.register-form-item {
    margin-bottom: 30px;
}
</style>
