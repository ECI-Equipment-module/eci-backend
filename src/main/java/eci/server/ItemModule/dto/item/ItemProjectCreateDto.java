package eci.server.ItemModule.dto.item;

import eci.server.ItemModule.entity.item.Item;
import eci.server.ItemModule.repository.newRoute.RouteOrderingRepository;
import eci.server.NewItemModule.entity.NewItem;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * project에서 item 골라서
 * 생성 시
 */
@Data
@AllArgsConstructor
public class ItemProjectCreateDto {

    private Long itemId;
    private String itemName;
    private String itemNumber;
    private Integer itemRevision;
    private String itemFamily; //classification
    private String itemStatus;


    public static ItemProjectCreateDto toDto(
            NewItem Item,
            RouteOrderingRepository routeOrderingRepository) {

        return new ItemProjectCreateDto(

                Item.getId(),
                Item.getName(),
                Item.getItemNumber(),
                Item.getRevision(),
                Item.getClassification().getClassification1().getName()+"/"
                        +Item.getClassification().getClassification2().getName()+"/"
                        +Item.getClassification().getClassification3().getName(),
                routeOrderingRepository.findByNewItem(Item).get(
                        routeOrderingRepository.findByNewItem(Item).size()-1
                ).getLifecycleStatus()

        );
    }

}