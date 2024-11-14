package ntnu.idata2502.backend.dto;

import ntnu.idata2502.backend.entities.User;

import java.time.LocalDateTime;
import java.util.Set;

public record TaskRequest(String header, String description, String status,
                          LocalDateTime deadline, Set<Long> assigneeIds) {
}
