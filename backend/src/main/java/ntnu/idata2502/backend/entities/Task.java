package ntnu.idata2502.backend.entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;

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
    private LocalDateTime deadline;

    @Column(updatable = false)
    private LocalDateTime createdAt;

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
     */
    public Task(String header, String description, String status, LocalDateTime deadline) {
        this.header = header;
        this.description = description;
        this.status = status;
        this.deadline = deadline;
        this.createdAt = LocalDateTime.now();
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

    public LocalDateTime getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDateTime deadline) {
        this.deadline = deadline;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
