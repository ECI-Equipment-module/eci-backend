package eci.server.NewItemModule.dto.newItem;

import com.fasterxml.jackson.annotation.JsonFormat;
import eci.server.NewItemModule.entity.NewItem;
import eci.server.NewItemModule.entity.NewItemParentChildren;
import eci.server.NewItemModule.repository.item.NewItemParentChildrenRepository;
import eci.server.NewItemModule.repository.item.NewItemRepository;
import eci.server.NewItemModule.service.item.NewItemService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewItemChildDto {

    private Long cardId;
    private String classification;
    private String cardName;
    private String cardType;
    private String cardNumber;

    private String sharing;
    private boolean plusPossible;
    private List<NewItemChildDto> children;

    private String imageAddress;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;


    /**
     * 배포에서 아이템 검색할 때 - 아이템 Revision 이 65 인 아이들만 가능 (초도배포)
     * @param NewItems
     * @return
     */
    public static Page<NewItemChildDto> toDtoList(
            Page<NewItem> NewItems,
            String defaultImageAddress
    ) {
        List<NewItemChildDto> newItemChildDtos = NewItems.stream().map(
                c-> new NewItemChildDto(
                        c.getId(),//엔티티를 DTO로 변환하는 함수
                        c.getClassification().getClassification1().getName()
                                +"/"+c.getClassification().getClassification2().getName()+
                                (
                                        c.getClassification().getClassification3().getId()==99999L?
                                                "":"/"+"/"+c.getClassification().getClassification3().getName()
                                )
                        ,
                        c.getName(),
                        c.getItemTypes().getItemType().name(),
                        c.getItemNumber(),
                        c.isSharing()?"공용":"전용",
                        c.isSubAssy(),
                        new ArrayList<>(),
                        c.getThumbnail()==null?defaultImageAddress:c.getThumbnail().getImageaddress(),
                        c.getCreatedAt()
                )
        ).collect(Collectors.toList());

        Page<NewItemChildDto> itemProductList = new PageImpl<>(newItemChildDtos);
        return itemProductList;

    }
    public static List<NewItemChildDto> toDtoList(
            List<NewItemParentChildren> NewItems,
            NewItemParentChildrenRepository newItemParentChildrenRepository,
            String defaultImageAddress
    ) {
        List<NewItemChildDto> newItemChildDtos = NewItems.stream().map(
                c -> new NewItemChildDto(
                        c.getChildren().getId(),//엔티티를 DTO로 변환하는 함수
                        c.getChildren().getClassification().getClassification1().getName()
                                +"/"+c.getChildren().getClassification().getClassification2().getName()+
                                (
                                        c.getChildren().getClassification().getClassification3().getId()==99999L?
                                                "":"/"+"/"+c.getChildren().getClassification().getClassification3().getName()
                                )
                        ,
                        c.getChildren().getName(),
                        c.getChildren().getItemTypes().getItemType().name(),
                        c.getChildren().getItemNumber(),
                        c.getChildren().isSharing()?"공용":"전용",
                        //c.getThumbnailAddress(),
                        c.getChildren().isSubAssy(),
                        c.getChildren().getChildren().size()>0?
                                toDtoList(newItemParentChildrenRepository
                                                .findAllWithParentByParentId(c.getChildren().getId()),

                                        newItemParentChildrenRepository

                                , defaultImageAddress):new ArrayList<>(),

                        c.getChildren().getThumbnail()==null?defaultImageAddress:c.getChildren().getThumbnail().getImageaddress(),
                        c.getChildren().getCreatedAt()

                )
        ).collect(Collectors.toList());

        return newItemChildDtos;

    }

    /**
     * 봄 없는 애들
     * @param NewItems
     * @return
     */
    public static Page<NewItemChildDto> toGeneralDtoList(
            List<NewItem> NewItems
    ) {
        List<NewItemChildDto> newItemChildDtos = NewItems.stream().map(
                c -> new NewItemChildDto(
                        c.getId(),//엔티티를 DTO로 변환하는 함수
                        c.getClassification().getClassification1().getName()
                                +"/"+c.getClassification().getClassification2().getName()+
                                (
                                        c.getClassification().getClassification3().getId()==99999L?
                                                "":"/"+"/"+c.getClassification().getClassification3().getName()
                                )
                        ,
                        c.getName(),
                        c.getItemTypes().getItemType().name(),
                        c.getItemNumber(),
                        c.isSharing()?"공용":"전용",
                        //c.getThumbnailAddress(),
                        c.isSubAssy(),
                        new ArrayList<>(),
                        c.getThumbnail().getImageaddress(),
                        c.getCreatedAt()

                )
        ).collect(Collectors.toList());

        Page<NewItemChildDto> itemProductList = new PageImpl<>(newItemChildDtos);

        return itemProductList;

    }


    public static Page<NewItemChildDto> toAddChildDtoList(
            Page<NewItem> NewItem,
            NewItemService newItemService,
            String defaultImageAddress
            ) {


        List<NewItemChildDto> newItemChildDtos = NewItem.stream().map(
                c -> new NewItemChildDto(
                        c.getId(),//엔티티를 DTO로 변환하는 함수
                        c.getClassification().getClassification1().getName()
                                +"/"+c.getClassification().getClassification2().getName()+
                                (
                                        c.getClassification().getClassification3().getId()==99999L?
                                                "":"/"+c.getClassification().getClassification3().getName()
                                )
                        ,
                        c.getName(),
                        c.getItemTypes().getItemType().name(),
                        c.getItemNumber(),
                        c.isSharing()?"공용":"전용",
                        //c.getThumbnailAddress(),
                        c.isSubAssy(),
                        newItemService.readChildAll(c.getId()),
                        c.getThumbnail()==null?defaultImageAddress:c.getThumbnail().getImageaddress(),
                        c.getCreatedAt()


                )
        ).collect(Collectors.toList());

        Page<NewItemChildDto> itemProductList = new PageImpl<>(newItemChildDtos);
        return itemProductList;

    }

}
