package eci.server.NewItemModule.dto.attachment;

import eci.server.NewItemModule.entity.attachment.AttachmentTag;
import eci.server.NewItemModule.entity.activateAttributes.ActivateAttributes;
import eci.server.NewItemModule.entity.attachment.Classification1AttachmentTag;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AttachmentTagDto {
    private Long id;
    private String name;

    public static AttachmentTagDto toDto(
            AttachmentTag attachmentTag) {

        return new  AttachmentTagDto(
                attachmentTag.getId(),
                attachmentTag.getName()
        );
    }

    public static List<AttachmentTagDto> toDtoList(
            List <Classification1AttachmentTag> classification1AttachmentTags
    ) {
        List<AttachmentTagDto> classification1AttachmentTagList
                = classification1AttachmentTags.stream().map(
                a -> new AttachmentTagDto(
                        a.getAttachmentTag().getId(),
                        a.getAttachmentTag().getName()
                )
        ).collect(
                toList()
        );
        return classification1AttachmentTagList;
    }

    public static List<AttachmentTagDto> toDtoList2(
            List <ActivateAttributes> activateAttributes
    ) {
        List<AttachmentTagDto> activateAttributeList
                = activateAttributes.stream().map(
                a -> new AttachmentTagDto(
                        a.getId(),
                        a.getName()
                )
        ).collect(
                toList()
        );
        return activateAttributeList;
    }

}
