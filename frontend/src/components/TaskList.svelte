<script>
    import TaskCard from './TaskCard.svelte';
  
    //Test context
    let tasks = [
      { title: 'Task 1', description: 'Description of Task 1', dueDate: '2024-11-08', status: 'pending', assignedTo: 'John Doe' },
      { title: 'Task 2', description: 'Description of Task 2', dueDate: '2024-11-09', status: 'in-progress', assignedTo: 'Jane Smith' },
      { title: 'Task 3', description: 'Description of Task 3', dueDate: '2024-11-10', status: 'completed', assignedTo: 'Alice Brown' }
    ];
  
    let newTask = {
      title: '',
      description: '',
      dueDate: '',
      status: 'pending',
      assignedTo: ''
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
      tasks = [...tasks, { ...newTask }];
      newTask = { title: '', description: '', dueDate: '', status: 'pending', assignedTo: '' };
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
        <TaskCard title={task.title} description={task.description} dueDate={task.dueDate} assignedTo={task.assignedTo} onStatusChange={(newStatus) => updateTaskStatus(task, newStatus)} />
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
        <TaskCard title={task.title} description={task.description} dueDate={task.dueDate} assignedTo={task.assignedTo} onStatusChange={(newStatus) => updateTaskStatus(task, newStatus)} />
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
        <TaskCard title={task.title} description={task.description} dueDate={task.dueDate} assignedTo={task.assignedTo} onStatusChange={(newStatus) => updateTaskStatus(task, newStatus)} />
      {/each}
    </div>
  </div>
  