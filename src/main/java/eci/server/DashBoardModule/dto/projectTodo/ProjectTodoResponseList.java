package eci.server.DashBoardModule.dto.projectTodo;

import eci.server.DashBoardModule.dto.itemTodo.ItemTodoResponse;
.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectTodoResponseList {
    List<ProjectTodoResponse> SaveAsDraft;

    List<ItemTodoResponse> NewProject;

}
