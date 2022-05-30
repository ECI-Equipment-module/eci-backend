package eci.server.NewItemModule.dto.activateAttribute;

import eci.server.NewItemModule.entity.activateAttributeClassification.ChoiceFieldDto;
import eci.server.NewItemModule.entity.activateAttributes.ActivateAttributes;
import eci.server.NewItemModule.entity.activateAttributes.ChoiceField;
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
    private String apiUri;
    private String inputType;
    private String name;
    private List<ChoiceFieldDto> choiceFields;

    public static ActivateAttributesDto toDto(
            ActivateAttributes activateAttributes) {

        return new ActivateAttributesDto(
                activateAttributes.getId(),
                activateAttributes.getApiUri(),
                activateAttributes.getInputType(),
                activateAttributes.getName(),
                ChoiceFieldDto.toDtoList(
                        activateAttributes.getChoiceFields()
                )

        );
    }


    public static List<ActivateAttributesDto> toDtoList(
            List <ActivateAttributes> activateAttributes
    ) {
        List<ActivateAttributesDto> activateAttributeList
                = activateAttributes.stream().map(
                a -> new ActivateAttributesDto(
                        a.getId(),
                        a.getApiUri(),
                        a.getInputType(),
                        a.getName(),
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
