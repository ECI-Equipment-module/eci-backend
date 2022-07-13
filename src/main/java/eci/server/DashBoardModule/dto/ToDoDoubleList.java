package eci.server.DashBoardModule.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ToDoDoubleList {
    List<ToDoSingle> lists;


}
