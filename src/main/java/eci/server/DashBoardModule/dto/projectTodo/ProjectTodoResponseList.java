package eci.server.DashBoardModule.dto.projectTodo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectTodoResponseList {
    List<ProjectTodoResponse> SaveAsDraft;
    List<ProjectTodoResponse> NewProject1;
    List<ProjectTodoResponse> NewProject2;
}
