package eci.server.ProjectModule.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import eci.server.ItemModule.dto.item.ItemDto;
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
    private ItemDto item;
    //제품 타입, 제품명, 제품 번호
    private char revision;
    private LocalDate startPeriod;
    private LocalDate overPeriod;

    private MemberDto member;
    private Boolean tempsave;

    //개발 사양서
    //디자인 파일은 -> 이 프로젝트를 참조하는 디자인의 파일로 데려오기
    private List<ProjectAttachmentDto> projectAttachments;


}