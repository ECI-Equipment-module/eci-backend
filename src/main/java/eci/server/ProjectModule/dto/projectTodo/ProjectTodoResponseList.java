package eci.server.ProjectModule.dto.projectTodo;

import eci.server.ItemModule.dto.itemTodo.ItemTodoResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectTodoResponseList {
    List<ProjectTodoResponse> SaveAsDraft;
    List<ProjectTodoResponse> AddSth;
    List<ProjectTodoResponse> InProgress;
}
