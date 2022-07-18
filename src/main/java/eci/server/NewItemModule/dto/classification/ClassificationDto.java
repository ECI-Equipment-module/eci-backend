package eci.server.NewItemModule.dto.classification;

import eci.server.DocumentModule.entity.classification.DocClassification;
import eci.server.NewItemModule.entity.classification.Classification;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClassificationDto {
    private String value;
    private String classification;

    public static ClassificationDto toDto() {
        return  new ClassificationDto();
    }

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


    public static ClassificationDto toDocDto(DocClassification classification) {

        return new ClassificationDto(
                classification.getDocClassification1().getName()
                        +"/"+classification.getDocClassification2().getName()
                ,
                classification.getDocClassification1().getId()
                        +"/"+classification.getDocClassification2().getId()


        );

    }

}
