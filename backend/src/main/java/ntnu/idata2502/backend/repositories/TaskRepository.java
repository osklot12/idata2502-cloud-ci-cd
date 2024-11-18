package ntnu.idata2502.backend.repositories;

import ntnu.idata2502.backend.entities.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    @Query("SELECT t FROM Task t WHERE t.creator.id = :userId OR EXISTS (SELECT 1 FROM t.assignees a WHERE a.id = :userId)")
    List<Task> findTasksByUserParticipation(@Param("userId") Long userId);

    // finds tasks by status
    @Query("SELECT t FROM Task t WHERE t.status = ?1")
    List<Task> findByStatus(String status);

    // finds all tasks with a deadline before a given date
    @Query("SELECT t FROM Task t WHERE t.deadline < ?1")
    List<Task> findTasksBeforeDeadline(LocalDateTime deadline);

    // finds all tasks created after a given date
    @Query("SELECT t FROM Task t WHERE t.createdAt >= ?1")
    List<Task> findTasksCreatedAfter(LocalDateTime created);
}
