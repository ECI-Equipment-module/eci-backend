package eci.server.ItemModule.dto.route;

import eci.server.ItemModule.entity.item.Item;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Null;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class RouteApproverUpdateRequest {

    private String type;
    @Null
    private String workflow;
    @Null
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
    @Null
    private Long reviewerId;
    @Null
    private String reviewer_comment;
    @Null
    private Long approverId;

    private String approver_comment;
    @Null
    private Item item;
    @Null
    private Boolean inProgress;

}



