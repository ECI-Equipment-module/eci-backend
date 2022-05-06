package eci.server.ItemModule.dto.item;

import eci.server.ItemModule.entity.item.Item;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ItemProjectDashboardDto {

    private Long id;
    private String name;
    private Integer itemNumber;


    public static ItemProjectDashboardDto toDto(Item Item) {

        return new ItemProjectDashboardDto(

                Item.getId(),
                Item.getName(),
                Item.getItemNumber()

        );
    }
}
