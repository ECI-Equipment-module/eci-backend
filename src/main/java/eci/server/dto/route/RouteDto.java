package eci.server.dto.route;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import eci.server.dto.member.MemberDto;
import eci.server.entity.route.Route;
import eci.server.helper.NestedConvertHelper;
import eci.server.repository.route.RouteRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RouteDto {

    private Long id;
    private String type;
    private String workflow;
    private String workflowPhase;
    private Character lifecycleStatus;
    private Integer revisedCnt;
    private MemberDto member;
    private MemberDto reviewer;
    private MemberDto approver;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;
    private List<RouteDto> children;

    public static List<RouteDto> toDtoList(List<Route> Routes) {
        NestedConvertHelper helper = NestedConvertHelper.newInstance(
                Routes,
                c -> new RouteDto(
                        c.getId(),
                        c.isDeleted() ? null : c.getType(),
                        c.isDeleted() ? null : c.getWorkflow(),
                        c.isDeleted() ? null : c.getWorkflowPhase(),
                        c.isDeleted() ? null : c.getLifecycleStatus(),
                        c.isDeleted() ? null : c.getRevisedCnt(),
                        c.isDeleted() ? null : MemberDto.toDto(c.getMember()),
                        c.isDeleted() ? null : MemberDto.toDto(c.getReviewer()),
                        c.isDeleted() ? null : MemberDto.toDto(c.getApproval()),
                        c.getCreatedAt(),
                        new ArrayList<>()
                        ),
                c -> c.getParent(),
                c -> c.getId(),
                d -> d.getChildren());
        return helper.convert();
    }
}