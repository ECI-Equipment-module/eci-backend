package eci.server.DashBoardModule.dto.designTodo;

import eci.server.DashBoardModule.dto.itemTodo.ItemTodoResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DesignTodoResponseList {
    List<DesignTodoResponse> inProgress;
    List<DesignTodoResponse> newDesign;
    List<DesignTodoResponse> rejected;
    List<DesignTodoResponse> revise;
    List<DesignTodoResponse> review;
}
