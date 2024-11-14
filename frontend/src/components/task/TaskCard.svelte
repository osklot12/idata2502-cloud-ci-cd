<!-- TaskCard.svelte -->
<script>
    export let title;
    export let description;
    export let dueDate;
    export let assignedTo;
    export let status;
    export let onStatusChange;

    // Emit event when status changes
  const handleStatusChange = (event) => {
    const newStatus = event.target.value;
    // Emit the new status to the parent component (TaskList)
    dispatch('statusChange', newStatus);
  };
  </script>
  
  <div class="task-card">
    <h3>{title}</h3>
    <p>{description}</p>
    <small>Due: {dueDate}</small>
    <p>Assigned to: {assignedTo}</p>

    <!-- Dropdown to change task status -->
  <label for="status">Status:</label>
  <select id="status" bind:value={status} on:change={(e) => onStatusChange(e.target.value)}>
    <option value="pending">Pending</option>
    <option value="in-progress">In Progress</option>
    <option value="completed">Completed</option>
  </select>
  </div>
  