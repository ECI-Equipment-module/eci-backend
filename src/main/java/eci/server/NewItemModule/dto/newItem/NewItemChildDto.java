package eci.server.NewItemModule.dto.newItem;

import eci.server.ItemModule.helper.NestedConvertHelper;
import eci.server.NewItemModule.entity.NewItemParentChildren;
import eci.server.NewItemModule.repository.item.NewItemParentChildrenRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewItemChildDto {

    private Long id;
    private String classification;
    private String name;
    private String itemTypes;
    private String itemNumber;
    //private String thumbnailAddress;
    private String sharing;
    private List<NewItemChildDto> children;


    public static List<NewItemChildDto> toDtoList(
            List<NewItemParentChildren> NewItems,
            NewItemParentChildrenRepository newItemParentChildrenRepository
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
                        c.getChildren().getChildren().size()>0?
                                toDtoList(newItemParentChildrenRepository.findAllWithParentByParentId(c.getChildren().getId()),
                                        newItemParentChildrenRepository):new ArrayList<>()

                )
        ).collect(Collectors.toList());

        return newItemChildDtos;

    }

}
