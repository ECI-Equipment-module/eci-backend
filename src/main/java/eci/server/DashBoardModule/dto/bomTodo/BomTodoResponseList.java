package eci.server.DashBoardModule.dto.bomTodo;

import eci.server.DashBoardModule.dto.designTodo.DesignTodoResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BomTodoResponseList {
        List<DesignTodoResponse> inProgress;
        List<DesignTodoResponse> newBom;
        List<DesignTodoResponse> rejected;
        List<DesignTodoResponse> revise;
        List<DesignTodoResponse> review;
    }

