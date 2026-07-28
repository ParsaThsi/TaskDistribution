package org.parsa.taskdistribution.service;

import lombok.RequiredArgsConstructor;
import org.parsa.taskdistribution.entity.AppUser;
import org.parsa.taskdistribution.entity.Project;
import org.parsa.taskdistribution.entity.Task;
import org.parsa.taskdistribution.entity.TaskStatus;
import org.parsa.taskdistribution.repository.AppUserRepository;
import org.parsa.taskdistribution.repository.ProjectRepository;
import org.parsa.taskdistribution.repository.TaskRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final AppUserRepository appUserRepository;
    private final ProjectRepository projectRepository;

    @Transactional
    public Task createTask(Task task) {
        // Fetch and attach the real Project entity from the database
        if (task.getProject() != null && task.getProject().getId() != null) {
            Project realProject = projectRepository.findById(task.getProject().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Project not found with id: " + task.getProject().getId()));
            task.setProject(realProject);
        } else {
            throw new IllegalArgumentException("Task must belong to a project");
        }

        // Fetch and attach the real AppUser (assignee) entity if provided
        if (task.getAssignee() != null && task.getAssignee().getId() != null) {
            AppUser realAssignee = appUserRepository.findById(task.getAssignee().getId())
                    .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + task.getAssignee().getId()));
            task.setAssignee(realAssignee);
        } else {
            // Ensure we don't accidentally save a dummy "transient" user object 
            // if an ID was not provided but MapStruct created an empty object anyway.
            task.setAssignee(null);
        }

        return taskRepository.save(task);
    }

    @Transactional(readOnly = true)
    public List<Task> getTasksByProjectId(Long projectId) {
        return taskRepository.findByProjectId(projectId);
    }

    @Transactional
    public Task updateTaskStatus(Long taskId, TaskStatus status) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task not found with id: " + taskId));
        
        task.setStatus(status);
        return taskRepository.save(task);
    }

    @Transactional
    public Task assignTask(Long taskId, Long userId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task not found with id: " + taskId));
        
        AppUser user = appUserRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));

        task.setAssignee(user);
        return taskRepository.save(task);
    }
    
    @Transactional
    public void deleteTask(Long taskId) {
        if (!taskRepository.existsById(taskId)) {
            throw new IllegalArgumentException("Task not found with id: " + taskId);
        }
        taskRepository.deleteById(taskId);
    }
}
