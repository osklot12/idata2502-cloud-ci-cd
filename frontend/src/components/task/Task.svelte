<script>
    import {userId} from "../../stores/authStore.js";

    export let task;

    let isMinimized = true;
    let currentUserId = $userId;

    function toggleMinimize() {
        isMinimized = !isMinimized;
    }
</script>

<div class="task-container">
    <div class="task-header">
        {task.header}
        <div class="task-header-options">
            {#if String(task.creator.id) === String(currentUserId)}
                <button class="task-btn">
                    <span class="material-symbols-outlined">edit</span>
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
        <p class="task-description">
            {task.description}
        </p>
        <div class="task-deadline-wrap">
        <span class="material-symbols-outlined deadline-icon">
            pending_actions
        </span>
            <p class="task-deadline">
                {task.getFormattedDeadline()}
            </p>
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

        color: white;
        text-shadow: 2px 1px 0 rgba(0, 0, 0, 0.22);
    }

    .task-description {
        font-size: 1rem;
        padding: 10px;
        margin: 0 0 10px 0;

        line-height: 1.5rem;
    }

    .task-deadline-wrap {
        display: flex;
        flex-direction: row;
        justify-content: space-around;
        align-items: center;

        margin: 0 10px 10px 10px;

        border-bottom: 1px solid var(--text-color);
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
        padding: 5px;
        border-radius: 20px;
        border: 1px solid var(--secondary-text-color)
    }
</style>
