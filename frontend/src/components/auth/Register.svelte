<script>
    import {api} from "../../main.js";
    import { isAuthenticated } from "../../stores/authStore.js";

    let username = '';
    let email = '';
    let password = '';
    let errorMessage = '';

    async function handleRegister() {
        try {
            const result = await api.register(username, email, password);
            isAuthenticated.set(true);
            window.location.hash = "#/tasks";
        } catch (e) {
            errorMessage = e.message;
        }
    }
</script>


<form on:submit|preventDefault={handleRegister} class="std-form">
    <input type="text" class="std-form-element std-form-input" bind:value={username} placeholder="Username" required/>
    <input type="text" class="std-form-element std-form-input" bind:value={email} placeholder="Email" required/>
    <input type="password" class="std-form-element std-form-input" bind:value={password} placeholder="Password"
           required/>
    <button type="submit" class="std-form-element std-form-btn">Register</button>
    {#if errorMessage}
        <p class="std-form-element std-form-hint">{errorMessage}</p>
    {/if}
</form>
<p class="subtle-info">
    Already a user?
    <a href="#/auth/login">Login here</a>
</p>

