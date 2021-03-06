package eci.server.NewItemModule.dto;

import eci.server.ItemModule.entity.newRoute.RouteOrdering;
import eci.server.ItemModule.entity.newRoute.RouteProduct;
import eci.server.ItemModule.exception.item.ItemNotFoundException;
import eci.server.ItemModule.repository.newRoute.RouteProductRepository;
import eci.server.NewItemModule.entity.NewItem;
import eci.server.NewItemModule.entity.TempNewItemParentChildren;
import eci.server.NewItemModule.repository.TempNewItemParentChildrenRepository;
import eci.server.NewItemModule.repository.item.NewItemRepository;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.autoconfigure.amqp.RabbitTemplateConfigurer;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
    //private Long routeId;
    private boolean preRejected;

    public static TempNewItemChildDto toDevelopmentBomDto(
            RouteOrdering routeOrdering,
            RouteProductRepository routeProductRepository,
            NewItem newItem,
            List<TempNewItemChildDto> children,
            Long routeId
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
                newItem.isSharing()?"??????":"??????",
                true,
                children,
                true, // ????????? ???????????? ?????? cad ?????? ????????? ?????????
                //routeId
                DevPreRejected(routeOrdering, routeProductRepository)

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
                        c.getChildren().getId(),//???????????? DTO??? ???????????? ??????
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
                        c.getChildren().isSharing()?"??????":"??????",

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

                        newItemParentChildrenRepository.findByParentAndChildren(parent, c.getChildren()).isGray(),
                        false//preRejected ??? ??????????????? ???????????? ?????? ??????

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
//     * ??? ?????? ??????
//     * @param NewItems
//     * @return
//     */
//    public static Page<TempNewItemChildDto> toGeneralDtoList(
//            List<NewItem> NewItems
//    ) {
//        List<TempNewItemChildDto> tempNewItemChildDtos = NewItems.stream().map(
//                c -> new TempNewItemChildDto(
//                        c.getId(),//???????????? DTO??? ???????????? ??????
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
//                        c.isSharing()?"??????":"??????",
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
//                        c.getId(),//???????????? DTO??? ???????????? ??????
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
//                        c.isSharing()?"??????":"??????",
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

    private static boolean DevPreRejected(RouteOrdering routeOrdering,
                                             RouteProductRepository routeProductRepository){

        boolean preRejected = false;

        List<RouteProduct> routeProductList =
                routeProductRepository.findAllByRouteOrdering(routeOrdering);

        if(!(routeOrdering.getPresent()==routeProductList.size())) {
            RouteProduct currentRouteProduct =
                    routeProductList.get(routeOrdering.getPresent());

            if (Objects.equals(currentRouteProduct.getType().getModule(), "BOM") &&
                    Objects.equals(currentRouteProduct.getType().getName(), "CREATE")) {

                if (currentRouteProduct.isPreRejected()) {
                    preRejected = true;
                }
            }
        }
        return preRejected;
    }

}
