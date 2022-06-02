package eci.server.DashBoardModule.dto.bomTodo;

import eci.server.BomModule.entity.Bom;
import eci.server.DesignModule.entity.design.Design;
import eci.server.ItemModule.entity.item.Item;
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
            Item item,
            Bom bom
    ) {

        return new BomTodoResponse(

                bom.getId(),
                item.getName(),
                item.getType(),
                item.getItemNumber()

        );
    }


}

