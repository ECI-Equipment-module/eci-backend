package eci.server.ItemModule.dto.route;

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
import java.util.Optional;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RouteCreateRequest {

    @NotBlank(message = "라우트의 타입을 입력해주세요")
    private String type;

    @NotBlank(message = "라우트의 workflow를 지정해주세요")
    private String workflow;

    @Null//workflow에 맞는 사진
    private String workflowPhase;

    @Null//in_progress, Reject, waiting approval, Complete
    private String lifecycleStatus;
    @Null
    private Integer revisedCnt;

    @NotNull(message = "아이템 아이디를 입력해주세요")
    @Positive(message = "올바른 아이템 아이디를 입력해주세요")
    private Long itemId;

    /**
     * 사용자 입력 x
     */
    @Null
    private Long memberId;

    //    @NotBlank(message = "요청 코멘트를 입력해주세요")
    private String applicant_comment;

    private Long reviewerId;

    //    @NotBlank(message = "리뷰 코멘트를 입력해주세요")
    @Null
    private String reviewer_comment;

    private Long approverId;

    //    @NotBlank(message = "승인 여부 코멘트를 입력해주세요")
    @Null
    private String approver_comment;

    private Long parentId;
    @Null
    private Boolean inProgress;

    public static Route toEntity(
            RouteCreateRequest req,
            MemberRepository memberRepository,
            ItemRepository itemRepository,
            RouteRepository routeRepository) {

        return new Route(
                req.type,
                req.workflow,
                "IN_PROGRESS", //workflow 설정하면 그것에 맞는 이미지 파일 돌려주기
                "DEVELOP",
                0+65, //revisedCnt+65  (0)A (1)B C D
                memberRepository.findById(req.memberId).orElseThrow(MemberNotFoundException::new),
                req.applicant_comment,
                memberRepository.findById(req.reviewerId).orElseThrow(MemberNotFoundException::new),
                "",//req.reviewer_comment,//빈칸
                memberRepository.findById(req.approverId).orElseThrow(MemberNotFoundException::new),
                "needed",//req.approver_comment,//빈칸
                itemRepository.findById(req.itemId).orElseThrow(ItemNotFoundException::new),
                Optional.ofNullable(req.parentId)
                        .map(id -> routeRepository.findById(id).orElseThrow(RouteNotFoundException::new))
                        .orElse(null),
                true
        );
    }
}