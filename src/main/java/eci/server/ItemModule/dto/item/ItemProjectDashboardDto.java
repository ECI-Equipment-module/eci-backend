package eci.server.ItemModule.dto.item;

//import eci.server.ItemModule.entity.item.Item;
import eci.server.NewItemModule.entity.NewItem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemProjectDashboardDto {

    private Long id;
    private String name;
    private String itemNumber;


    public static ItemProjectDashboardDto toDto(NewItem Item) {

        return new ItemProjectDashboardDto(

                Item.getId(),
                Item.getName(),
                Item.getItemNumber()

        );
    }

    public static ItemProjectDashboardDto toDto() {

        return new ItemProjectDashboardDto(

                -1L,
                "",
                ""

        );
    }

}
