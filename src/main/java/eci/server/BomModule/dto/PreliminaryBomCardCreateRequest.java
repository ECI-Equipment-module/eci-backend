package eci.server.BomModule.dto;

import eci.server.BomModule.entity.PreliminaryBom;
import eci.server.BomModule.entity.PreliminaryBomCard;
import eci.server.BomModule.exception.PreliminaryBomCardNotFoundException;
import eci.server.BomModule.exception.PreliminaryBomNotFoundException;
import eci.server.BomModule.repository.PreliminaryBomCardRepository;
import eci.server.BomModule.repository.PreliminaryBomRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PreliminaryBomCardCreateRequest {
    @NotNull(message = "아이템 번호를 입력해주세요.")
    private String cardNumber;

    @NotNull(message = "아이템 이름을 입력해주세요.")
    private String cardName;

    @NotNull(message = "분류를 입력해주세요.")
    private String classification;

    @NotNull(message = "타입을 입력해주세요.")
    private String cardType;

    @NotNull(message = "공용 여부를 입력해주세요.")
    private String sharing;

    @NotNull(message = "프릴 봄 아이디는 필수! ")
    private Long preliminaryBomId;

    @Null
    private Long parentId;

    private List<PreliminaryBomCardCreateRequest> children;

    public static PreliminaryBomCard toParentEntity(
            PreliminaryBomCardCreateRequest req,
            PreliminaryBomRepository preliminaryBomRepository,
            PreliminaryBomCardRepository preliminaryBomCardRepository
    ){
        return new PreliminaryBomCard(
                req.getCardNumber(),
                req.cardName,
                req.getClassification(),
                req.getCardType(),
                req.getSharing(),
                preliminaryBomRepository.findById(req.getPreliminaryBomId()).orElseThrow(PreliminaryBomNotFoundException::new),//preliminary
                null //지가 젤 부모라서 부모 없음 얘는
        );
    }

    public static PreliminaryBomCard toEntity(
            PreliminaryBomCardCreateRequest req,
            PreliminaryBomRepository preliminaryBomRepository,
            PreliminaryBomCardRepository preliminaryBomCardRepository
    ){
        return new PreliminaryBomCard(
                req.getCardNumber(),
                req.cardName,
                req.getClassification(),
                req.getCardType(),
                req.getSharing(),
                preliminaryBomRepository.findById(req.getPreliminaryBomId()).orElseThrow(PreliminaryBomNotFoundException::new),//preliminary

                req.getParentId()==null? //부모 아이디는 null 가능
                        null
                :
                        preliminaryBomCardRepository.findById(req.getParentId()).orElseThrow(PreliminaryBomCardNotFoundException::new)//parent Id
        );
    }

    public static List<PreliminaryBomCard> toEntityList(
            PreliminaryBomCardCreateRequest req,
            PreliminaryBomRepository preliminaryBomRepository,
            PreliminaryBomCardRepository preliminaryBomCardRepository
    ) {

        List<PreliminaryBomCard> willBeSavedPreliminaryBomCardList
                = new ArrayList<>(); //여기에 생성된 모든 라우트들 ADD할거야


        PreliminaryBomCard firstCard =
                PreliminaryBomCardCreateRequest.toEntity(
                req,
                preliminaryBomRepository,
                preliminaryBomCardRepository
        );

        return willBeSavedPreliminaryBomCardList;

    }
    // 아이디는 직전에 생긴 애,,,
    //if children =>
    public static List<PreliminaryBomCard> toChildrenList(
            Long parentId,
            List<PreliminaryBomCardCreateRequest> childrenList,
            PreliminaryBom preliminaryBom,
            PreliminaryBomCardRepository preliminaryBomCardRepository
    ){

        List<PreliminaryBomCard> willBeSavedChildrenList
                = new ArrayList<>(); //여기에 생성된 모든 라우트들 ADD할거야

        for(PreliminaryBomCardCreateRequest req : childrenList){

            willBeSavedChildrenList.add(
                    new PreliminaryBomCard(
                            req.getCardNumber(),
                            req.getCardName(),
                            req.getClassification(),
                            req.getCardType(),
                            req.getSharing(),
                            preliminaryBom,
                            preliminaryBomCardRepository.findById(parentId).orElseThrow(PreliminaryBomCardNotFoundException::new)
                    )
            );
        }

        return willBeSavedChildrenList;
    }

}
