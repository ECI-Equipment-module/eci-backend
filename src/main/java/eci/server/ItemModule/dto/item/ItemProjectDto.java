package eci.server.ItemModule.dto.item;

import eci.server.ItemModule.repository.newRoute.RouteOrderingRepository;
import eci.server.NewItemModule.dto.newItem.ItemClassificationDto;
import eci.server.NewItemModule.entity.NewItem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemProjectDto {

    private Long id;
    private String name;
    private String type;
    private String itemNumber;
    private char revision;

    private ItemClassificationDto classification;
    private String status;


    public static ItemProjectDto toDto(NewItem Item, RouteOrderingRepository routeOrderingRepository) {

        return new ItemProjectDto(

                Item.getId(),
                Item.getName(),
                Item.getItemTypes().getItemType().toString(),
                Item.getItemNumber(),
                (char) Item.getRevision(),

                new ItemClassificationDto(Item.getClassification().getClassification1().getName()+"/"
                        +Item.getClassification().getClassification2().getName()+"/"
                        +( Item.getClassification().getClassification3().getId().equals(99999L)?
                        "":
                        "/" + Item.getClassification().getClassification3().getName()
                )
                ),


                routeOrderingRepository.findByNewItem(Item).get(
                        routeOrderingRepository.findByNewItem(Item).size() - 1
                ).getLifecycleStatus()


        );
    }

    public static ItemProjectDto noRoutetoDto(NewItem Item, RouteOrderingRepository routeOrderingRepository) {

        return new ItemProjectDto(

                Item.getId(),
                Item.getName(),
                Item.getItemTypes().getItemType().toString(),
                Item.getItemNumber(),
                (char) Item.getRevision(),

                new ItemClassificationDto(Item.getClassification().getClassification1().getName()+"/"
                        +Item.getClassification().getClassification2().getName()+"/"
                        +( Item.getClassification().getClassification3().getId().equals(99999L)?
                        "":
                        "/" + Item.getClassification().getClassification3().getName()
                )
                ),


                "NONE"


        );
    }

    public static ItemProjectDto toDto() {
        return new ItemProjectDto();
    }

}
