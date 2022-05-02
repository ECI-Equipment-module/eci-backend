package eci.server.ItemModule.dto.item;

import eci.server.ItemModule.entity.item.Item;
import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class ItemProjectDto {

    private Long id;
    private String name;
    private String type;
    private Integer itemNumber;


    public static ItemProjectDto toDto(Item Item) {

        return new ItemProjectDto(

                Item.getId(),
                Item.getName(),
                Item.getType(),
                Item.getItemNumber()

        );
    }

}
