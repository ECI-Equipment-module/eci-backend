package eci.server.DesignModule.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class DesignReadDto {
    private Long id;
    private String name;

    //프로젝트 번호, 제품명, 제품 번호
    //private ItemProjectDashboardDto itemProjectDashboardDto;
    private String projectNumber;
    private String itemName;
    private Integer itemNumber;

    private Boolean tempsave;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;

}
