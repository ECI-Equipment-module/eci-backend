package eci.server.NewItemModule.dto;

import eci.server.ItemModule.entity.item.ItemTypes;
import eci.server.NewItemModule.dto.attachment.AttachmentTagDto;
import eci.server.NewItemModule.entity.activateAttributes.ActivateAttributes;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemTypesDto {
    private Long id;
    private String name;

    public static ItemTypesDto toDto(
            ItemTypes itemTypes
    ){
        return new ItemTypesDto(
                itemTypes.getId(),
                itemTypes.getItemType().name()
        );
    }

    public static List<ItemTypesDto> toDtoList(
            List <ItemTypes> itemTypes
    ) {
        List<ItemTypesDto> itemTypesDtoList
                = itemTypes.stream().map(
                a -> new ItemTypesDto(
                        a.getId(),
                        a.getItemType().name()
                )
        ).collect(
                toList()
        );
        return itemTypesDtoList;
    }
}

