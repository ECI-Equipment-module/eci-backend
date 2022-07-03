package eci.server.ItemModule.dto.item;

//import eci.server.ItemModule.entity.item.Item;
import eci.server.ItemModule.repository.newRoute.RouteOrderingRepository;
import eci.server.NewItemModule.entity.NewItem;
import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class ItemProjectDto {

    private Long id;
    private String name;
    private String type;
    private String itemNumber;
    private int revision;

    private String classification;
    private String status;


    public static ItemProjectDto toDto(NewItem Item, RouteOrderingRepository routeOrderingRepository) {

        return new ItemProjectDto(

                Item.getId(),
                Item.getName(),
                Item.getItemTypes().getItemType().toString(),
                Item.getItemNumber(),
                Item.getRevision(),
                Item.getClassification().getClassification1().getName()
                        +"/" + Item.getClassification().getClassification2().getName()+
                        ( Item.getClassification().getClassification3().getId().equals(99999L)?
                                "":
                                "/" + Item.getClassification().getClassification3().getName()
                        ),
                routeOrderingRepository.findByNewItem(Item).get(
                        routeOrderingRepository.findByNewItem(Item).size() - 1
                ).getLifecycleStatus()


        );
    }

}
