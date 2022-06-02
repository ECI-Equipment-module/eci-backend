package eci.server.NewItemModule.dto.activateAttribute;

import eci.server.NewItemModule.dto.ItemTypesDto;
import eci.server.NewItemModule.dto.attachment.AttachmentTagDto;
import eci.server.NewItemModule.entity.activateAttributeClassification.ChoiceFieldDto;
import eci.server.NewItemModule.entity.activateAttributeClassification.ClassifyActivate;
import eci.server.NewItemModule.entity.activateAttributes.ActivateAttributes;
import eci.server.NewItemModule.entity.activateAttributes.ChoiceField;
import eci.server.NewItemModule.repository.classification.ClassifyActivateRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActivateAttributesDto {
    private Long id;
//    private String apiUri;
    private String inputType;
    private String name;
    private String requestName;
    private int required;
    private List<ChoiceFieldDto> choiceFields;



    public static ActivateAttributesDto toDto(
            ActivateAttributes activateAttributes,
            ClassifyActivateRepository classifyActivateRepository) {

        return new ActivateAttributesDto(
                activateAttributes.getId(),
                activateAttributes.getInputType(),
                activateAttributes.getName(),
                activateAttributes.getRequestName(),
                -1,
                ChoiceFieldDto.toDtoList(
                        activateAttributes.getChoiceFields()
                )

        );
    }


    public static List<ActivateAttributesDto> toDtoList(
            List <ActivateAttributes> activateAttributes,
            List<ClassifyActivate> classifyActivates
    ) {
        List<ActivateAttributesDto> activateAttributeList
                = activateAttributes.stream().map(
                a -> new ActivateAttributesDto(
                        a.getId(),
//                        a.getApiUri(),
                        a.getInputType(),
                        a.getName(),
                        a.getRequestName(),
                        classifyActivates.get(activateAttributes.indexOf(a)).isRequired()?1:0,
                        ChoiceFieldDto.toDtoList(
                                a.getChoiceFields()
                        )
                )
        ).collect(
                toList()
        );
        return activateAttributeList;
    }
}
