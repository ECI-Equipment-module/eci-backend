package eci.server.DashBoardModule.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import eci.server.ItemModule.dto.item.ItemProjectDashboardDto;
import eci.server.ProjectModule.dto.carType.CarTypeDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectDashboardDto {
    private Long id;
    private String projectNumber;
    private String name;
    private CarTypeDto carType;

    //제품명, 제품 번호
    private ItemProjectDashboardDto itemProjectDashboardDto;

    private LocalDate startPeriod;
    private LocalDate overPeriod;

    private Boolean tempsave;

    private String lifecycle;

    //phase
    private String phase;

    //status
    private Double status;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;

}
