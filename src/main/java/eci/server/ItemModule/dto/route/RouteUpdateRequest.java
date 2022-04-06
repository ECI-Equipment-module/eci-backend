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
    @NotBlank(message = "라우트의 타입을 입력해주세요")
    private String type;

    @NotBlank(message = "라우트의 workflow를 지정해주세요")
    private String workflow;

    @Null
    private String workflowPhase;

    private String lifecycleStatus;

    private Integer revisedCnt;

    @NotNull(message = "아이템 아이디를 입력해주세요")
    @Positive(message = "올바른 아이템 아이디를 입력해주세요")
    private Long itemId;

    /**
     * 사용자 입력 x
     */
    @Null
    private Long memberId;

    private String applicant_comment;

    private Long reviewerId;

    private String reviewer_comment;

    private Long approverId;

    private String approver_comment;

    private Item item;

    private Boolean inProgress;


}
