<script>
    import AssigneeHandler from "./AssigneeHandler.svelte";
    import {api} from "../../main.js";
    import {formatDateToStr} from "../../utils/formatter.js"
    import {tasks} from "../../stores/taskStore.js";
    import User from "../../classes/User.js";
    import Task from "../../classes/Task.js";

    let task = new Task();
    let errorMessage = "";

    async function addAssignee(username) {
        try {
            const userData = await api.getUserByUsername(username);
            const newUser = new User(userData);
            if (!task.assignees.some(assignee => assignee.id === newUser.id)) {
                task.assignees = [...task.assignees, newUser];
            } else {
                errorMessage = `User "${username}" is already an assignee.`;
            }
        } catch (error) {
            errorMessage = error.message || `Failed to add user "${username}".`;
            console.error(errorMessage);
        }
    }

    function removeAssignee(assigneeToRemove) {
        task.assignees = task.assignees.filter(assignee => assignee.id !== assigneeToRemove.id);
    }

    async function handleSubmit(event) {
        event.preventDefault();
        errorMessage = "";

        try {
            const newTaskPayload = {
                header: task.header,
                description: task.description,
                status: task.status,
                deadline: formatDateToStr(task.deadline),
                assigneeIds: task.assignees.map(assignee => assignee.id),
            };

            const response = await api.createTask(newTaskPayload);

            const createdTask = new Task(response);

            // Add the new task to the task list
            tasks.update((currentTasks) => [...currentTasks, createdTask]);

            // Reset the form or navigate to another page on success
            task = new Task(); // Clear the form


        } catch (error) {
            errorMessage = error.message || "Failed to create the task.";
            console.error(errorMessage);
        }
    }
</script>

<form class="std-form" on:submit={handleSubmit}>
    <input
            type="text"
            class="std-form-element std-form-input"
            placeholder="header"
            bind:value={task.header}
            required
    />
    <textarea
            class="std-form-element std-text-area"
            placeholder="description"
            bind:value={task.description}
    ></textarea>
    <label class="std-form-element task-creator-deadline-wrap">
        Due
        <input
                type="date"
                class="std-form-datepicker"
                value={formatDateToStr(new Date())}
                on:change={(e) => task.deadline = new Date(e.target.value)}
        />
    </label>
    <label class="std-form-element task-creator-assignees-wrap">
        Assignees
        <AssigneeHandler
                assignees={task.assignees}
                canEdit={true}
                onAdd={addAssignee}
                onRemove={removeAssignee}
        />
    </label>
    <div class="task-creator-options-wrap">
        <button type="submit" class="std-form-btn task-creator-option">
            Create new task
        </button>
    </div>
    {#if errorMessage}
        <p class="std-form-element error-message">{errorMessage}</p>
    {/if}
</form>

<style>
    .task-creator-deadline-wrap {
        display: flex;
        flex-direction: row;
        align-items: center;
        justify-content: space-around;
        color: var(--secondary-text-color);
    }

    .task-creator-assignees-wrap {
        display: flex;
        flex-direction: column;
        justify-content: center;
        text-align: center;
        color: var(--secondary-text-color);
    }

    .task-creator-options-wrap {
        display: flex;
        flex-direction: row;
        justify-content: right;
    }

    .task-creator-option {
        padding: 10px;
        border: none;
        font-size: 1rem;
    }

    .error-message {
        text-align: center;
    }
</style>