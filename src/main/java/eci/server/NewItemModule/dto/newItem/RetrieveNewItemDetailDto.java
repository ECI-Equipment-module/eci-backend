package eci.server.NewItemModule.dto.newItem;

import eci.server.NewItemModule.dto.activateAttribute.ActivateAttributesDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RetrieveNewItemDetailDto {
    private List<ActivateAttributesDto> activateAttributes;
    private NewItemDetailDto newItemDetail;
}
