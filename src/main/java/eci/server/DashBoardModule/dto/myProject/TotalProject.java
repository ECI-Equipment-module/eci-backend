package eci.server.DashBoardModule.dto.myProject;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TotalProject {
    private Integer total;
    private Double working;
    private Double complete;
    private Double release;
    private Double pending;
    private Double drop;
}

