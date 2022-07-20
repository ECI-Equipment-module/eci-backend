package eci.server.DocumentModule.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import eci.server.DocumentModule.entity.DocumentAttachment;
import eci.server.ItemModule.dto.member.MemberDto;
import eci.server.ItemModule.entity.member.Member;
import eci.server.NewItemModule.dto.attachment.AttachmentTagDto;
import eci.server.NewItemModule.dto.classification.ClassificationDto;
import eci.server.NewItemModule.repository.attachment.AttachmentTagRepository;
import eci.server.ProjectModule.dto.projectAttachmentDto.ProjectAttachmentDto;
import eci.server.ProjectModule.entity.projectAttachment.ProjectAttachment;
import eci.server.ReleaseModule.entity.ReleaseAttachment;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Data
@AllArgsConstructor
public class DocumentAttachmentDto implements Comparable<DocumentAttachmentDto>{
    private Long id;
    private String originName;
    private String uniqueName;
    private boolean deleted;
//    private AttachmentTagDto tag;
    private String attachmentaddress;
    private String date;
    private String upload;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime modifiedAt;


    public static DocumentAttachmentDto toDto(
            DocumentAttachment attachment
    ) {
        return new DocumentAttachmentDto(
                attachment.getId(),
                attachment.getOriginName(),
                attachment.getUniqueName(),
                attachment.isDeleted(),
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

                attachment.getDocument().getMember().getUsername(),

                attachment.getModifiedAt()

        );
    }


    public static List<ProjectAttachmentDto> toDtoList(
            List<ProjectAttachment> Attachments,
            AttachmentTagRepository attachmentTagRepository
    ) {

        List<eci.server.ProjectModule.dto.projectAttachmentDto.ProjectAttachmentDto> attachmentDtoList =
                Attachments.stream().map(
                        i -> new eci.server.ProjectModule.dto.projectAttachmentDto.ProjectAttachmentDto(
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
                                i.getProject().getMember().getUsername(),
                                i.getModifiedAt()
                        )
                ).collect(
                        toList()
                );

        return attachmentDtoList;

    }


    public static List<DocumentAttachmentDto> toDtoList() {

        List<DocumentAttachmentDto> documentAttachmentDtos = new ArrayList<>();

        return documentAttachmentDtos;

    }

    @Override
    public int compareTo(DocumentAttachmentDto attachment) {
        return (int) (this.id - attachment.getId());
    }

}


