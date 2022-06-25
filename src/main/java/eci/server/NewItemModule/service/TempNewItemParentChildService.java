package eci.server.NewItemModule.service;

import eci.server.BomModule.entity.Bom;
import eci.server.BomModule.repository.BomRepository;
import eci.server.BomModule.repository.DevelopmentBomRepository;
import eci.server.DesignModule.dto.DesignContentDto;
import eci.server.NewItemModule.dto.TempNewItemChildDto;
import eci.server.NewItemModule.entity.NewItem;
import eci.server.NewItemModule.entity.NewItemParentChildrenId;
import eci.server.NewItemModule.entity.TempNewItemParentChildren;
import eci.server.NewItemModule.repository.TempNewItemParentChildrenRepository;
import eci.server.NewItemModule.repository.item.NewItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TempNewItemParentChildService {

    private final TempNewItemParentChildrenRepository tempNewItemParentChildrenRepository;
    private final BomRepository bomRepository;
    private final DevelopmentBomRepository developmentBomRepository;

    public List<TempNewItemChildDto> readTempChildAll(Long id) {

        return TempNewItemChildDto.toDtoList(
                tempNewItemParentChildrenRepository.
                        findAllWithParentByParentId(id),//ByParentIdOrderByParentIdAscNullsFirst(
                tempNewItemParentChildrenRepository

        );

    }

    /**
     * createDevelopmentCard 에서 쓰일 것

     */
    public void recursiveChildrenMaking(
            NewItem parentNewItem,
            List<DesignContentDto> childrenList,
            TempNewItemParentChildrenRepository newItemParentChildrenRepository,
            NewItemRepository newItemRepository
    ) {

        if (childrenList!=null && childrenList.size() > 0) {
            for (DesignContentDto p : childrenList) {

                NewItem children =
                        newItemRepository.findByItemNumber(p.getCardNumber());

                // 디자인 승인 완료 되니깐 ~ 찐 관계 생성
                NewItemParentChildrenId newItemParentChildrenId = new NewItemParentChildrenId(
                        parentNewItem,
                        newItemRepository.findByItemNumber(p.getCardNumber())

                );


                // 06-25 newParentItemId 는

                Long newId = Long.parseLong((parentNewItem.getId().toString()+
                        children.getId().toString()));

                Bom bom = bomRepository.findByNewItem(parentNewItem).get(
                        bomRepository.findByNewItem(parentNewItem).size()-1
                );

                newItemParentChildrenRepository.save(
                        new TempNewItemParentChildren(
                                newId,
                                parentNewItem,
                                children,
                                bom.getId()
                        )
                );


                if (
                        childrenList.get(
                                childrenList.indexOf(p)
                        )!=null
                                &&
                                childrenList.get(
                                        childrenList.indexOf(p)
                                ).getChildren()!=null
                                &&
                                childrenList.get(
                                        childrenList.indexOf(p)
                                ).getChildren().size() > 0
                ) {

                    recursiveChildrenMaking(
                            newItemRepository.findByItemNumber(p.getCardNumber()),
                            childrenList.get(
                                    childrenList.indexOf(p)
                            ).getChildren(),
                            newItemParentChildrenRepository,
                            newItemRepository
                    );
                }

            }
        }
    }





}
