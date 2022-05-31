package eci.server.NewItemModule.dto.item;

import eci.server.NewItemModule.entity.NewItem;
import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class ItemProjectDto {

    private Long id;
    private String name;
    private String type;
    private String itemNumber;
    private int revision;


    public static ItemProjectDto toDto(NewItem Item) {

        return new ItemProjectDto(

                Item.getId(),
                Item.getName(),
                Item.getItemTypes().getItemType().toString(),
                Item.getItemNumber(),
                Item.getRevision()

        );
    }

}
