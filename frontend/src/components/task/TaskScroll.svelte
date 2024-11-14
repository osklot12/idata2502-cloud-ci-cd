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

<div>
    <div>
        <h3>
            {title}
        </h3>
        <div>

        </div>
    </div>
    <div>
        {#each tasks as task}
            <Task {task}/>
        {/each}
    </div>
</div>