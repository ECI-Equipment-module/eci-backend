package eci.server.NewItemModule.dto.newItem;

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
public class NewItemParentDto {

    private Long id;
    private String itemClassification;
    private String itemName;
    private String thumbNail;
    private String name;
    private List<NewItemParentDto> children;


    public static List<NewItemParentDto> toDtoList(
            List<NewItemParentChildren> NewItems,
            NewItemParentChildrenRepository newItemParentChildrenRepository
    ) {
        List<NewItemParentDto> newItemParentDtos = NewItems.stream().map(
                c -> new NewItemParentDto(
                        c.getParent().getId(),//엔티티를 DTO로 변환하는 함수
                        c.getParent().getClassification().getClassification1().getName()
                                +"/"+c.getParent().getClassification().getClassification2().getName()+
                                (
                                        c.getParent().getClassification().getClassification3().getId()==99999L?
                                                "":"/"+"/"+c.getParent().getClassification().getClassification3().getName()
                                )
                        ,
                        c.getParent().getName(),
                        c.getParent().getThumbnail().getImageaddress(),
                        c.getParent().getItemTypes().getItemType().name(),
                        c.getParent().getParent().size()>0?
                                toDtoList(newItemParentChildrenRepository
                                                .findAllWithChildByChildId(c.getParent().getId()),
                                        newItemParentChildrenRepository)
                                :new ArrayList<>()

                )
        ).collect(Collectors.toList());

        return newItemParentDtos;

    }


}
