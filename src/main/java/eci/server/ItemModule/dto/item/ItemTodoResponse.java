package eci.server.ItemModule.dto.item;

import com.fasterxml.jackson.annotation.JsonInclude;
import eci.server.ItemModule.entity.item.Item;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemTodoResponse {
    Long itemId;
    String itemName;
    String itemType;
    Integer itemNumber;
}
