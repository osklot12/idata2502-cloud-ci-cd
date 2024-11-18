package ntnu.idata2502.backend.dto;

import ntnu.idata2502.backend.entities.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

public record TaskRequest(String header, String description, String status,
                          LocalDate deadline, Set<Long> assigneeIds) {
}
