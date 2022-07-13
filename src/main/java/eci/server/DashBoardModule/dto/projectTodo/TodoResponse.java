package eci.server.DashBoardModule.dto.projectTodo;

import eci.server.DashBoardModule.dto.ToDoDoubleList;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TodoResponse implements Comparable<TodoResponse>{
    Long id;
    String name;
    String type;
    String number;
    Long reviseId;

    @Override
    public int compareTo(TodoResponse o) {
        return (int) (this.id-o.id);
    }
}
