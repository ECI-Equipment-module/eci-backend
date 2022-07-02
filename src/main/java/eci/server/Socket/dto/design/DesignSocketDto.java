package eci.server.Socket.dto.design;

import eci.server.DesignModule.dto.DesignContentDto;
import eci.server.NewItemModule.entity.NewItem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DesignSocketDto {
    String title;
    DesignContentDto item;
    DesignContentDto cad;

    public static DesignSocketDto toDto(
            NewItem newItem
    ) {

        return new DesignSocketDto(

                newItem.getItemNumber()+ "-" +
                newItem.getName() + "-"
                +"desgin file",

                DesignContentDto.toDto(newItem),

                DesignContentDto.toDto(newItem)

        );

    }

}