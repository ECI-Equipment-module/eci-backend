package eci.server.BomModule.service;

import eci.server.BomModule.dto.BomDto;
import eci.server.BomModule.dto.PreliminaryBomCardCreateRequest;
import eci.server.BomModule.dto.PreliminaryBomCardDto;
import eci.server.BomModule.dto.cond.PreliminaryBomReadCondition;
import eci.server.BomModule.entity.Bom;
import eci.server.BomModule.entity.PreliminaryBom;
import eci.server.BomModule.entity.PreliminaryBomCard;
import eci.server.BomModule.exception.BomNotFoundException;
import eci.server.BomModule.exception.PreliminaryBomCardNotFoundException;
import eci.server.BomModule.repository.BomRepository;
import eci.server.BomModule.repository.PreliminaryBomCardRepository;
import eci.server.BomModule.repository.PreliminaryBomRepository;
import eci.server.ItemModule.dto.newRoute.routeOrdering.RouteOrderingCreateResponse;
import eci.server.ItemModule.repository.newRoute.RouteTypeRepository;
import eci.server.config.guard.BomGuard;
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
    private final BomGuard bomGuard;

    @Transactional
    //최초 카드의 아이디만 돌려주면 된다.
    public RouteOrderingCreateResponse createPreliminaryCard(PreliminaryBomCardCreateRequest req) {

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

        if (childrenList!=null && childrenList.size() > 0) {
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
                            preliminaryBom,
                            preliminaryBomCardRepository
                    );
                }

            }
        }
    }

    public List<PreliminaryBomCardDto> readPreliminaryAll(PreliminaryBomReadCondition cond) {
        return PreliminaryBomCardDto.toDtoList(
                preliminaryBomCardRepository.
                        findAllWithParentByPreliminaryBomIdOrderByParentIdAscNullsFirstPreliminaryBomIdAsc(
                                cond.getPreliminaryBomId()
                        )
        );
    }


    @Transactional
//    1) findDeletabl~ 조회된 결과가 있다면 DB 실제로 제거
//    2) delete를 호출하여 삭제 표시
    public void delete(Long id) {
        PreliminaryBomCard preliminaryBomCard = preliminaryBomCardRepository.findById(id).orElseThrow(PreliminaryBomCardNotFoundException::new);
        preliminaryBomCard.findDeletablePreliminaryBomCard().ifPresentOrElse(
                preliminaryBomCardRepository::delete,
                preliminaryBomCard::delete
        );
    }

    @Transactional
    public BomDto read(Long id){

        Bom bom = bomRepository.findById(id).orElseThrow(BomNotFoundException::new);


        return BomDto.toDto(
            bom.getNewItem(),
                bom,
                bomGuard,
                preliminaryBomRepository
        );
    }


}
