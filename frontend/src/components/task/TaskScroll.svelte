<script>
    import { onMount } from 'svelte';
    import Task from './Task.svelte';
    import TaskModel from '../../classes/Task.js';
    import { getTasks } from '../../services/api/api.js';

    export let title = "Task List";
    export let tasks = [];

    async function fetchTasks() {
        try {
            const fetchedTasks = await getTasks(); // Fetch tasks and set them to the tasks array
            tasks = fetchedTasks.map(taskData => new TaskModel(taskData))
        } catch (error) {
            console.error('Error fetching tasks:', error);
            // Optional: display an error message to the user
        }
    }

    onMount(fetchTasks); // Fetch tasks when component mounts
</script>

<div class="task-scroll">
    <div class="task-scroll-header">
        <h3 class="task-scroll-title">
            {title}
        </h3>
        <div>

        </div>
    </div>
    <div class="task-scroll-list">
        {#each tasks as task}
            <Task {task}/>
        {/each}
    </div>
</div>

<style>
    .task-scroll {
        color: var(--text-color);
    }

    .task-scroll-header {
        font-size: 1.3rem;
        padding: 5px;
    }

    .task-scroll-title {
        font-weight: bold;
        margin: 0;
        border-bottom: 1px solid var(--secondary-text-color);
    }

    .task-scroll-list {
        display: flex;
        flex-direction: column;
    }
</style>