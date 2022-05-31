package eci.server.NewItemModule.dto.newItem;

import eci.server.ItemModule.entity.item.ItemType;
import eci.server.NewItemModule.dto.image.NewItemImageDto;
import eci.server.NewItemModule.entity.NewItem;
import eci.server.NewItemModule.entity.classification.Classification;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Data
@AllArgsConstructor
public class NewItemPagingDto {

    private Long id;
    private List<NewItemImageDto> thumbnail;
    private String itemNumber;
    private String name;
    private ItemType type;
    private Classification classification;
    private boolean share;


    public static NewItemPagingDto toDto(NewItem newItem) {

        return new NewItemPagingDto(

                newItem.getId(),

                newItem.getThumbnail().
                        stream().
                        map(NewItemImageDto::toDto).collect(toList()),

                newItem.getItemNumber(),

                newItem.getName(),

                newItem.getItemTypes().getItemType(),

                newItem.getClassification(),

                newItem.isSharing()
        );

    }
}