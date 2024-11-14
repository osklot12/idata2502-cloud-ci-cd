<script>
  import TaskList from './components/TaskList.svelte';
  import AuthPage from './components/AuthPage.svelte';

  import { isAuthenticated } from './stores/authStore';

  // Log to confirm reactive updates
  $: console.log("Auth status in App.svelte:", $isAuthenticated);
</script>

<main>
  {#if $isAuthenticated}
    <TaskList />
  {:else}
    <AuthPage />
  {/if}
</main>
