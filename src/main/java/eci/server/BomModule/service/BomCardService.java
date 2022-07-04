package eci.server.BomModule.service;

import eci.server.BomModule.dto.BomDto;
import eci.server.BomModule.dto.dev.DevelopmentBomCardCreateRequest;
import eci.server.BomModule.dto.dev.DevelopmentBomCardDto;
import eci.server.BomModule.dto.cond.DevelopmentBomReadCondition;
import eci.server.BomModule.entity.Bom;
import eci.server.BomModule.entity.DevelopmentBom;
import eci.server.BomModule.entity.DevelopmentBomCard;
import eci.server.BomModule.exception.BomNotFoundException;
import eci.server.BomModule.exception.DevelopmentCardNotFoundException;
import eci.server.BomModule.repository.*;
import eci.server.ItemModule.dto.newRoute.routeOrdering.RouteOrderingCreateResponse;
import eci.server.ItemModule.repository.newRoute.RouteTypeRepository;
import eci.server.NewItemModule.repository.item.NewItemRepository;
import eci.server.config.guard.BomGuard;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BomCardService {
    private final RouteTypeRepository routeTypeRepository;
    private final BomRepository bomRepository;
    private final DevelopmentBomRepository DevelopmentBomRepository;
    private final PreliminaryBomRepository preliminaryBomRepository;
    private final CompareBomRepository compareBomRepository;
    private final DevelopmentBomCardRepository DevelopmentBomCardRepository;
    private final NewItemRepository newItemRepository;

    public BomGuard getBomGuard() {
        return bomGuard;
    }

    private final BomGuard bomGuard;

    /**
     * 0613 : dev BOM 에서 데이터 추가하는 경우에 USE 할 것
     * @param req
     * @return
     */
    @Transactional
    //최초 카드의 아이디만 돌려주면 된다.
    public RouteOrderingCreateResponse createDevelopmentCard(DevelopmentBomCardCreateRequest req) {

        DevelopmentBomCard first = DevelopmentBomCardRepository.save(
                DevelopmentBomCardCreateRequest.toParentEntity(
                        req,
                        DevelopmentBomRepository,
                        DevelopmentBomCardRepository,
                        newItemRepository

                )
        );

        //얘 자식리스트는 이것들은 공통으로 가질 것
        Long parentId = first.getId(); //이건 갱신될 값
        DevelopmentBom DevelopmentBom = first.getDevelopmentBom(); //갱신 안 됨, 고정임

        recursiveChildrenMaking(
                parentId,
                req.getChildren(),
                DevelopmentBom,
                DevelopmentBomRepository
        );

        return new RouteOrderingCreateResponse(first.getId());
    }

    /**
     * createDevelopmentCard 에서 쓰일 것
     * @param parentId
     * @param childrenList
     * @param DevelopmentBom
     * @param DevelopmentBomRepository
     */
    public void recursiveChildrenMaking(
            Long parentId,
            List<DevelopmentBomCardCreateRequest> childrenList,
            DevelopmentBom DevelopmentBom,
            DevelopmentBomRepository DevelopmentBomRepository
    ) {

        if (childrenList!=null && childrenList.size() > 0) {
            List<DevelopmentBomCard> toChildrenList =
                    DevelopmentBomCardCreateRequest.toChildrenList(
                            parentId,
                            childrenList,
                            DevelopmentBom,
                            DevelopmentBomCardRepository,
                            newItemRepository
                    );

            for (DevelopmentBomCard p : toChildrenList) {
                DevelopmentBomCard child =
                        DevelopmentBomCardRepository.save(p);

                if (
                        childrenList.get(
                        toChildrenList.indexOf(p)
                        )!=null
                                &&
                                childrenList.get(
                                        toChildrenList.indexOf(p)
                                ).getChildren()!=null
                    &&
                                childrenList.get(
                        toChildrenList.indexOf(p)
                ).getChildren().size() > 0
                ) {

                    recursiveChildrenMaking(
                            p.getId(),
                            childrenList.get(
                                    toChildrenList.indexOf(p)
                            ).getChildren(),
                            DevelopmentBom,
                            DevelopmentBomRepository
                    );
                }

            }
        }
    }

    public List<DevelopmentBomCardDto> readDevelopmentAll(DevelopmentBomReadCondition cond) {
        return DevelopmentBomCardDto.toDtoList(
                DevelopmentBomCardRepository.
                        findAllWithParentByDevelopmentBomIdOrderByParentIdAscNullsFirstPreliminaryBomIdAsc
                        (
                                cond.getDevelopmentBomId()
                        )
        );
    }


    @Transactional
//    1) findDeletable~ 조회된 결과가 있다면 DB 실제로 제거
//    2) delete 를 호출하여 삭제 표시
    public void delete(Long id) {
        DevelopmentBomCard DevelopmentBomCard = DevelopmentBomCardRepository.
                findById(id).orElseThrow(DevelopmentCardNotFoundException::new);
        DevelopmentBomCard.findDeletableDevelopmentBomCard().ifPresentOrElse(
                DevelopmentBomCardRepository::delete,
                DevelopmentBomCard::delete
        );
    }

    @Transactional
    public BomDto read(Long id){

        Bom bom = bomRepository.findById(id).orElseThrow(BomNotFoundException::new);

        return BomDto.toDto(
            bom.getNewItem(),
                bom,
                bomGuard,
                preliminaryBomRepository,
                DevelopmentBomRepository,
                compareBomRepository
        );

    }


}
