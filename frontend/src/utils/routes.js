import TaskPage from '../components/task/TaskPage.svelte'
import LoginPage from '../components/auth/LoginPage.svelte';
import RegisterPage from '../components/auth/RegisterPage.svelte';

export const routes = {
    '/auth/login': LoginPage,
    '/auth/register': RegisterPage,
    '/tasks': TaskPage
};
