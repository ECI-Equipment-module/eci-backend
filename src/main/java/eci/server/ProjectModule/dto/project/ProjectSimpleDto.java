package eci.server.ProjectModule.dto.project;

import com.fasterxml.jackson.annotation.JsonFormat;
import eci.server.ItemModule.dto.item.ItemProjectDto;
import eci.server.ProjectModule.dto.carType.CarTypeDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectSimpleDto {
    private Long id;
    private String projectNumber;
    private String name;
    private CarTypeDto carType;

    private ItemProjectDto itemProjectDto;

    //제품 타입, 제품명, 제품 번호
    private Character itemRevision;


    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate startPeriod;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate overPeriod;

    private Boolean tempsave;

    //태그가 개발 사양서
    private List<String> developAttachmentName;

    //태그 디자인 파일일
    private List<String> designAttachmentName;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;

    private String lifecycle;

    private Boolean readonly;

}