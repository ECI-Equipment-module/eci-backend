package eci.server.NewItemModule.entity.activateAttributeClassification;

import eci.server.NewItemModule.dto.activateAttribute.ActivateAttributesDto;
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
public class ChoiceFieldDto {
    private String name;

    public static ChoiceFieldDto toDto(
            ChoiceField choiceField) {

        return new ChoiceFieldDto(
                choiceField.getName()
        );
    }


    public static List<ChoiceFieldDto> toDtoList(
            List <ChoiceField> choiceFields
    ) {
        List<ChoiceFieldDto> choiceFieldDtoList
                = choiceFields.stream().map(
                a -> new ChoiceFieldDto(
                        a.getName()
                )
        ).collect(
                toList()
        );
        return choiceFieldDtoList;
    }
}


