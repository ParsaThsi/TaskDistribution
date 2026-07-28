package org.parsa.taskdistribution.dto;

import lombok.Data;
import org.parsa.taskdistribution.entity.TaskStatus;

@Data
public class TaskStatusUpdateDto {
    private TaskStatus status;
}
