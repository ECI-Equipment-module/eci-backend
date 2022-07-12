package eci.server.DashBoardModule.dto.projectTodo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TodoResponse {
    Long id;
    String name;
    String type;
    String number;
    Long reviseId;
}
