package eci.server.DashBoardModule.dto.projectTodo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectTodoResponseList {

    List<TodoResponse> todoList1;
    List<TodoResponse> todoList2;

}
