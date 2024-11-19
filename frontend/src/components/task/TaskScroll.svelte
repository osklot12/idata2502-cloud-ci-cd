<script>
    import {tasks} from "../../stores/taskStore.js";
    import { onMount } from 'svelte';
    import Task from './Task.svelte';
    import TaskModel from '../../classes/Task.js';
    import {api} from "../../main.js";

    $: taskList = $tasks;

    async function fetchTasks() {
        try {
            const fetchedTasks = await api.getTasks(); // Fetch tasks from the backend
            tasks.set(fetchedTasks.map(taskData => new TaskModel(taskData))); // Update the shared store
        } catch (error) {
            console.error('Error fetching tasks:', error);
            // Optional: Display an error message to the user
        }
    }

    onMount(fetchTasks); // Fetch tasks when component mounts
</script>

<div class="task-scroll">
    <div class="task-scroll-list">
        {#each taskList as task}
            <Task {task}/>
        {/each}
    </div>
</div>

<style>
    .task-scroll {
        color: var(--text-color);
        display: flex;
        flex-direction: column;
        flex: 1;
        align-items: center;


        padding: 50px;
    }

    .task-scroll-list {
        display: flex;
        flex-direction: column;
        align-items: center;
        flex: 1;
        width: 100%;

        max-width: 600px;
        gap: 20px;
    }
</style>