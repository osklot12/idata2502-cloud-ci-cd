package ntnu.idata2502.backend.services;

import ntnu.idata2502.backend.entities.Task;
import ntnu.idata2502.backend.repositories.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class StandardTaskService implements TaskService{
    private final TaskRepository taskRepository;

    @Autowired
    public StandardTaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    @Override
    public Task getTaskById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found with id: " + id));
    }

    @Override
    public Task createTask(Task task) {
        task.setCreatedAt(LocalDateTime.now());
        return taskRepository.save(task);
    }

    @Override
    public Task updateTaskById(Long id, Task updatedTask) {
        Task existingTask = getTaskById(id);
        existingTask.setHeader(updatedTask.getHeader());
        existingTask.setDescription(updatedTask.getDescription());
        existingTask.setStatus(updatedTask.getStatus());
        existingTask.setDeadline(updatedTask.getDeadline());
        return taskRepository.save(existingTask);
    }

    @Override
    public void deleteTaskById(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new RuntimeException("Task not found with id: " + id);
        }
        taskRepository.deleteById(id);
    }
}
