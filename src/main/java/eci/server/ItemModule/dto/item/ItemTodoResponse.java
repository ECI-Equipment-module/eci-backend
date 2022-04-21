package eci.server.ItemModule.dto.item;

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
}
