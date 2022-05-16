package eci.server.Socket.dto;

import eci.server.ItemModule.dto.item.ItemDto;
import eci.server.ItemModule.entity.item.Item;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ItemCadMatchDto {
    ItemMatchDto item;
    ItemMatchDto cadfile;
    Integer unmatched;
    String type;

}
