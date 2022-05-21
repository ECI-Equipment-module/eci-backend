package eci.server.ItemModule.dto.item;

import eci.server.ItemModule.entity.item.Item;
import eci.server.ItemModule.repository.newRoute.RouteOrderingRepository;
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
    private Integer itemNumber;
    private Integer revision;
    private String status;


    public static ItemProjectCreateDto toDto(
            Item Item,
            RouteOrderingRepository routeOrderingRepository) {

        return new ItemProjectCreateDto(

                Item.getId(),
                Item.getName(),
                Item.getType(),
                Item.getItemNumber(),
                Item.getRevision(),
                routeOrderingRepository.findByItem(Item).get(
                        routeOrderingRepository.findByItem(Item).size()-1
                ).getLifecycleStatus()

        );
    }

}