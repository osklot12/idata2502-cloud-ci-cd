<script>
    import { login } from '../services/authService';
    import { isAuthenticated } from '../stores/authStore';

    let username = '';
    let password = '';
    let errorMessage = '';

    async function handleLogin() {
        const success = await login(username, password);
        if (!success) {
            errorMessage = 'Incorrect username or password.';
        } else {
            errorMessage = ''; // Clear any previous error messages on success
        }
    }
</script>

{#if !$isAuthenticated} <!-- Use `$isAuthenticated` for reactivity -->
    <form on:submit|preventDefault={handleLogin} class="std-form">
        <input type="text" class="std-form-element std-form-input" bind:value={username} placeholder="Username" required />
        <input type="password" class="std-form-element std-form-input" bind:value={password} placeholder="Password" required />
        <button type="submit" class="std-form-element std-form-btn">Login</button>
        {#if errorMessage}
            <p class="std-form-element std-form-hint">{errorMessage}</p>
        {/if}
    </form>
{:else}
    <p>You are logged in!</p>
{/if}
