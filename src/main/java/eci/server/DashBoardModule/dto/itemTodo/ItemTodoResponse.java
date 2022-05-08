package eci.server.DashBoardModule.dto.itemTodo;
import eci.server.ItemModule.entity.item.Item;
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
    Integer itemNumber;

    public static ItemTodoResponse toDto(
            Item item
    ) {

        return new ItemTodoResponse(

                item.getId(),
                item.getName(),
                item.getType(),
                item.getItemNumber()

        );
    }

}
