package eci.server.NewItemModule.dto.item;

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

    private Long id;

    private String name;
    private String type;
    private String itemNumber;
    private char itemRevision;
    private String status;


    public static ItemProjectCreateDto toDto(
            NewItem Item,
            RouteOrderingRepository routeOrderingRepository) {

        return new ItemProjectCreateDto(

                Item.getId(),
                Item.getName(),
                Item.getItemTypes().getItemType().toString(),
                Item.getItemNumber(),
                (char)Item.getRevision(),
                routeOrderingRepository.findByNewItemOrderByIdAsc(Item).get(
                        routeOrderingRepository.findByNewItemOrderByIdAsc(Item).size()-1
                ).getLifecycleStatus()

        );
    }

}