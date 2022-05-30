package eci.server.NewItemModule.dto.attachment;

import eci.server.ItemModule.entity.item.Attachment;
import eci.server.NewItemModule.entity.NewItemAttachment;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Data
@AllArgsConstructor
public class NewItemAttachmentDto {
    private Long id;
    private String originName;
    private String uniqueName;
    private String attach_comment;
    private boolean deleted;
    private String tag;
    private String attachmentaddress;
    private String date;
    private String upload;

    public static NewItemAttachmentDto toDto(NewItemAttachment attachment) {
        return new NewItemAttachmentDto(
                attachment.getId(),
                attachment.getOriginName(),
                attachment.getUniqueName(),
                attachment.getAttach_comment(),
                attachment.isDeleted(),
                attachment.getTag(),

                "src/main/prodmedia/image/"
                        +
                        attachment.
                                getCreatedAt().
                                toString().
                                split("_")[0].
                                substring(0,10)
                        + "/"  +
                        attachment.getUniqueName(),

                attachment.getModifiedAt().toString().split("_")[0].substring(0, 10),

                attachment.getNewItem().getMember().getUsername()

        );
    }


    public static List<NewItemAttachmentDto> toDtoList(List<Attachment> Attachments) {

        List<NewItemAttachmentDto> attachmentDtoList =
                Attachments.stream().map(
                        i -> new NewItemAttachmentDto(
                                i.getId(),
                                i.getOriginName(),
                                i.getUniqueName(),
                                i.getAttach_comment(),
                                i.isDeleted(),
                                i.getTag(),

                                "src/main/prodmedia/image/"
                                        +
                                        i.
                                                getCreatedAt().
                                                toString().
                                                split("_")[0].
                                                substring(0,10)
                                        + "\\"  +
                                        i.getUniqueName(),

                                i.getModifiedAt().toString().split("_")[0].substring(0, 10),

                                i.getItem().getMember().getUsername()
                        )
                ).collect(
                        toList()
                );

        return attachmentDtoList;

    }



}
