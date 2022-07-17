package eci.server.NewItemModule.dto.attachment;

import com.fasterxml.jackson.annotation.JsonFormat;
import eci.server.NewItemModule.entity.NewItemAttachment;
import eci.server.NewItemModule.entity.attachment.Attachment;
import eci.server.NewItemModule.repository.attachment.AttachmentTagRepository;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
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
    private AttachmentTagDto tag;
    private String attachmentaddress;
    private String date;
    private String upload;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime modifiedAt;

    public static NewItemAttachmentDto toDto(
            NewItemAttachment attachment,
            AttachmentTagRepository attachmentTagRepository
    ) {
        return new NewItemAttachmentDto(
                attachment.getId(),
                attachment.getOriginName(),
                attachment.getUniqueName(),
                attachment.getAttach_comment(),
                attachment.isDeleted(),

                AttachmentTagDto.toDto(
                        attachmentTagRepository.findByName(attachment.getTag())
                ),

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

                attachment.getNewItem().getMember().getUsername(),

                attachment.getModifiedAt()
        );
    }


    public static List<NewItemAttachmentDto> toDtoList(
            List<Attachment> Attachments,
            AttachmentTagRepository attachmentTagRepository
    ) {

        List<NewItemAttachmentDto> attachmentDtoList =
                Attachments.stream().map(
                        i -> new NewItemAttachmentDto(
                                i.getId(),
                                i.getOriginName(),
                                i.getUniqueName(),
                                i.getAttach_comment(),
                                i.isDeleted(),

                                AttachmentTagDto.toDto(
                                        attachmentTagRepository.findByName(i.getTag())
                                ),

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

                                i.getItem().getMember().getUsername(),

                                i.getModifiedAt()
                        )
                ).collect(
                        toList()
                );

        return attachmentDtoList;

    }



}
