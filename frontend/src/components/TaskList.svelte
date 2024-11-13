<script>
    import TaskCard from './TaskCard.svelte';
  
    //Test context
    let tasks = [
      { title: 'Task 1', description: 'Description of Task 1', dueDate: '2024-11-08', status: 'pending', assignedTo: 'John Doe', id: 1 },
      { title: 'Task 2', description: 'Description of Task 2', dueDate: '2024-11-09', status: 'in-progress', assignedTo: 'Jane Smith', id: 2 },
      { title: 'Task 3', description: 'Description of Task 3', dueDate: '2024-11-10', status: 'completed', assignedTo: 'Alice Brown', id: 3 }
    ];
  
    let newTask = {
      title: '',
      description: '',
      dueDate: '',
      status: 'pending',
      assignedTo: '',
      id: ''
    };
  
    let showForm = false;
    let users = ['John Doe', 'Jane Smith', 'Alice Brown'];
  
    let sortByPending = 'dueDate';
    let sortByInProgress = 'dueDate';
    let sortByCompleted = 'dueDate';
  
    function addTask() {
      if (!newTask.title || !newTask.description || !newTask.dueDate || !newTask.assignedTo) {
        alert('Please fill in all fields');
        return;
      }
      id = Math.random();
      tasks = [...tasks, { ...newTask }];
      newTask = { title: '', description: '', dueDate: '', status: 'pending', assignedTo: '', id: id };
      showForm = false;
    }

    function updateTaskStatus(task, newStatus) {
    task.status = newStatus;
    tasks = [...tasks];
  }
  
    // Sorting function that works with each task column
    function sortTasks(tasks, sortBy) {
      return tasks.sort((a, b) => {
        if (sortBy === 'dueDate') {
          return new Date(a.dueDate) - new Date(b.dueDate);
        } else if (sortBy === 'assignedTo') {
          return a.assignedTo.localeCompare(b.assignedTo);
        }
        return 0;
      });
    }

    function removeTask(taskId) {
    tasks = tasks.filter(task => task.id !== taskId);
  }
    
  
    // Filter tasks by status
  $: pendingTasks = sortTasks(tasks.filter(task => task.status === 'pending'));
  $: inProgressTasks = sortTasks(tasks.filter(task => task.status === 'in-progress'));
  $: completedTasks = sortTasks(tasks.filter(task => task.status === 'completed'));
  </script>
  
  
  
  <button class="show-form-button" on:click={() => showForm = !showForm}>
    {showForm ? 'Cancel' : 'Add New Task'}
  </button>
  
  {#if showForm}
    <div class="form-container">
      <h3>Add a New Task</h3>
      <label for="title">Title</label>
      <input id="title" type="text" bind:value={newTask.title} placeholder="Task title" />
      <label for="description">Description</label>
      <textarea id="description" bind:value={newTask.description} placeholder="Task description"></textarea>
      <label for="dueDate">Due Date</label>
      <input id="dueDate" type="date" bind:value={newTask.dueDate} />
      <label for="assignedTo">Assign to</label>
      <select id="assignedTo" bind:value={newTask.assignedTo}>
        <option value="" disabled selected>Select a user</option>
        {#each users as user}
          <option value={user}>{user}</option>
        {/each}
      </select>
      <button on:click={addTask}>Add Task</button>
    </div>
  {/if}
  
  <h2>Task List</h2>
  <div class="task-board">
    <!-- Pending Tasks -->
    <div class="task-column">
      <h2>Pending Tasks</h2>
      <select bind:value={sortByPending}>
        <option value="dueDate">Sort by Due Date</option>
        <option value="assignedTo">Sort by Assigned To</option>
      </select>
      {#each pendingTasks as task}
        <TaskCard title={task.title} description={task.description} dueDate={task.dueDate} assignedTo={task.assignedTo} onStatusChange={(newStatus) => updateTaskStatus(task, newStatus)} onRemove={() => removeTask(task.id)} />
      {/each}
    </div>
  
    <!-- In Progress Tasks -->
    <div class="task-column">
      <h2>In Progress</h2>
      <select bind:value={sortByInProgress}>
        <option value="dueDate">Sort by Due Date</option>
        <option value="assignedTo">Sort by Assigned To</option>
      </select>
      {#each inProgressTasks as task}
        <TaskCard title={task.title} description={task.description} dueDate={task.dueDate} assignedTo={task.assignedTo} onStatusChange={(newStatus) => updateTaskStatus(task, newStatus)} onRemove={() => removeTask(task.id)} />
      {/each}
    </div>
  
    <!-- Completed Tasks -->
    <div class="task-column">
      <h2>Completed</h2>
      <select bind:value={sortByCompleted}>
        <option value="dueDate">Sort by Due Date</option>
        <option value="assignedTo">Sort by Assigned To</option>
      </select>
      {#each completedTasks as task}
        <TaskCard title={task.title} description={task.description} dueDate={task.dueDate} assignedTo={task.assignedTo} onStatusChange={(newStatus) => updateTaskStatus(task, newStatus)} onRemove={() => removeTask(task.id)} />
      {/each}
    </div>
  </div>

  <style>
    /* General body styling */
    :global(body) {
      font-family: Arial, sans-serif;
      background-color: #f0f8ff; /* Light blue background */
      color: #333;
      padding: 20px;
      display: flex;
      justify-content: center;
    }
  
    /* Button styling */
    .show-form-button {
      background-color: #2176c7;
      color: white;
      border: none;
      padding: 10px 20px;
      border-radius: 8px;
      cursor: pointer;
      margin-bottom: 20px;
      box-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);
      transition: background-color 0.3s;
    }
  
    .show-form-button:hover {
      background-color: #1d5fa7;
    }
  
    /* Form container styling */
    .form-container {
      background-color: #ffffff;
      padding: 20px;
      border-radius: 8px;
      box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
      max-width: 400px;
      margin-bottom: 20px;
      border: 1px solid #cce4f6;
    }
  
    .form-container input,
    .form-container textarea,
    .form-container select,
    .form-container button {
      width: 100%;
      margin-bottom: 10px;
      padding: 10px;
      border-radius: 4px;
      border: 1px solid #cce4f6;
      font-size: 1rem;
    }
  
    .form-container button {
      background-color: #2176c7;
      color: white;
      border: none;
      cursor: pointer;
      transition: background-color 0.3s;
    }
  
    .form-container button:hover {
      background-color: #1d5fa7;
    }
  
    /* Task Board styling */
    .task-board {
      display: flex;
      gap: 20px;
      max-width: 1200px;
      width: 100%;
    }
  
    /* Task Column Styling */
    .task-column {
      flex: 1;
      background-color: #f5faff;
      padding: 10px;
      border-radius: 8px;
      border: 1px solid #cce4f6;
      box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
    }
  
    .task-column h2 {
      text-align: center;
      color: #2176c7;
      margin-bottom: 15px;
    }
  
    .task-column select {
      width: 100%;
      padding: 8px;
      margin-bottom: 10px;
      border-radius: 4px;
      border: 1px solid #cce4f6;
      background-color: #e6f0fa;
    }
  
    /* TaskCard Styling */
    .task-card {
      background-color: #ffffff;
      border: 1px solid #cce4f6;
      border-radius: 8px;
      padding: 16px;
      margin-bottom: 10px;
      box-shadow: 0 4px 8px rgba(33, 118, 199, 0.1);
      transition: transform 0.2s, box-shadow 0.2s;
    }
  
    .task-card:hover {
      transform: translateY(-3px);
      box-shadow: 0 6px 12px rgba(33, 118, 199, 0.2);
    }
  
    .task-card h3 {
      margin: 0 0 8px 0;
      color: #2176c7;
    }
  
    .task-card p {
      margin: 0 0 8px 0;
      color: #555;
      font-size: 0.9rem;
    }
  
    .task-card small {
      display: block;
      margin-bottom: 8px;
      font-size: 0.8rem;
      color: #777;
    }
  
    .status-container {
      display: flex;
      align-items: center;
      margin-top: 10px;
    }
  
    .status-select {
      width: 100%;
      padding: 6px;
      border-radius: 4px;
      border: 1px solid #cce4f6;
      background-color: #e6f0fa;
      color: #2176c7;
      transition: border-color 0.2s;
    }
  
    .status-select:hover {
      border-color: #2176c7;
    }
  </style>
  