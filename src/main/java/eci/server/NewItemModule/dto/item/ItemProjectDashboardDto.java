package eci.server.NewItemModule.dto.item;

import eci.server.NewItemModule.entity.NewItem;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ItemProjectDashboardDto {

    private Long id;
    private String name;
    private Integer itemNumber;


    public static ItemProjectDashboardDto toDto(NewItem Item) {

        return new ItemProjectDashboardDto(

                Item.getId(),
                Item.getName(),
                Item.getItemNumber()

        );
    }
}
