package ntnu.idata2502.backend.entities;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String header;

    @Column(length = 1000)
    private String description;

    @Column(nullable = false)
    private String status;

    @Column
    private LocalDate deadline;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    @ManyToOne
    @JoinColumn(name = "creator_id", nullable = false)
    private User creator;

    @ManyToMany
    @JoinTable(
            name = "task_assignees",
            joinColumns = @JoinColumn(name = "task_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> assignees = new HashSet<>();

    /**
     * Empty constructor.
     */
    public Task() {}

    /**
     * Constructor.
     *
     * @param header the task header
     * @param description the task description
     * @param status the task status
     * @param deadline the task deadline
     * @param creator the user that created the task
     */
    public Task(String header, String description, String status, LocalDate deadline, User creator) {
        this.header = header;
        this.description = description;
        this.status = status;
        this.deadline = deadline;
        this.creator = creator;
    }

    // getters and setters

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getHeader() {
        return header;
    }
    public void setHeader(String header) {
        this.header = header;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDate getDeadline() {
        return deadline;
    }
    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public User getCreator() { return creator; }
    public void setCreator(User creator) { this.creator = creator; }

    public Set<User> getAssignees() { return assignees; }
    public void setAssignees(Set<User> assignees) { this.assignees = assignees; }
}
