package eci.server.ItemModule.dto.route;

import eci.server.ItemModule.dto.item.ItemDto;
import eci.server.ItemModule.entity.item.Item;
import eci.server.ItemModule.entity.route.Route;
import eci.server.ItemModule.exception.item.ItemNotFoundException;
import eci.server.ItemModule.exception.member.sign.MemberNotFoundException;
import eci.server.ItemModule.exception.route.RouteNotFoundException;
import eci.server.ItemModule.repository.item.ItemRepository;
import eci.server.ItemModule.repository.member.MemberRepository;
import eci.server.ItemModule.repository.route.RouteRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Positive;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RouteUpdateRequest {
    @Null
    private String type;
    @Null
    private String workflow;

    private String workflowPhase;
    @Null//develop 이랑 release 중 하나
    private String lifecycleStatus;

    private Integer revisedCnt;

    @Null
    private Long itemId;

    /**
     * 사용자 입력 x
     */
    @Null
    private Long memberId;
    @Null
    private String applicant_comment;

    private Long reviewerId;

    private String reviewer_comment;

    private Long approverId;

    private String approver_comment;

    private Item item;
    @Null
    private Boolean inProgress;


}
