package eci.server.CRCOModule.dto.co;

import com.fasterxml.jackson.annotation.JsonFormat;
import eci.server.CRCOModule.entity.CrAttachment;
import eci.server.CRCOModule.entity.cofeatures.CoAttachment;
import eci.server.DocumentModule.dto.DocumentAttachmentDto;
import eci.server.NewItemModule.dto.attachment.AttachmentTagDto;
import eci.server.NewItemModule.repository.attachment.AttachmentTagRepository;
import eci.server.ProjectModule.entity.projectAttachment.ProjectAttachment;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Data
@AllArgsConstructor
public class CoAttachmentDto implements Comparable<CoAttachmentDto>{
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

    public static CoAttachmentDto toDto(
            CoAttachment attachment,
            AttachmentTagRepository attachmentTagRepository
    ) {
        return new CoAttachmentDto(
                attachment.getId(),
                attachment.getOriginName(),
                attachment.getUniqueName(),
                attachment.getAttach_comment(),
                attachment.isDeleted(),
                //attachment.getTag(),
                AttachmentTagDto.toDto(
                        attachmentTagRepository.findByName(
                                attachment.getTag()
                        )
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

                attachment.getChangeOrder().getMember().getUsername(),

                attachment.getModifiedAt()

        );
    }


    public static List<CoAttachmentDto> toDtoList(
            List<CoAttachment> Attachments,
            AttachmentTagRepository attachmentTagRepository
    ) {

        List<CoAttachmentDto> attachmentDtoList =
                Attachments.stream().map(
                        i -> new CoAttachmentDto(
                                i.getId(),
                                i.getOriginName(),
                                i.getUniqueName(),
                                i.getAttach_comment(),
                                i.isDeleted(),
                                //i.getTag(),
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
                                i.getChangeOrder().getMember().getUsername(),
                                i.getModifiedAt()
                        )
                ).collect(
                        toList()
                );

        return attachmentDtoList;

    }

    @Override
    public int compareTo(CoAttachmentDto attachment) {
        return (int) (this.id - attachment.getId());
    }

}
