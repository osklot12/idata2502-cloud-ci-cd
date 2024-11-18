<!-- TaskCard.svelte -->
<script>
  export let title;
  export let description;
  export let dueDate;
  export let assignedTo;
  export let status;
  export let onStatusChange;
  export let onRemove = () => {};

  // Emit event when status changes
  const handleStatusChange = (event) => {
    const newStatus = event.target.value;
    // Emit the new status to the parent component (TaskList)
    dispatch("statusChange", newStatus);
  };
</script>

<div class="task-card">
  <button class="remove-button" on:click={onRemove}>✕</button>

  <h3>{title}</h3>
  <p>{description}</p>
  <small>Due: {dueDate}</small>
  <p>Assigned to: {assignedTo}</p>

  <!-- Dropdown to change task status -->
  <label for="status">Status:</label>
  <select
    id="status"
    bind:value={status}
    on:change={(e) => onStatusChange(e.target.value)}
  >
    <option value="pending">Pending</option>
    <option value="in-progress">In Progress</option>
    <option value="completed">Completed</option>
  </select>
</div>

<style>
  /* Container styling for the whole task board */
  :global(body) {
    font-family: Arial, sans-serif;
    background-color: #e6f0fa;
    color: #333;
    margin: 0;
    padding: 20px;
    display: flex;
    justify-content: center;
  }

  .task-board {
    display: flex;
    gap: 20px;
    max-width: 1200px;
    width: 100%;
  }

  /* Styling for each task list */
  .task-list {
    flex: 1;
    background-color: #f5faff;
    padding: 10px;
    border-radius: 8px;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
    border: 1px solid #cce4f6;
  }

  .task-list h2 {
    text-align: center;
    color: #2176c7;
    margin-bottom: 15px;
  }

  /* 3-column layout */
  .task-container {
    display: grid;
    grid-template-columns: repeat(3, 1fr);
    gap: 20px;
    padding: 20px;
  }

  /* Task card styling */
  .task-card {
    background-color: #ffffff;
    border: 1px solid #cce4f6;
    border-radius: 8px;
    padding: 16px;
    box-shadow: 0 4px 8px rgba(33, 118, 199, 0.1);
    transition:
      transform 0.2s,
      box-shadow 0.2s;
  }

  .task-card:hover {
    transform: translateY(-3px);
    box-shadow: 0 6px 12px rgba(33, 118, 199, 0.2);
  }

  .task-title {
    margin: 0 0 8px 0;
    font-size: 1.2rem;
    font-weight: bold;
    color: #2176c7;
  }

  .task-description {
    margin: 0 0 8px 0;
    font-size: 0.9rem;
    color: #555;
  }

  .task-due-date,
  .task-assigned-to {
    display: block;
    font-size: 0.8rem;
    color: #555;
    margin-bottom: 8px;
  }

  .status-container {
    display: flex;
    align-items: center;
    margin-top: 10px;
  }

  .status-label {
    margin-right: 8px;
    font-size: 0.9rem;
    color: #2176c7;
  }

  .status-select {
    width: 100%;
    padding: 6px;
    border-radius: 4px;
    border: 1px solid #cce4f6;
    font-size: 0.9rem;
    background-color: #f5faff;
    color: #2176c7;
    transition: border-color 0.2s;
  }

  .status-select:hover {
    border-color: #2176c7;
  }

  .status-select option {
    color: #2176c7;
  }

  .status-select option[value="pending"] {
    color: #ff6f61;
  }

  .status-select option[value="in-progress"] {
    color: #f0ad4e;
  }

  .status-select option[value="completed"] {
    color: #5cb85c;
  }
</style>
