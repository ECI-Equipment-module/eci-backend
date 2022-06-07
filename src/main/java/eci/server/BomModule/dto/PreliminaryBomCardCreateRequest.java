package eci.server.BomModule.dto;

import eci.server.BomModule.entity.PreliminaryBom;
import eci.server.BomModule.entity.PreliminaryBomCard;
import eci.server.BomModule.exception.PreliminaryBomCardNotFoundException;
import eci.server.BomModule.exception.PreliminaryBomNotFoundException;
import eci.server.BomModule.repository.BomRepository;
import eci.server.BomModule.repository.PreliminaryBomCardRepository;
import eci.server.BomModule.repository.PreliminaryBomRepository;
import eci.server.NewItemModule.dto.newItem.create.NewItemCreateRequest;
import eci.server.NewItemModule.repository.classification.Classification1Repository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
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
                preliminaryBomCardRepository.findById(req.getParentId()).orElseThrow(PreliminaryBomCardNotFoundException::new)//parent Id
        );
    }



}
