package eci.server.ItemModule.dto.itemTodo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemTodoResponseList {
    List<ItemTodoResponse> inProgress;
    List<ItemTodoResponse> needRevise;
    List<ItemTodoResponse> rejected;
    List<ItemTodoResponse> waitingApproval;
}
