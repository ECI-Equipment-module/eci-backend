package eci.server.DashBoardModule.dto.myProject;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TotalProject {
    private Integer total;
    private double working;
    private double complete;
    private double release;
    private double pending;
    private double drop;
}

