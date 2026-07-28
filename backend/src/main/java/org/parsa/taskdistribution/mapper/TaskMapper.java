package org.parsa.taskdistribution.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.parsa.taskdistribution.dto.TaskRequestDto;
import org.parsa.taskdistribution.dto.TaskResponseDto;
import org.parsa.taskdistribution.entity.Task;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface TaskMapper {

    @Mapping(target = "projectId", source = "project.id")
    @Mapping(target = "assigneeId", source = "assignee.id")
    TaskResponseDto toDto(Task task);

    List<TaskResponseDto> toDtoList(List<Task> tasks);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "project.id", source = "projectId")
    @Mapping(target = "assignee.id", source = "assigneeId")
    Task toEntity(TaskRequestDto dto);
}
