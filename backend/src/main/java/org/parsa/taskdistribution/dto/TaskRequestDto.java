package org.parsa.taskdistribution.dto;

import lombok.Data;
import org.parsa.taskdistribution.entity.TaskPriority;
import org.parsa.taskdistribution.entity.TaskStatus;

import java.time.LocalDate;

@Data
public class TaskRequestDto {
    private String title;
    private String description;
    private TaskStatus status;
    private TaskPriority priority;
    private LocalDate dueDate;
    private Long projectId;
    private Long assigneeId;
}
