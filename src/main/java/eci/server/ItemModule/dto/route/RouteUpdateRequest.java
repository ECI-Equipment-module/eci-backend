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
import java.util.ArrayList;
import java.util.Optional;

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

    public static Route toEntity(RouteUpdateRequest req, MemberRepository memberRepository, ItemRepository itemRepository, RouteRepository routeRepository) {
        return new Route(
                req.type,
                req.workflow,
                req.workflow, //workflow 설정하면 그것에 맞는 이미지 파일 돌려주기
                req.lifecycleStatus,
                req.revisedCnt+64, //revistion  A B C D
                memberRepository.findById(req.memberId).orElseThrow(MemberNotFoundException::new),
                req.applicant_comment,
                memberRepository.findById(req.reviewerId).orElseThrow(MemberNotFoundException::new),
                req.reviewer_comment,
                memberRepository.findById(req.approverId).orElseThrow(MemberNotFoundException::new),
                req.approver_comment,
                itemRepository.findById(req.itemId).orElseThrow(ItemNotFoundException::new),
                null
        );
    }
}
