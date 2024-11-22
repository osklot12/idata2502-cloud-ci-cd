package ntnu.idata2502.backend.services;

import ntnu.idata2502.backend.dto.TaskRequest;
import ntnu.idata2502.backend.entities.Task;
import ntnu.idata2502.backend.entities.User;
import ntnu.idata2502.backend.repositories.TaskRepository;
import ntnu.idata2502.backend.repositories.UserRepository;
import ntnu.idata2502.backend.util.AuthUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.security.access.AccessDeniedException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ActiveProfiles("dev")
@ExtendWith(MockitoExtension.class)
class StandardTaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AuthUtil authUtil;

    @InjectMocks
    private StandardTaskService taskService;

    private User mockUser;
    private User anotherMockUser;
    private Task task;
    private Task updatedTask;

    @BeforeEach
    void setup() {
        // mock authenticated user
        mockUser = new User("username", "email@test.com", "password");
        mockUser.setId(1L);

        // mocking another user
        anotherMockUser = new User("anotherUsername", "another@example.com", "anotherPassword");
        anotherMockUser.setId(2L);

        // mock task
        task = new Task("Task 1", "Description 1", "pending", LocalDate.now(), mockUser);
        task.setId(1L);

        // mock updated task
        updatedTask = new Task("Updated Header", "Updated Description", "done", LocalDate.now().plusDays(5), mockUser);
    }

    @Test
    void testGetAllTasks() {
        // given
        Task task2 = new Task("Task 2", "Description 2", "pending", LocalDate.now(), mockUser);
        List<Task> mockTasks = List.of(task, task2);

        when(taskRepository.findTasksByUserParticipation(mockUser.getId())).thenReturn(mockTasks);
        when(authUtil.getCurrentUser()).thenReturn(mockUser);

        // when
        List<Task> tasks = taskService.getAllTasks();

        // then
        assertNotNull(tasks);
        assertEquals(2, tasks.size());
        assertEquals(task.getHeader(), tasks.get(0).getHeader());
        verify(taskRepository).findTasksByUserParticipation(mockUser.getId());
    }

    @Test
    void testGetTaskByIdUserIsCreator() {
        // given
        when(taskRepository.findById(task.getId())).thenReturn(Optional.of(task));
        when(authUtil.getCurrentUser()).thenReturn(mockUser);

        // when
        Task result = taskService.getTaskById(task.getId());

        // then
        assertNotNull(result);
        assertEquals(task.getHeader(), result.getHeader());
        verify(taskRepository).findById(task.getId());
    }

    @Test
    void testGetTaskByIdUserIsAssignee() {
        // given
        task.setAssignees(Set.of(mockUser));
        task.setCreator(anotherMockUser);

        when(taskRepository.findById(task.getId())).thenReturn(Optional.of(task));
        when(authUtil.getCurrentUser()).thenReturn(mockUser);

        // when
        Task result = taskService.getTaskById(task.getId());

        // then
        assertNotNull(result);
        assertEquals(task.getHeader(), result.getHeader());
        verify(taskRepository).findById(task.getId());
    }

    @Test
    void testGetTaskByIdAccessDenied() {
        // given
        task.setCreator(anotherMockUser);
        task.setAssignees(Set.of());

        when(taskRepository.findById(task.getId())).thenReturn(Optional.of(task));
        when(authUtil.getCurrentUser()).thenReturn(mockUser);

        // when & then
        assertThrows(AccessDeniedException.class, () -> taskService.getTaskById(task.getId()));
        verify(taskRepository).findById(task.getId());
    }

    @Test
    void testGetTaskByIdTaskNotFound() {
        //given
        when(taskRepository.findById(task.getId())).thenReturn(Optional.empty());

        // when & then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> taskService.getTaskById(task.getId()));
        assertEquals("Task not found", exception.getMessage());
        verify(taskRepository).findById(task.getId());
    }

    TaskRequest getTaskRequest(Task task) {
        return new TaskRequest(
                task.getHeader(),
                task.getDescription(),
                task.getStatus(),
                task.getDeadline(),
                task.getAssignees().stream()
                        .map(User::getId)
                        .collect(Collectors.toSet())
        );
    }

    @Test
    void testCreateTaskSuccess() {
        // given
        TaskRequest taskRequest = getTaskRequest(task);

        when(taskRepository.save(any(Task.class))).thenReturn(task);

        // when
        Task result = taskService.createTask(taskRequest);

        // then
        assertNotNull(result);
        assertEquals(task.getHeader(), result.getHeader());
        assertEquals(task.getDescription(), result.getDescription());
        assertEquals(task.getStatus(), result.getStatus());
        assertEquals(task.getDeadline(), result.getDeadline());
        assertEquals(mockUser, result.getCreator());
        verify(taskRepository).save(any(Task.class));
    }

    @Test
    void testCreateTaskWithAssignees() {
        // given
        Set<User> assignees = Set.of(anotherMockUser);
        task.setAssignees(assignees);
        TaskRequest taskRequest = getTaskRequest(task);

        when(userRepository.findById(anotherMockUser.getId())).thenReturn(Optional.of(anotherMockUser));
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        // when
        Task result = taskService.createTask(taskRequest);

        // then
        assertNotNull(result);
        assertEquals(task.getHeader(), result.getHeader());
        assertEquals(task.getDescription(), result.getDescription());
        assertEquals(task.getStatus(), result.getStatus());
        assertEquals(task.getDeadline(), result.getDeadline());
        assertEquals(mockUser, result.getCreator());
        assertEquals(assignees.size(), result.getAssignees().size());
        assertTrue(result.getAssignees().containsAll(assignees));
        verify(userRepository).findById(anotherMockUser.getId());
        verify(taskRepository).save(any(Task.class));
    }

    @Test
    void testUpdateTaskByIdSuccess() {
        // given
        updatedTask.setAssignees(Set.of(anotherMockUser));
        TaskRequest taskRequest = getTaskRequest(updatedTask);

        when(taskRepository.findById(task.getId())).thenReturn(Optional.of(task));
        when(userRepository.findById(anotherMockUser.getId())).thenReturn(Optional.of(anotherMockUser));
        when(taskRepository.save(any(Task.class))).thenReturn(task);
        when(authUtil.getCurrentUser()).thenReturn(mockUser);

        // when
        Task result = taskService.updateTaskById(task.getId(), taskRequest);

        // then
        assertNotNull(result);
        assertEquals(taskRequest.header(), result.getHeader());
        assertEquals(taskRequest.description(), result.getDescription());
        assertEquals(taskRequest.status(), result.getStatus());
        assertEquals(taskRequest.deadline(), result.getDeadline());
        assertEquals(mockUser, result.getCreator());
        assertEquals(taskRequest.assigneeIds().size(), result.getAssignees().size());
        assertTrue(result.getAssignees().contains(anotherMockUser));

        verify(taskRepository).findById(task.getId());
        verify(userRepository).findById(anotherMockUser.getId());
        verify(taskRepository).save(any(Task.class));
    }

    @Test
    void testUpdateTaskByIdAccessDenied() {
        // given
        task.setCreator(anotherMockUser);
        TaskRequest taskRequest = getTaskRequest(updatedTask);

        when(taskRepository.findById(task.getId())).thenReturn(Optional.of(task));

        // when & then
        assertThrows(AccessDeniedException.class, () -> taskService.updateTaskById(task.getId(), taskRequest));
        verify(taskRepository).findById(task.getId());
        verifyNoMoreInteractions(taskRepository);
    }

    @Test
    void testUpdateTaskByIdTaskNotFound() {
        // given
        TaskRequest taskRequest = getTaskRequest(updatedTask);

        when(taskRepository.findById(task.getId())).thenReturn(Optional.empty());

        // when & then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> taskService.updateTaskById(task.getId(), taskRequest));
        assertEquals("Task not found", exception.getMessage());
        verify(taskRepository).findById(task.getId());
    }

    @Test
    void testDeleteTaskByIdSuccess() {
        // given
        when(taskRepository.existsById(task.getId())).thenReturn(true);
        when(taskRepository.findById(task.getId())).thenReturn(Optional.of(task));
        when(authUtil.getCurrentUser()).thenReturn(mockUser);

        // when
        taskService.deleteTaskById(task.getId());

        // then
        verify(taskRepository).existsById(task.getId());
        verify(taskRepository).findById(task.getId());
        verify(taskRepository).deleteById(task.getId());
    }

    @Test
    void testDeleteTaskByIdTaskNotFound() {
        // given
        when(taskRepository.existsById(task.getId())).thenReturn(false);

        // when & then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> taskService.deleteTaskById(task.getId()));
        assertEquals("Task not found with id: " + task.getId(), exception.getMessage());
        verify(taskRepository).existsById(task.getId());
        verify(taskRepository, never()).findById(anyLong());
        verify(taskRepository, never()).deleteById(anyLong());
    }

    @Test
    void testDeleteTaskByIdAccessDenied() {
        // given
        task.setCreator(anotherMockUser);

        when(authUtil.getCurrentUser()).thenReturn(mockUser);
        when(taskRepository.existsById(task.getId())).thenReturn(true);
        when(taskRepository.findById(task.getId())).thenReturn(Optional.of(task));

        // when & then
        assertThrows(AccessDeniedException.class, () -> taskService.deleteTaskById(task.getId()));
        verify(taskRepository).existsById(task.getId());
        verify(taskRepository).findById(task.getId());
        verify(taskRepository, never()).deleteById(anyLong());
    }
}