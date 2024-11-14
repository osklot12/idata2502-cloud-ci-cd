<script>
    export let task;
    export let onUpdateTask = () => {}
    export let onDeleteTask = () => {}
</script>

<div class="task-container">
    <div class="task-header">
        {task.header}
    </div>
    <p class="task-description">
        {task.description}
    </p>
    <p class="task-deadline">
        {task.getFormattedDeadline()}
    </p>
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
    <div>

    </div>
</div>
