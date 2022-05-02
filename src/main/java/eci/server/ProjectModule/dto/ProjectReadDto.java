package eci.server.ProjectModule.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import eci.server.ItemModule.dto.item.ItemDto;
import eci.server.ItemModule.dto.item.ItemProjectDto;
import eci.server.ItemModule.dto.member.MemberDto;
import eci.server.ProjectModule.dto.projectAttachmentDto.ProjectAttachmentDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectReadDto {
    private Long id;
    private String projectNumber;
    private String name;
    private String carType;

    private String itemType;
    private String itemName;
    private Integer itemNum;

    //제품 타입, 제품명, 제품 번호
    private Character revision;
    private LocalDate startPeriod;
    private LocalDate overPeriod;

    private Boolean tempsave;


}