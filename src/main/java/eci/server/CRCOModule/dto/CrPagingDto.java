package eci.server.CRCOModule.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import eci.server.CRCOModule.dto.featuresdtos.CrImportanceDto;
import eci.server.CRCOModule.dto.featuresdtos.CrReasonDto;
import eci.server.CRCOModule.dto.featuresdtos.CrSourceDto;
import eci.server.CRCOModule.entity.ChangeRequest;
import eci.server.CRCOModule.entity.features.CrImportance;
import eci.server.CRCOModule.entity.features.CrSource;
import eci.server.ItemModule.dto.member.MemberDto;
import eci.server.ItemModule.repository.newRoute.RouteOrderingRepository;
import eci.server.NewItemModule.dto.newItem.ItemClassificationDto;
import eci.server.NewItemModule.entity.NewItem;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class CrPagingDto {

    // item part
    private Long itemId;
    private String itemName;
    private String itemNumber;

    private char itemRevision;

    private ItemClassificationDto itemClassification;
    private String itemStatus;

    // cr part

    private String crNumber;
    private String crType;
    private CrReasonDto crReason;
    private CrImportanceDto crImportance;
    private CrSourceDto crSource;
    private String crName;
    private MemberDto member;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;



    public static CrPagingDto toDto(
            NewItem Item,
            RouteOrderingRepository routeOrderingRepository,
            ChangeRequest changeRequest,
            String defaultImageAddress
    ) {

        return new CrPagingDto(
                Item.getId(),
                Item.getName(),
                Item.getItemNumber(),
                (char) Item.getRevision(),

                new ItemClassificationDto(Item.getClassification().getClassification1().getName() + "/"
                        + Item.getClassification().getClassification2().getName() + "/"
                        + (Item.getClassification().getClassification3().getId().equals(99999L) ?
                        " " :
                        "/" + Item.getClassification().getClassification3().getName()
                )
                ),


                routeOrderingRepository.findByNewItem(Item).get(
                        routeOrderingRepository.findByNewItem(Item).size() - 1
                ).getLifecycleStatus(),
                changeRequest.getCrNumber(),
                "FAST TRACK",
                CrReasonDto.toDto(changeRequest.getCrReason()),
                CrImportanceDto.toDto(changeRequest.getCrImportance()),
                CrSourceDto.toDto(changeRequest.getCrSource()),
                changeRequest.getName(),
                MemberDto.toDto(changeRequest.getMember(), defaultImageAddress),
                changeRequest.getCreatedAt()
        );
    }


}
