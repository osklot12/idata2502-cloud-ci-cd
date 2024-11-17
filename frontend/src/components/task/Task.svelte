<script>
    import {userId} from "../../stores/authStore.js";
    import {updateTask} from "../../services/api/api.js";
    import Task from "../../classes/Task.js";

    export let task;

    let isMinimized = true;
    let isEditing = false;
    let currentUserId = $userId;

    let updatedHeader = task.header;
    let updatedDescription = task.description;
    let updatedDeadline = task.deadline
        ? task.deadline.toISOString().split("T")[0]
        : "";

    let isLoading = false;
    let errorMessage = "";

    function toggleMinimize() {
        isMinimized = !isMinimized;
    }

    function toggleEditMode() {
        isEditing = !isEditing;

        if (!isEditing) {
            updatedHeader = task.header;
            updatedDescription = task.description;
            updatedDeadline = task.deadline
                ? task.deadline.toISOString().split("T")[0]
                : "";
            errorMessage = "";
        }
    }

    async function saveTask() {
        isLoading = true;
        errorMessage = "";

        try {
            const updatedTask = {
                ...task,
                header: updatedHeader,
                description: updatedDescription,
                deadline: updatedDeadline
            };

            console.log("Updated Task Payload:", updatedTask);
            const response = await updateTask(updatedTask);

            task = new Task(response);

            isEditing = false;
        } catch (error) {
            errorMessage = error.message || "Failed to update the task.";
            console.error("Error updating task:", error);
        } finally {
            isLoading = false;
        }
    }
</script>

<div class="task-container">
    <div class="task-header">
        {#if isEditing}
            <input
                    type="text"
                    bind:value={updatedHeader}
                    class="task-title-input"
            />
        {:else}
            {task.header}
        {/if}
        <div class="task-header-options">
            {#if String(task.creator.id) === String(currentUserId)}
                {#if isEditing}
                    <button class="task-btn" on:click={saveTask} disabled={isLoading}>
                        <span class="material-symbols-outlined">check</span>
                    </button>
                {/if}
                <button class="task-btn" on:click={toggleEditMode}>
                        <span class="material-symbols-outlined">
                            { isEditing ? "close" : "edit" }
                        </span>
                </button>
                <button class="task-btn">
                    <span class="material-symbols-outlined">delete</span>
                </button>
            {/if}
            <button class="task-btn" on:click={toggleMinimize}>
                <span class="material-symbols-outlined">{isMinimized ? "expand_content" : "collapse_content"}</span>
            </button>
        </div>
    </div>
    {#if !isMinimized}
        {#if isEditing}
            <textarea
                    bind:value={updatedDescription}
                    class="task-description-input"
            ></textarea>
        {:else}
            <p class="task-description">
                {task.description}
            </p>
        {/if}
        <div class="task-deadline-wrap">
            <span class="material-symbols-outlined deadline-icon">
                pending_actions
            </span>
            {#if isEditing}
                <input
                        type="date"
                        bind:value={updatedDeadline}
                        class="task-deadline-input"
                />
            {:else}
                <p class="task-deadline">
                    {task.getFormattedDeadline()}
                </p>
            {/if}
        </div>

        <div class="task-created-info">
            <p class="task-meta-text">
                Created by
            </p>
            <div class="task-creator">
                {task.getCreatorName()}
            </div>
            <p class="task-meta-text">
                on
            </p>
            <p class="task-created-at">
                {task.getFormattedCreatedAt()}
            </p>
        </div>
        <div class="task-assignees-wrap">
            {#each task.assignees as assignee}
                <div class="task-assignee">
                    {assignee.getDisplayName()}
                </div>
            {/each}
        </div>
        {#if errorMessage}
            <p class="error-message">{errorMessage}</p>
        {/if}
    {/if}
</div>

<style>
    .task-container {
        padding: 5px;
        color: var(--text-color);
        max-width: 400px;
    }

    .task-header {
        display: flex;
        flex-direction: row;
        justify-content: space-between;
        align-items: center;

        margin: 0 0 10px 0;
        padding: 10px;
        font-size: 1.1rem;
        font-weight: bold;
        text-shadow: 2px 1px 0 rgba(0, 0, 0, 0.22);

        background-color: var(--accent-color);
        color: white;
    }

    .task-title-input {
        border: none;
        font-size: 1.1rem;
        outline: none;
    }

    .task-header-options {
        display: flex;
        flex-direction: row;
    }

    .task-btn {
        display: flex;
        justify-content: center;
        align-items: center;
        background-color: transparent;
        border: none;
        outline: none;

        color: rgba(255, 255, 255, 0.84);
        text-shadow: 2px 1px 0 rgba(0, 0, 0, 0.22);
    }

    .task-btn:hover {
        color: white;
    }

    .task-description {
        font-size: 1rem;
        padding: 10px;
        margin: 0 0 10px 0;

        line-height: 1.5rem;
    }

    .task-description-input {
        resize: vertical;

        width: calc(100% - 20px);

        font-size: 1rem;
        padding: 10px;
        margin: 0 0 10px 0;

        line-height: 1.5rem;

        border: none;
        outline: none;
    }

    .task-deadline-wrap {
        display: flex;
        flex-direction: row;
        justify-content: space-around;
        align-items: center;

        margin: 0 10px 10px 10px;
        padding: 10px;

        border-bottom: 1px solid var(--text-color);
    }

    .task-deadline {
        margin: 0;
    }

    .task-deadline-input {
        border: 1px solid var(--secondary-text-color)
    }

    .task-created-info {
        padding: 10px;
        display: flex;
        flex-direction: row;

        align-items: center;

        color: var(--secondary-text-color)
    }

    .task-creator {
        margin: 0 10px;
        padding: 5px;
        border-radius: 20px;
        background-color: var(--primary-color);

        color: white;
        box-shadow: 2px 2px 0 rgba(0, 0, 0, 0.22);
    }

    .task-created-at {
        margin: 0 10px;
    }

    .task-assignees-wrap {
        display: grid;
        grid-template-columns: repeat(auto-fit, minmax(100px, 1fr));
        gap: 15px;

        padding: 10px;
        color: var(--secondary-text-color)
    }

    .task-assignee {
        text-align: center;
        padding: 5px 10px;
        border-radius: 20px;
        border: 1px solid var(--secondary-text-color);

        margin: 0 auto;
    }
</style>
