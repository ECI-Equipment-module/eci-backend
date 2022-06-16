package eci.server.DashBoardModule.dto.designTodo;

import eci.server.DesignModule.entity.design.Design;
//import eci.server.ItemModule.entity.item.Item;
import eci.server.NewItemModule.entity.NewItem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DesignTodoResponse {
    Long designId;
    String itemName;
    String itemType;
    String itemNumber;


    public static DesignTodoResponse toDto(
            NewItem item,
            Design design
    ) {

        return new DesignTodoResponse(

                design.getId(),
                item.getName(),
                item.getItemTypes().getItemType().name(),
                item.getItemNumber()

        );
    }


}

