import { defineStore } from 'pinia';

import axios from 'axios';



export const useAuthStore = defineStore({
    id: 'auth',
    state: () => ({

        user: JSON.parse(localStorage.getItem('user')),
        returnUrl: null
    }),
    actions: {
        async login(email, password) {
            let user = null;
            try {
                const response = await axios.post('/api/login', { email, password });

                user = response.data;

                localStorage.setItem('user', JSON.stringify(user)); 

                axios.defaults.headers['Authorization'] = `Bearer ${user.token}`;
        
            } catch (err) {
                throw err;
            }
        
            this.user = user; 
        },
        logout() {
            this.user = null;
            localStorage.removeItem('user');
            delete axios.defaults.headers['Authorization'];
        },
        async register(email, password, name, passwordConfirmation, role) {
            try {
                const response = await axios.post('/api/register', {
                    email,
                    password,
                    name,
                    passwordConfirmation,
                    role
                });
                console.log('Register response:', response);
            } catch (err) {
                console.error('Error during registration:', err.response ? err.response.data : err); 
                throw err;
            }
        },
  }
  
});