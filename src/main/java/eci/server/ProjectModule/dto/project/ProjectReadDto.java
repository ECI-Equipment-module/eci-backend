package eci.server.ProjectModule.dto.project;

import com.fasterxml.jackson.annotation.JsonFormat;

import eci.server.ItemModule.dto.item.ItemProjectDashboardDto;
import lombok.AllArgsConstructor;
import lombok.Data;


import java.time.LocalDate;
import java.time.LocalDateTime;


@Data
@AllArgsConstructor
/**
 *
 */
public class ProjectReadDto {
    private Long id;
    private String projectNumber;
    private String name;
    private String carType;

    //제품명, 제품 번호
    //private ItemProjectDashboardDto itemProjectDashboardDto;
    private String itemName;
    private String itemNumber;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate startPeriod;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate overPeriod;

    private Boolean tempsave;

    private String lifecycle;

    //phase
    private String phase;



    //status
    //private Double status;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;

}