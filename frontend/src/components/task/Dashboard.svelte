<script>
    import TaskCreator from "./TaskCreator.svelte";
    import {username} from "../../stores/authStore.js";

    let greetings = "";

    function getGreeting() {
        const currentHour = new Date().getHours();
        if (currentHour < 12) {
            return "Good morning";
        } else if (currentHour < 18) {
            return "Good afternoon";
        } else {
            return "Good evening";
        }
    }

    $: greeting = `${getGreeting()}, ${$username || "Guest"}`;
</script>

<div class="dashboard">
    <div class="greeting-wrap">
        <h1>
            {greeting}
        </h1>
    </div>
    <TaskCreator/>
</div>

<style>
    .dashboard {
        display: flex;
        flex-direction: column;
        align-items: center;
        padding: 5rem;
        gap: 5rem;
        color: white;
    }
</style>