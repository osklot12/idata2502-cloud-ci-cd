<script>
    export let assignees;
    export let canEdit = false;
    export let onRemove;
    export let onAdd;

    let newAssignee = "";

    function handleAdd(event) {
        event.preventDefault(); // Prevent form submission from reloading the page
        if (newAssignee.trim()) {
            onAdd(newAssignee);
            newAssignee = ""; // Clear the input field after adding
        }
    }
</script>

<div class="task-assignee-handler">
    <div class="task-assignee-wrap">
        {#each assignees as assignee}
            <div class="task-assignee">
                {assignee.getDisplayName()}
                {#if canEdit}
                    <button class="assignee-label-btn"
                            on:click={() => onRemove(assignee)}
                    >
                        <span class="material-symbols-outlined">remove</span>
                    </button>
                {/if}
            </div>
        {/each}
    </div>
    {#if canEdit}
        <form class="task-assignee-options" on:submit={handleAdd}>
            <input
                    class="std-form-input std-form-element"
                    placeholder="username"
                    bind:value={newAssignee}
                    required
            />
            <button class="task-assignee-options-btn" type="submit">
                <span class="material-symbols-outlined">add</span>
            </button>
        </form>
    {/if}
</div>

<style>
    .task-assignee-wrap {
        display: flex;
        flex-wrap: wrap;
        justify-content: center;
        gap: 15px;

        padding: 10px;
        color: var(--secondary-text-color)
    }

    .task-assignee {
        display: flex;
        flex-direction: row;
        justify-content: center;
        align-items: center;
        gap: 10px;

        text-align: center;
        padding: 5px 10px;
        border-radius: 20px;
        border: 1px solid var(--secondary-text-color);

        margin: 0 auto;
    }

    .assignee-label-btn {
        display: flex;
        justify-content: center;
        align-items: center;
        background-color: transparent;
        border: none;
        outline: none;

        color: var(--secondary-text-color);
    }

    .assignee-label-btn:hover {
        color: var(--text-color)
    }

    .task-assignee-options {
        display: flex;
        flex-direction: row;
        align-items: center;
        justify-content: center;
    }

    .task-assignee-options-btn {
        outline: none;
        border: none;
        background-color: transparent;
        color: var(--text-color);
        display: flex;
        align-items: center;
        justify-content: center;
        padding: 10px;
    }
</style>