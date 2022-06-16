package eci.server.NewItemModule.dto.image;

import eci.server.NewItemModule.entity.NewItemImage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewItemImageDto {
    private Long id;
    private String originName;
    private String uniqueName;
    private String imageAdress;
    public static NewItemImageDto toDto(NewItemImage newItemImage) {
        return new NewItemImageDto(newItemImage.getId(),
                newItemImage.getOriginName(),
                newItemImage.getUniqueName(),
                newItemImage.getImageaddress()
        );
    }

}
