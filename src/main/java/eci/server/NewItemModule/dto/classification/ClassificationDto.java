package eci.server.NewItemModule.dto.classification;

import eci.server.NewItemModule.entity.classification.Classification;
import lombok.AllArgsConstructor;
import lombok.Data;

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
