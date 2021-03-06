package eci.server.ItemModule.dto.item;


//import eci.server.ItemModule.entity.item.Item;
import eci.server.NewItemModule.entity.NewItem;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ItemDesignDto {

    private Long id;
    private String name;
    private String type;
    private String itemNumber;
    private String imageAddress;
    private int itemRevision;



    public static ItemDesignDto toDto(NewItem Item, String defaultImageAddress) {

        return new ItemDesignDto(

                Item.getId(),
                Item.getName(),
                Item.getItemTypes().getItemType().toString(),
                Item.getItemNumber(),
                Item.getThumbnail()==null?defaultImageAddress: Item.getThumbnail().getImageaddress(),
                Item.getRevision()

        );
    }

}

