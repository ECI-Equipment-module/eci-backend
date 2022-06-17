package eci.server.DashBoardModule.dto.bomTodo;

import eci.server.BomModule.entity.Bom;
import eci.server.DesignModule.entity.design.Design;
//import eci.server.ItemModule.entity.item.Item;
import eci.server.NewItemModule.entity.NewItem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BomTodoResponse {
    Long bomId;
    String itemName;
    String itemType;
    String itemNumber;


    public static BomTodoResponse toDto(
            NewItem item,
            Bom bom
    ) {

        return new BomTodoResponse(

                bom.getId(),
                item.getName(),
                item.getItemTypes().getItemType().name(),
                item.getItemNumber()

        );
    }


}

