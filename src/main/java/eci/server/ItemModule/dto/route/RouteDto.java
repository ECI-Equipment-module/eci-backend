package eci.server.ItemModule.dto.route;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import eci.server.ItemModule.dto.member.MemberDto;
import eci.server.ItemModule.entity.route.Route;
import eci.server.ItemModule.helper.NestedConvertHelper;
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
    private String lifecycleStatus;
    private int revisedCnt;
    private MemberDto member;
    private String applicant_comment;
    private MemberDto reviewer;
    private String reviewer_comment;
    private MemberDto approver;
    private String approver_comment;
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
                        c.isDeleted() ? null : c.getApplicant_comment(),
                        c.isDeleted() ? null : MemberDto.toDto(c.getReviewer()),
                        c.isDeleted() ? null : c.getReviewer_comment(),
                        c.isDeleted() ? null : MemberDto.toDto(c.getApprover()),
                        c.isDeleted() ? null : c.getApprover_comment(),
                        c.getCreatedAt(),
                        new ArrayList<>()
                        ),
                c -> c.getParent(),
                c -> c.getId(),
                d -> d.getChildren());
        return helper.convert();
    }

    public static RouteDto toDto(Route Route) {

        return new RouteDto(
                Route.getId(),
                Route.getType(),
                Route.getWorkflow(),
                Route.getWorkflowPhase(),
                Route.getLifecycleStatus(),
                Route.getRevisedCnt(),
                MemberDto.toDto(Route.getMember()),
                Route.getApplicant_comment(),
                MemberDto.toDto(Route.getReviewer()),
                Route.getReviewer_comment(),
                MemberDto.toDto(Route.getApprover()),
                Route.getApprover_comment(),
                Route.getCreatedAt(),
                new ArrayList<>()
        );
    }
}