package eci.server.NewItemModule.dto.classification;

import eci.server.NewItemModule.dto.ItemTypesDto;
import eci.server.NewItemModule.dto.image.NewItemImageDto;
import eci.server.NewItemModule.dto.newItem.NewItemPagingDto;
import eci.server.NewItemModule.entity.NewItem;
import eci.server.NewItemModule.entity.classification.Classification;
import lombok.AllArgsConstructor;
import lombok.Data;

import static java.util.stream.Collectors.toList;

@Data
@AllArgsConstructor
public class ClassificationDto {
    private String value;
    private String classification;

    public static ClassificationDto toDto(Classification classification) {

        return new ClassificationDto(
                classification.getClassification1().getName()
                +"/"+classification.getClassification2().getName()
                        +
                        (classification.getClassification3().getId().equals(99999L)?
                                "":
                        "/" +classification.getClassification3().getName()
                        ),
                classification.getClassification1().getId()
                        +"/"+classification.getClassification2().getId()
                        +
                        (classification.getClassification3().getId().equals(99999L)?
                                "/99999":
                                "/" +classification.getClassification3().getId()
                        )

        );

    }
}
