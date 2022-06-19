package eci.server.ItemModule.dto.item;

//import eci.server.ItemModule.entity.item.Item;
import eci.server.ItemModule.repository.newRoute.RouteOrderingRepository;
import eci.server.NewItemModule.dto.newItem.ItemClassificationDto;
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

    private String itemNumber;
    private String itemName;

    private Long itemId;
    private Integer itemRevision;
    // private String itemFamily; //classification
    private ItemClassificationDto itemClassification;
    private String itemStatus;


    public static ItemProjectCreateDto toDto(
            NewItem Item,
            RouteOrderingRepository routeOrderingRepository) {

        return new ItemProjectCreateDto(
                Item.getItemNumber(),
                Item.getName(),

                Item.getId(),
                Item.getRevision(),

                new ItemClassificationDto(Item.getClassification().getClassification1().getName()+"/"
                        +Item.getClassification().getClassification2().getName()+"/"
                        +Item.getClassification().getClassification3().getName()),

                routeOrderingRepository.findByNewItem(Item).get(
                        routeOrderingRepository.findByNewItem(Item).size()-1
                ).getLifecycleStatus()

        );
    }

}