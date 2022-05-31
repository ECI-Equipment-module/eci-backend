package eci.server.NewItemModule.dto.activateAttribute;

import eci.server.ItemModule.repository.item.ItemTypesRepository;
import eci.server.NewItemModule.dto.ItemTypesDto;
import eci.server.NewItemModule.dto.attachment.AttachmentTagDto;
import eci.server.NewItemModule.entity.classification.Classification1;
import eci.server.NewItemModule.repository.attachment.Classification1AttachmentTagRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RetrieveActivateAttributes {
    List<ActivateAttributesDto> attributesDtoList;
    List<ItemTypesDto> selectableItemTypes;
    List<AttachmentTagDto> selectableAttachmentTags;

    public static RetrieveActivateAttributes toDto(
            List<ActivateAttributesDto> attributesDtoList,
            Classification1 classification1,
            ItemTypesRepository itemTypesRepository,
            Classification1AttachmentTagRepository classification1AttachmentTagRepository) {

        return new RetrieveActivateAttributes(
                attributesDtoList,
                ItemTypesDto.toDtoList(
                        itemTypesRepository.findByClassification1(classification1)
                ),
                AttachmentTagDto.toDtoList(
                        classification1AttachmentTagRepository.findByClassification1(classification1)
                )

        );
    }
}
