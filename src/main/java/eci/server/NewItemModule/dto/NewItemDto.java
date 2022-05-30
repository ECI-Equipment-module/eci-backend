package eci.server.NewItemModule.dto;

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
public class NewItemDto {

    private Long id;
    private List<NewItemImageDto> thumbnail;
    private Integer itemNumber;
    private String name;
    private ItemType type;
    private Classification classification;
    private boolean share;


    public static NewItemDto toDto(NewItem newItem) {

        return new NewItemDto(

                newItem.getId(),

                newItem.getThumbnail().
                        stream().
                        map(NewItemImageDto::toDto).collect(toList()),

                newItem.getItemNumber(),

                newItem.getName(),

                newItem.getItemTypes().getItemType(),

                newItem.getClassification(),

                newItem.isShare()
        );

    }
}