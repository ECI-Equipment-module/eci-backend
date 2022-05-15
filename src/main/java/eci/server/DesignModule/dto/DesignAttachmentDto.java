package eci.server.DesignModule.dto;

import eci.server.DesignModule.entity.designfile.DesignAttachment;
import eci.server.ProjectModule.entity.projectAttachment.ProjectAttachment;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Data
@AllArgsConstructor
public class DesignAttachmentDto  {
    private Long id;
    private String originName;
    private String uniqueName;
    private String attach_comment;
    private boolean deleted;
    private String tag;
    private String attachmentaddress;
    private String date;
    private String upload;

    public static DesignAttachmentDto toDto(DesignAttachment attachment) {
        return new DesignAttachmentDto(
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

                attachment.getDesign().getMember().getUsername()

        );
    }


    public static List<DesignAttachmentDto> toDtoList(List<DesignAttachment> Attachments) {

        List<DesignAttachmentDto> attachmentDtoList =
                Attachments.stream().map(
                        i -> new DesignAttachmentDto(
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
                                i.getDesign().getMember().getUsername()
                        )
                ).collect(
                        toList()
                );

        return attachmentDtoList;

    }



}
