package eci.server.ItemModule.dto.item;

import eci.server.ItemModule.dto.member.MemberDto;
import eci.server.ItemModule.dto.route.RouteDto;
import eci.server.ItemModule.entity.item.Attachment;
import eci.server.ItemModule.entity.item.Image;
import eci.server.ItemModule.entity.route.Route;
import eci.server.ItemModule.helper.NestedConvertHelper;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Data
@AllArgsConstructor
public class AttachmentDto {
    private Long id;
    private String originName;
    private String uniqueName;
    private String attach_comment;
    private boolean deleted;
    private String tag;
    private String attachmentaddress;
    //time.split("_")[0].substring(0, 10)
//    private LocalDateTime created_at;
    private String date;
    private String upload;

    public static AttachmentDto toDto(Attachment attachment) {
        return new AttachmentDto(
                attachment.getId(),
                attachment.getOriginName(),
                attachment.getUniqueName(),
                attachment.getAttach_comment(),
                attachment.isDeleted(),
                attachment.getTag(),

                "C:/Users/DONGYUN/Desktop/spring/server3/prodmedia/image/"
                        +
                        attachment.
                                getCreatedAt().
                                toString().
                                split("_")[0].
                                substring(0,10)
                        + "\\"  +
                        attachment.getUniqueName(),

                attachment.getModifiedAt().toString().split("_")[0].substring(0, 10),

                attachment.getItem().getMember().getUsername()

//                attachment.getCreatedAt(),
//                attachment.getModifiedAt()
        );
    }


    public static List<AttachmentDto> toDtoList(List<Attachment> Attachments) {

        List<AttachmentDto> attachmentDtoList =
                Attachments.stream().map(
                        i -> new AttachmentDto(
                                i.getId(),
                                i.getOriginName(),
                                i.getUniqueName(),
                                i.getAttach_comment(),
                                i.isDeleted(),
                                i.getTag(),

                                "C:/Users/DONGYUN/Desktop/spring/server3/prodmedia/image/"
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