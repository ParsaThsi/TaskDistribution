package org.parsa.taskdistribution.controller;

import lombok.RequiredArgsConstructor;
import org.parsa.taskdistribution.dto.TaskAssignDto;
import org.parsa.taskdistribution.dto.TaskRequestDto;
import org.parsa.taskdistribution.dto.TaskResponseDto;
import org.parsa.taskdistribution.dto.TaskStatusUpdateDto;
import org.parsa.taskdistribution.entity.Task;
import org.parsa.taskdistribution.mapper.TaskMapper;
import org.parsa.taskdistribution.service.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;
    private final TaskMapper taskMapper;

    @PostMapping
    public ResponseEntity<TaskResponseDto> createTask(@RequestBody TaskRequestDto taskRequestDto) {
        Task taskToCreate = taskMapper.toEntity(taskRequestDto);
        Task createdTask = taskService.createTask(taskToCreate);
        return new ResponseEntity<>(taskMapper.toDto(createdTask), HttpStatus.CREATED);
    }

    @GetMapping("/project/{projectId}")
    public ResponseEntity<List<TaskResponseDto>> getTasksByProjectId(@PathVariable Long projectId) {
        List<Task> tasks = taskService.getTasksByProjectId(projectId);
        return ResponseEntity.ok(taskMapper.toDtoList(tasks));
    }

    @PatchMapping("/{taskId}/status")
    public ResponseEntity<TaskResponseDto> updateTaskStatus(
            @PathVariable Long taskId,
            @RequestBody TaskStatusUpdateDto statusUpdateDto) {
        Task updatedTask = taskService.updateTaskStatus(taskId, statusUpdateDto.getStatus());
        return ResponseEntity.ok(taskMapper.toDto(updatedTask));
    }

    @PatchMapping("/{taskId}/assign")
    public ResponseEntity<TaskResponseDto> assignTask(
            @PathVariable Long taskId,
            @RequestBody TaskAssignDto assignDto) {
        Task updatedTask = taskService.assignTask(taskId, assignDto.getAssigneeId());
        return ResponseEntity.ok(taskMapper.toDto(updatedTask));
    }
    
    @DeleteMapping("/{taskId}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long taskId) {
        taskService.deleteTask(taskId);
        return ResponseEntity.noContent().build();
    }
}
