package eci.server.BomModule.service;

import eci.server.BomModule.dto.PreliminaryBomCardCreateRequest;
import eci.server.BomModule.entity.Bom;
import eci.server.BomModule.entity.PreliminaryBom;
import eci.server.BomModule.entity.PreliminaryBomCard;
import eci.server.BomModule.repository.BomRepository;
import eci.server.BomModule.repository.PreliminaryBomCardRepository;
import eci.server.BomModule.repository.PreliminaryBomRepository;
import eci.server.ItemModule.dto.newRoute.routeOrdering.RouteOrderingCreateRequest;
import eci.server.ItemModule.dto.newRoute.routeOrdering.RouteOrderingCreateResponse;
import eci.server.ItemModule.dto.newRoute.routeProduct.RouteProductCreateRequest;
import eci.server.ItemModule.entity.newRoute.RouteOrdering;
import eci.server.ItemModule.entity.newRoute.RouteProduct;
import eci.server.ItemModule.exception.member.MemberNotFoundException;
import eci.server.ItemModule.repository.item.ItemTypesRepository;
import eci.server.ItemModule.repository.newRoute.RouteTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BomService {
    private final RouteTypeRepository routeTypeRepository;
    private final BomRepository bomRepository;
    private final PreliminaryBomRepository preliminaryBomRepository;
    private final PreliminaryBomCardRepository preliminaryBomCardRepository;

    @Transactional
    //최초 카드의 아이디만 돌려주면 된다.
    public RouteOrderingCreateResponse createCard(PreliminaryBomCardCreateRequest req) {

        PreliminaryBomCard first = preliminaryBomCardRepository.save(
                PreliminaryBomCardCreateRequest.toParentEntity(
                        req,
                        preliminaryBomRepository,
                        preliminaryBomCardRepository
                )
        );

        //얘 자식리스트는 이것들은 공통으로 가질 것
        Long parentId = first.getId(); //이건 갱신될 값
        PreliminaryBom preliminaryBom = first.getPreliminaryBom(); //갱신 안 됨, 고정임

        recursiveChildrenMaking(
                parentId,
                req.getChildren(),
                preliminaryBom,
                preliminaryBomCardRepository
        );

        return new RouteOrderingCreateResponse(first.getId());
    }

    public void recursiveChildrenMaking(
            Long parentId,
            List<PreliminaryBomCardCreateRequest> childrenList,
            PreliminaryBom preliminaryBom,
            PreliminaryBomCardRepository preliminaryBomCardRepository
    ) {
        if (childrenList.size() > 0) {
            List<PreliminaryBomCard> toChildrenList =
                    PreliminaryBomCardCreateRequest.toChildrenList(
                            parentId,
                            childrenList,
                            preliminaryBom,
                            preliminaryBomCardRepository
                    );

            for (PreliminaryBomCard p : toChildrenList) {
                PreliminaryBomCard child =
                        preliminaryBomCardRepository.save(p);
                if (childrenList.get(
                        toChildrenList.indexOf(p)
                ).getChildren().size() > 0) {
                    recursiveChildrenMaking(
                            p.getId(),
                            childrenList.get(
                                    toChildrenList.indexOf(p)
                            ).getChildren(),
                            preliminaryBom,
                            preliminaryBomCardRepository
                    );
                }

            }
        }
    }

}
