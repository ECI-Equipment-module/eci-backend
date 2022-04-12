package eci.server.ItemModule.dto.item;

import eci.server.ItemModule.entity.item.Image;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ImageDto {
    private Long id;
    private String originName;
    private String uniqueName;
    private String thumbnailaddres;

    public static ImageDto toDto(Image image) {
        return new ImageDto(
                image.getId(),
                image.getOriginName(),
                image.getUniqueName(),

                "src/main/prodmedia/image/"
                        +
                        image.getModifiedAt().
                                toString().
                                split("_")[0].
                                substring(0,10)
                        + "/"  +
                        image.getUniqueName()
        );

    }
}