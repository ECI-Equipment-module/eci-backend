package eci.server.DashBoardModule.dto.myProject;

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
    private String projectType;

    private String produceOrganization;
    private String clientOrganization;
    private String carType;

    //제품명, 제품 번호
    private ItemProjectDashboardDto itemProjectDashboardDto;

//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
//    private LocalDate startPeriod;
//
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
//    private LocalDate overPeriod;

    private Boolean tempsave;


    //phase
    private String phase;

    //status
    private String status;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;

}
