package eci.server.ItemModule.dto.item;

import com.fasterxml.jackson.annotation.JsonInclude;
import eci.server.ItemModule.dto.member.MemberDto;
import eci.server.ItemModule.dto.newRoute.RouteOrderingDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UnlinkedItemDto {

    private boolean tempsave;

    private Long id;
    private String name;

    private Integer itemNumber;

    private char revision;

    private String lifecyclePhase;

    private MemberDto member;

    private List<AttachmentDto> attachments;


    public static UnlinkedItemDto toDto(
            ItemDto itemDto,
            String lifecyclePhase,
            List<AttachmentDto> attachmentDtoList
    ) {
        return new UnlinkedItemDto(
                itemDto.isTempsave(),//true면 임시저장 상태, false면 찐 저장 상태

                itemDto.getId(),
                itemDto.getName(),
                itemDto.getItemNumber(),

                itemDto.getRevision(),

                lifecyclePhase,

                itemDto.getMember(),

                attachmentDtoList

        );
    }
}

