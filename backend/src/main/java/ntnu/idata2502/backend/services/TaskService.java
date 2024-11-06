package ntnu.idata2502.backend.services;

import ntnu.idata2502.backend.entities.Task;

import java.util.List;

public interface TaskService {
    /**
     * Returns all tasks.
     *
     * @return list of all tasks
     */
    List<Task> getAllTasks();

    /**
     * Returns a task with a given ID.
     *
     * @param id task id
     * @return the task with the given id
     */
    Task getTaskById(Long id);

    /**
     * Creates a new task.
     *
     * @param task the task to create
     * @return the created task
     */
    Task createTask(Task task);

    /**
     * Updates an existing task.
     *
     * @param id the task id
     * @param updatedTask the updated task
     * @return the updated task
     */
    Task updateTaskById(Long id, Task updatedTask);

    /**
     * Deletes an existing task.
     *
     * @param id the task id
     */
    void deleteTaskById(Long id);
}
