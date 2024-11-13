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

@Service
public class StandardTaskService implements TaskService{
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    @Autowired
    public StandardTaskService(TaskRepository taskRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<Task> getAllTasks() {
        User currentUser = AuthUtil.getCurrentUser();
        return taskRepository.findTasksByUserParticipation(currentUser.getId());
    }

    @Override
    public Task getTaskById(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        if (!isCreator(task) && !isAssigned(task)) {
            throw new AccessDeniedException("You do not have permission to view this task.");
        }

        return task;
    }

    @Override
    public Task createTask(TaskRequest request) {
        User creator = AuthUtil.getCurrentUser();

        Task task = createTaskFromRequest(request);
        task.setCreatedAt(LocalDateTime.now());
        task.setCreator(creator);

        return taskRepository.save(task);
    }

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
        task.setAssignees(getUsersByIds(request.assigneeIds()));

        return taskRepository.save(task);
    }

    private Task createTaskFromRequest(TaskRequest request) {
        Task task = new Task();

        task.setHeader(request.header());
        task.setDescription(request.description());
        task.setStatus(request.status());
        task.setDeadline(request.deadline());
        task.setAssignees(new HashSet<>(getUsersByIds(request.assigneeIds())));

        return task;
    }

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

    private boolean isCreator(Task task) {
        return task.getCreator().equals(AuthUtil.getCurrentUser());
    }

    private boolean isAssigned(Task task) {
        return task.getAssignees().contains(AuthUtil.getCurrentUser());
    }

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
}
