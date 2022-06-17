package eci.server.DashBoardModule.dto.itemTodo;

import eci.server.NewItemModule.entity.NewItem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemTodoResponse {
    Long itemId;
    String itemName;
    String itemType;
    String itemNumber;


    public static ItemTodoResponse toDto(
            NewItem item
    ) {

        return new ItemTodoResponse(

                item.getId(),
                item.getName(),
                item.getItemTypes().getItemType().name(),
                item.getItemNumber()

        );
    }


}
