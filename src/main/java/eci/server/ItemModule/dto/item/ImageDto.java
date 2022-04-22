package eci.server.ItemModule.dto.item;

import eci.server.ItemModule.entity.item.Image;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ImageDto {
    private Long id;
    private String originName;
    private String uniqueName;
    private String imageAdress;
    public static ImageDto toDto(Image image) {
        return new ImageDto(image.getId(),
                image.getOriginName(),
                image.getUniqueName(),
                image.getImageaddress()
        );
    }

}