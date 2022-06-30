package eci.server.NewItemModule.dto;

import eci.server.ItemModule.exception.item.ItemNotFoundException;
import eci.server.NewItemModule.entity.NewItem;
import eci.server.NewItemModule.entity.TempNewItemParentChildren;
import eci.server.NewItemModule.repository.TempNewItemParentChildrenRepository;
import eci.server.NewItemModule.repository.item.NewItemRepository;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TempNewItemChildDto {

    private Long cardId;
    private String classification;
    private String cardName;
    private String cardType;
    private String cardNumber;
    //private String thumbnailAddress;
    private String sharing;
    private boolean plusPossible;
    private List<TempNewItemChildDto> children;
    private boolean gray;

    public static TempNewItemChildDto toDevelopmentBomDto(
            NewItem newItem,
            List<TempNewItemChildDto> children
    ){
        return new TempNewItemChildDto(
                newItem.getId(),
                newItem.getClassification().getClassification1().getName()
                        +"/" + newItem.getClassification().getClassification2().getName()+
                        ( newItem.getClassification().getClassification3().getId().equals(99999L)?
                                "":
                                "/" + newItem.getClassification().getClassification3().getName()
                        ),
                newItem.getName(),
                newItem.getItemTypes().getItemType().name(),
                newItem.getItemNumber(),
                newItem.isSharing()?"공용":"전용",
                true,
                children,
                true // 최상위 아이템은 항상 cad 에서 데려온 것이니

        );
    }

    public static List<TempNewItemChildDto> toDtoList(
            Long parentId,
            List<TempNewItemParentChildren> NewItems,
            TempNewItemParentChildrenRepository newItemParentChildrenRepository,
            NewItemRepository newItemRepository
    ) {

        NewItem parent = newItemRepository.findById(parentId).orElseThrow(ItemNotFoundException::new);

        List<TempNewItemChildDto> tempNewItemChildDtos = NewItems.stream().map(
                c -> new TempNewItemChildDto(
                        c.getChildren().getId(),//엔티티를 DTO로 변환하는 함수
                        c.getChildren().getClassification().getClassification1().getName()
                                +"/"+c.getChildren().getClassification().getClassification2().getName()+
                                (
                                        c.getChildren().getClassification().getClassification3().getId()==99999L?
                                                "":"/"+c.getChildren().getClassification().getClassification3().getName()
                                )
                        ,
                        c.getChildren().getName(),
                        c.getChildren().getItemTypes().getItemType().name(),
                        c.getChildren().getItemNumber(),
                        c.getChildren().isSharing()?"공용":"전용",

                        c.getChildren().isSubAssy(),
                        newItemParentChildrenRepository
                                .findAllWithParentByParentId(c.getChildren().getId()).size()>0?
                                toDtoList(
                                        c.getChildren().getId(),
                                        newItemParentChildrenRepository
                                                .findAllWithParentByParentId(c.getChildren().getId()),

                                        newItemParentChildrenRepository,
                                        newItemRepository
                                )
                                :new ArrayList<>(),

                        newItemParentChildrenRepository.findByParentAndChildren(parent, c.getChildren()).isGray()

                )
        )
                .collect
                        (
                                Collectors.toList()
        );

        return tempNewItemChildDtos;

    }
//
//    /**
//     * 봄 없는 애들
//     * @param NewItems
//     * @return
//     */
//    public static Page<TempNewItemChildDto> toGeneralDtoList(
//            List<NewItem> NewItems
//    ) {
//        List<TempNewItemChildDto> tempNewItemChildDtos = NewItems.stream().map(
//                c -> new TempNewItemChildDto(
//                        c.getId(),//엔티티를 DTO로 변환하는 함수
//                        c.getClassification().getClassification1().getName()
//                                +"/"+c.getClassification().getClassification2().getName()+
//                                (
//                                        c.getClassification().getClassification3().getId()==99999L?
//                                                "":"/"+"/"+c.getClassification().getClassification3().getName()
//                                )
//                        ,
//                        c.getName(),
//                        c.getItemTypes().getItemType().name(),
//                        c.getItemNumber(),
//                        c.isSharing()?"공용":"전용",
//                        //c.getThumbnailAddress(),
//                        c.isSubAssy(),
//                        new ArrayList<>()
//
//                )
//        ).collect(Collectors.toList());
//
//        Page<TempNewItemChildDto> itemProductList = new PageImpl<>(tempNewItemChildDtos);
//
//        return itemProductList;
//
//    }
//
//
//    public static Page<TempNewItemChildDto> toAddChildDtoList(
//            Page<NewItem> NewItem,
//            TempNewItemParentChildService newItemService    ) {
//
//
//        List<TempNewItemChildDto> tempNewItemChildDtos = NewItem.stream().map(
//                c -> new TempNewItemChildDto(
//                        c.getId(),//엔티티를 DTO로 변환하는 함수
//                        c.getClassification().getClassification1().getName()
//                                +"/"+c.getClassification().getClassification2().getName()+
//                                (
//                                        c.getClassification().getClassification3().getId()==99999L?
//                                                "":"/"+c.getClassification().getClassification3().getName()
//                                )
//                        ,
//                        c.getName(),
//                        c.getItemTypes().getItemType().name(),
//                        c.getItemNumber(),
//                        c.isSharing()?"공용":"전용",
//                        //c.getThumbnailAddress(),
//                        c.isSubAssy(),
//                        newItemService.readTempChildAll(c.getId())
//
//
//                )
//        ).collect(Collectors.toList());
//
//        Page<TempNewItemChildDto> itemProductList = new PageImpl<>(tempNewItemChildDtos);
//        return itemProductList;
//
//    }

}
