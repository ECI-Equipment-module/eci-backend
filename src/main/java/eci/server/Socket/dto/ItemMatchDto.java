package eci.server.Socket.dto;

//import eci.server.ItemModule.entity.item.Item;
import eci.server.NewItemModule.entity.NewItem;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class ItemMatchDto {

    private boolean match;
    private Integer number;
    private String name;
    private Integer quantity;
    private String designData;
    private String topAssyDrawing;
    private String dwg;
    private String step;
    private String pdg;
    private List<ItemMatchDto> children;

    //temp
    public static ItemMatchDto toDtoMatch(NewItem item){
        List<ItemMatchDto> itemMatchDtos = new ArrayList<>();
        return new ItemMatchDto(
                true,
                323425245,
                "tempTopAssy",
                3,
                "src/main/prodmedia/image/2022-05-12/3e109555-c1bf-49a8-8ae0-ef76f336e6c1.JPG",
                "src/main/prodmedia/image/2022-05-12/3e109555-c1bf-49a8-8ae0-ef76f336e6c1.JPG",
                "src/main/prodmedia/image/2022-05-12/3e109555-c1bf-49a8-8ae0-ef76f336e6c1.JPG",
                "src/main/prodmedia/image/2022-05-12/3e109555-c1bf-49a8-8ae0-ef76f336e6c1.JPG",
                "src/main/prodmedia/image/2022-05-12/3e109555-c1bf-49a8-8ae0-ef76f336e6c1.JPG",
//                Item.getChildren
                itemMatchDtos

        );
    }
}
