package ntnu.idata2502.backend.services;

import ntnu.idata2502.backend.dto.TaskRequest;
import ntnu.idata2502.backend.entities.Task;
import ntnu.idata2502.backend.entities.User;
import ntnu.idata2502.backend.repositories.TaskRepository;
import ntnu.idata2502.backend.repositories.UserRepository;
import ntnu.idata2502.backend.util.AuthUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.Optional;

/**
 * Implementation of {@link TaskService} that provides standard operations for managing tasks.
 * This service handles CRUD operations and enforces access control based on task ownership and participation.
 */
@Service
public class StandardTaskService implements TaskService {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final AuthUtil authUtil;

    /**
     * Constructs a {@link StandardTaskService}.
     *
     * @param taskRepository the {@link TaskRepository} for managing task data
     * @param userRepository the {@link UserRepository} for managing user data
     */
    @Autowired
    public StandardTaskService(TaskRepository taskRepository, UserRepository userRepository, AuthUtil authUtil) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.authUtil = authUtil;
    }

    /**
     * Retrieves all tasks associated with the currently authenticated user.
     *
     * @return a list of {@link Task} objects where the current user is a creator or participant
     */
    @Override
    public List<Task> getAllTasks() {
        User currentUser = authUtil.getCurrentUser();
        return taskRepository.findTasksByUserParticipation(currentUser.getId());
    }

    /**
     * Retrieves a specific task by its ID, enforcing access control.
     *
     * @param id the ID of the task to retrieve
     * @return the {@link Task} object if accessible
     * @throws RuntimeException      if the task is not found
     * @throws AccessDeniedException if the current user does not have access to the task
     */
    @Override
    public Task getTaskById(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        if (!isCreator(task) && !isAssigned(task)) {
            throw new AccessDeniedException("You do not have permission to view this task.");
        }

        return task;
    }

    /**
     * Creates a new task with the specified details.
     *
     * @param request the {@link TaskRequest} containing the task details
     * @return the newly created {@link Task}
     */
    @Override
    public Task createTask(TaskRequest request) {
        Task task = createTaskFromRequest(request);
        task.setCreatedAt(LocalDateTime.now());
        task.setCreator(authUtil.getCurrentUser());

        return taskRepository.save(task);
    }

    /**
     * Updates an existing task by its ID, enforcing access control.
     *
     * @param id      the ID of the task to update
     * @param request the {@link TaskRequest} containing the updated details
     * @return the updated {@link Task}
     * @throws AccessDeniedException if the current user does not have permission to update the task
     */
    @Override
    public Task updateTaskById(Long id, TaskRequest request) {
        Task task = getTaskById(id);
        if (!isCreator(task)) {
            throw new AccessDeniedException("You do not have permission to edit this task.");
        }

        task.setHeader(request.header());
        task.setDescription(request.description());
        task.setStatus(request.status());
        task.setDeadline(request.deadline());
        task.setAssignees(new HashSet<>(getUsersByIds(request.assigneeIds())));

        return taskRepository.save(task);
    }

    /**
     * Deletes a task by its ID, enforcing access control.
     *
     * @param id the ID of the task to delete
     * @throws RuntimeException      if the task is not found
     * @throws AccessDeniedException if the current user does not have permission to delete the task
     */
    @Override
    public void deleteTaskById(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new RuntimeException("Task not found with id: " + id);
        }

        if (!isCreator(getTaskById(id))) {
            throw new AccessDeniedException("You do not have permission to delete this task.");
        }

        taskRepository.deleteById(id);
    }

    /**
     * Converts a {@link TaskRequest} into a {@link Task}.
     *
     * @param request the {@link TaskRequest} containing the task details
     * @return a {@link Task} object populated with the request details
     */
    private Task createTaskFromRequest(TaskRequest request) {
        Task task = new Task();

        task.setHeader(request.header());
        task.setDescription(request.description());
        task.setStatus(request.status());
        task.setDeadline(request.deadline());
        task.setAssignees(new HashSet<>(getUsersByIds(request.assigneeIds())));

        return task;
    }

    /**
     * Retrieves a set of {@link User} objects by their IDs.
     *
     * @param userIds the set of user IDs to retrieve
     * @return a set of {@link User} objects corresponding to the IDs
     */
    private Set<User> getUsersByIds(Set<Long> userIds) {
        if (userIds == null || userIds.isEmpty()) {
            return Set.of(); // Return an empty set if no assignees are specified
        }

        return userIds.stream()
                .map(userRepository::findById) // Get an Optional<User> for each userId
                .filter(Optional::isPresent) // Filter out non-existing users
                .map(Optional::get) // Extract the User from the Optional
                .collect(Collectors.toSet()); // Collect into a Set
    }

    /**
     * Checks if the current user is the creator of the specified task.
     *
     * @param task the {@link Task} to check
     * @return true if the current user is the creator, false otherwise
     */
    private boolean isCreator(Task task) {
        return task.getCreator().equals(authUtil.getCurrentUser());
    }

    /**
     * Checks if the current user is assigned to the specified task.
     *
     * @param task the {@link Task} to check
     * @return true if the current user is assigned, false otherwise
     */
    private boolean isAssigned(Task task) {
        return task.getAssignees().contains(authUtil.getCurrentUser());
    }
}
