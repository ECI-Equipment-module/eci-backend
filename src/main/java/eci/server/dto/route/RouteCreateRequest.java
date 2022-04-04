package eci.server.dto.route;

import eci.server.entity.route.Route;
import eci.server.exception.item.ItemNotFoundException;
import eci.server.exception.member.sign.MemberNotFoundException;
import eci.server.exception.route.RouteNotFoundException;
import eci.server.repository.item.ItemRepository;
import eci.server.repository.member.MemberRepository;
import eci.server.repository.route.RouteRepository;
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

    @Null
    private String workflowPhase;

    private Character lifecycleStatus;

    private Integer revisedCnt;

    @NotNull(message = "아이템 아이디를 입력해주세요")
    @Positive(message = "올바른 아이템 아이디를 입력해주세요")
    private Long itemId;

    /**
     * 사용자 입력 x
     */
    @Null
    private Long memberId;

    private Long reviewerId;

    private Long approverId;

    private Long parentId;

    public static Route toEntity(RouteCreateRequest req, MemberRepository memberRepository, ItemRepository itemRepository, RouteRepository routeRepository) {
        return new Route(
                req.type,
                req.workflow,
                req.workflow, //workflow 설정하면 그것에 맞는 이미지 파일 돌려주기
                req.lifecycleStatus,
                req.revisedCnt+64, //revistion  A B C D
                memberRepository.findById(req.memberId).orElseThrow(MemberNotFoundException::new),
                memberRepository.findById(req.reviewerId).orElseThrow(MemberNotFoundException::new),
                memberRepository.findById(req.approverId).orElseThrow(MemberNotFoundException::new),
                itemRepository.findById(req.itemId).orElseThrow(ItemNotFoundException::new),
                Optional.ofNullable(req.parentId)
                        .map(id -> routeRepository.findById(id).orElseThrow(RouteNotFoundException::new))
                        .orElse(null)
        );
    }
}