<script>
    import {userId} from "../../stores/authStore.js";
    import {updateTask, getUserByUsername} from "../../services/api/api.js";
    import {formatDateToStr} from "../../utils/formatter.js";
    import Task from "../../classes/Task.js";
    import User from "../../classes/User.js";
    import AssigneeHandler from "./AssigneeHandler.svelte";

    export let task;

    let isMinimized = true;
    let isEditing = false;

    $: canEdit = String(task.creator.id) === String($userId);

    let updatedHeader = task.header;
    let updatedDescription = task.description;
    let updatedDeadline = formatDateToStr(task.deadline);
    let updatedAssignees = task.assignees;

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
            updatedDeadline = formatDateToStr(task.deadline);
            updatedAssignees = task.assignees;
            errorMessage = "";
        }
    }

    async function saveTask() {
        isLoading = true;
        errorMessage = "";

        try {
            const assigneeIds = updatedAssignees.map(assignee => assignee.id);

            const updatedTask = {
                ...task,
                header: updatedHeader,
                description: updatedDescription,
                deadline: formatDateToStr(updatedDeadline),
                assigneeIds
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

    async function addAssignee(username) {
        try {
            const userData = await getUserByUsername(username);
            const newUser = new User(userData);
            if (!updatedAssignees.some(assignee => assignee.id === newUser.id)) {
                updatedAssignees = [...updatedAssignees, newUser];
            } else {
                errorMessage = `User "${username}" is already an assignee.`;
            }
        } catch (error) {
            errorMessage = error.message || `Failed to add user "${username}".`;
            console.error(errorMessage);
        }
    }

    function removeAssignee(assigneeToRemove) {
        updatedAssignees = updatedAssignees.filter(assignee => assignee.id !== assigneeToRemove.id);
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
            {#if canEdit}
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
                        class="task-deadline-input"
                        bind:value={updatedDeadline}
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
        <AssigneeHandler
                assignees={updatedAssignees}
                canEdit={isEditing}
                onAdd={addAssignee}
                onRemove={removeAssignee}
        />
        {#if errorMessage}
            <p class="error-message">{errorMessage}</p>
        {/if}
    {/if}
    <div class="task-seperator">

    </div>
</div>

<style>
    .task-container {
        display: flex;
        flex-direction: column;
        width: 100%;
        padding: 5px;
        color: var(--text-color);
    }

    .task-header {
        display: flex;
        flex-direction: row;
        justify-content: space-between;
        align-items: center;
        gap: 20px;

        margin: 0 0 10px 0;
        padding: 10px;
        font-size: 1.1rem;
        font-weight: bold;
        text-shadow: 2px 1px 0 rgba(0, 0, 0, 0.22);

        color: var(--text-color);
    }

    .task-title-input {
        background-color: rgba(255, 255, 255, 0.04);
        color: var(--text-color);

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
        background-color: rgba(255, 255, 255, 0.04);
        color: var(--text-color);

        width: calc(100% - 40px);

        font-size: 1rem;
        margin: 10px;

        line-height: 1.5rem;

        border: none;
        outline: none;
    }

    .task-deadline-wrap {
        display: flex;
        flex-direction: row;
        justify-content: space-around;
        align-items: center;

        margin: 10px;
        padding: 10px;
    }

    .task-deadline {
        margin: 0;
    }

    .task-deadline-input {
        background-color: rgba(255, 255, 255, 0.04);
        color: var(--text-color);

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

    .task-seperator {
        border-bottom: 1px solid var(--secondary-text-color);
        margin: 10px;
    }
</style>
