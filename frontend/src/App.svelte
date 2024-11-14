<script>
  import Router from 'svelte-spa-router';
  import { routes } from './routes';
  import { isAuthenticated } from './stores/authStore';

  $: if ($isAuthenticated) {
    // redirect to /tasks if authenticated
    window.location.hash = '#/tasks';
  } else {
    // redirect to login if not authenticated
    window.location.hash = '#/auth/login';
  }
</script>

<main>
  <Router {routes} />
</main>

