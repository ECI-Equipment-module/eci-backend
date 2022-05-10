package eci.server.DashBoardModule.dto;

import eci.server.DashBoardModule.dto.projectTodo.TodoResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ToDoSingle {
    String category;
    List<TodoResponse> toDoList;
}
