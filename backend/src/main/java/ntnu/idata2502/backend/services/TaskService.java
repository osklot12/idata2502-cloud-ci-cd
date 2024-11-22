package ntnu.idata2502.backend.services;

import ntnu.idata2502.backend.dto.TaskRequest;
import ntnu.idata2502.backend.entities.Task;

import java.util.List;

/**
 * Defines the contract for task management services.
 * This interface specifies operations for creating, retrieving, updating, and deleting tasks.
 */
public interface TaskService {

    /**
     * Retrieves all tasks associated with the system.
     *
     * @return a list of all {@link Task} objects
     */
    List<Task> getAllTasks();

    /**
     * Retrieves a task by its unique identifier.
     *
     * @param id the unique identifier of the task to retrieve
     * @return the {@link Task} with the specified ID
     * @throws RuntimeException if the task is not found
     */
    Task getTaskById(Long id);

    /**
     * Creates a new task based on the provided task details.
     *
     * @param request the {@link TaskRequest} containing details for the new task
     * @return the newly created {@link Task}
     */
    Task createTask(TaskRequest request);

    /**
     * Updates an existing task identified by its unique ID.
     *
     * @param id the unique identifier of the task to update
     * @param request the {@link TaskRequest} containing the updated task details
     * @return the updated {@link Task}
     * @throws RuntimeException if the task is not found
     * @throws SecurityException if the user does not have permission to update the task
     */
    Task updateTaskById(Long id, TaskRequest request);

    /**
     * Deletes an existing task identified by its unique ID.
     *
     * @param id the unique identifier of the task to delete
     * @throws RuntimeException if the task is not found
     * @throws SecurityException if the user does not have permission to delete the task
     */
    void deleteTaskById(Long id);
}
