package eci.server.ItemModule.dto.item;


import eci.server.ItemModule.entity.item.Item;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ItemDesignDto {

    private Long id;
    private String name;
    private String type;
    private Integer itemNumber;
    private String imageAddress;
    private int revision;



    public static ItemDesignDto toDto(Item Item) {

        return new ItemDesignDto(

                Item.getId(),
                Item.getName(),
                Item.getType(),
                Item.getItemNumber(),
                Item.getThumbnail().get(0).getImageaddress(),
                Item.getRevision()

        );
    }

}

