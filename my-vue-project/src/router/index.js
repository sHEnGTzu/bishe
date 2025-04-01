import { createRouter, createWebHistory } from 'vue-router';
import LoginView from '../views/LoginView.vue';
import RegisterView from '../views/RegisterView.vue';
import LiteratureListView from '../views/LiteratureListView.vue';
import LiteratureDetailView from '../views/LiteratureDetailView.vue';

const routes = [
    {
        path: '/login',
        name: 'Login',
        component: LoginView
    },
    {
        path: '/register',
        name: 'Register',
        component: RegisterView
    },
    {
        path: '/literature',
        name: 'LiteratureList',
        component: LiteratureListView
    },
    {
        path: '/literature/:id',
        name: 'LiteratureDetail',
        component: LiteratureDetailView
    }
];

const router = createRouter({
    history: createWebHistory(),
    routes
});

export default router;