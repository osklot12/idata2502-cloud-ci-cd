import TaskList from './components/TaskList.svelte';
import LoginPage from './components/LoginPage.svelte';
import RegisterPage from './components/RegisterPage.svelte';

export const routes = {
    '/auth/login': LoginPage,
    '/auth/register': RegisterPage,
    '/tasks': TaskList
};
