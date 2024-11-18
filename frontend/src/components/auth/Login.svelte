<script>
    import { login } from "../../services/api/api.js"
    import { isAuthenticated } from "../../stores/authStore.js";

    let username = '';
    let password = '';
    let errorMessage = '';

    async function handleLogin() {
        const result = await login(username, password);
        if (result.success) {
            isAuthenticated.set(true);
            window.location.hash = '#/tasks'; // Redirect to task list after successful login
        } else {
            errorMessage = result.message; // Display the specific error message from the backend
        }
    }
</script>


<form on:submit|preventDefault={handleLogin} class="std-form">
    <input type="text" class="std-form-element std-form-input" bind:value={username} placeholder="Username" required/>
    <input type="password" class="std-form-element std-form-input" bind:value={password} placeholder="Password"
           required/>
    <button type="submit" class="std-form-element std-form-btn">Login</button>
    {#if errorMessage}
        <p class="std-form-element std-form-hint">{errorMessage}</p>
    {/if}
</form>
<p class="subtle-info">
    Not a user?
    <a href="#/auth/register">Register here</a>
</p>

