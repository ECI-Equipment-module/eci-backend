package eci.server.BomModule.dto;

import eci.server.BomModule.entity.DevelopmentBom;

import eci.server.BomModule.entity.DevelopmentBomCard;
import eci.server.BomModule.exception.DevelopmentBomNotFoundException;

import eci.server.BomModule.exception.DevelopmentCardNotFoundException;
import eci.server.BomModule.repository.DevelopmentBomCardRepository;
import eci.server.BomModule.repository.DevelopmentBomRepository;
import eci.server.ItemModule.exception.item.ItemNotFoundException;
import eci.server.NewItemModule.repository.item.NewItemRepository;
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
public class DevelopmentBomCardCreateRequest {
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
    private Long DevelopmentBomId;

    @Null
    private Long parentId;

    private List<DevelopmentBomCardCreateRequest> children;

    @NotNull(message = "이 카드 아이디의 진짜 아이템 아이디는 필수! ")
    private Long NewItemId;

    public static DevelopmentBomCard toParentEntity(
            DevelopmentBomCardCreateRequest req,
            DevelopmentBomRepository DevelopmentBomRepository,
            DevelopmentBomCardRepository DevelopmentBomCardRepository,
            NewItemRepository newItemRepository
    ){
        return new DevelopmentBomCard(
                req.getCardNumber(),
                req.cardName,
                req.getClassification(),
                req.getCardType(),
                req.getSharing(),
                DevelopmentBomRepository.findById(req.getDevelopmentBomId()).orElseThrow(DevelopmentBomNotFoundException::new),//Development
                null, //지가 젤 부모라서 부모 없음 얘는
                newItemRepository.findById(req.NewItemId).orElseThrow(ItemNotFoundException::new)
        );
    }

    public static DevelopmentBomCard toEntity(
            DevelopmentBomCardCreateRequest req,
            DevelopmentBomRepository DevelopmentBomRepository,
            DevelopmentBomCardRepository DevelopmentBomCardRepository,
            NewItemRepository newItemRepository
    ){
        return new DevelopmentBomCard(
                req.getCardNumber(),
                req.cardName,
                req.getClassification(),
                req.getCardType(),
                req.getSharing(),
                DevelopmentBomRepository.findById(req.getDevelopmentBomId()).orElseThrow(DevelopmentBomNotFoundException::new),//Development

                req.getParentId()==null? //부모 아이디는 null 가능
                        null
                :
                        DevelopmentBomCardRepository.findById(req.getParentId()).orElseThrow(DevelopmentCardNotFoundException::new),//parent Id
                newItemRepository.findById(req.NewItemId).orElseThrow(ItemNotFoundException::new)
        );
    }

    public static List<DevelopmentBomCard> toEntityList(
            DevelopmentBomCardCreateRequest req,
            DevelopmentBomRepository DevelopmentBomRepository,
            DevelopmentBomCardRepository DevelopmentBomCardRepository,
            NewItemRepository newItemRepository
    ) {

        List<DevelopmentBomCard> willBeSavedDevelopmentBomCardList
                = new ArrayList<>(); //여기에 생성된 모든 라우트들 ADD할거야


        DevelopmentBomCard firstCard =
                DevelopmentBomCardCreateRequest.toEntity(
                req,
                DevelopmentBomRepository,
                DevelopmentBomCardRepository,
                        newItemRepository
        );

        return willBeSavedDevelopmentBomCardList;

    }
    // 아이디는 직전에 생긴 애,,,
    //if children =>
    public static List<DevelopmentBomCard> toChildrenList(
            Long parentId,
            List<DevelopmentBomCardCreateRequest> childrenList,
            DevelopmentBom DevelopmentBom,
            DevelopmentBomCardRepository DevelopmentBomCardRepository,
            NewItemRepository newItemRepository
    ){

        List<DevelopmentBomCard> willBeSavedChildrenList
                = new ArrayList<>(); //여기에 생성된 모든 라우트들 ADD할거야

        for(DevelopmentBomCardCreateRequest req : childrenList){

            willBeSavedChildrenList.add(
                    new DevelopmentBomCard(
                            req.getCardNumber(),
                            req.getCardName(),
                            req.getClassification(),
                            req.getCardType(),
                            req.getSharing(),
                            DevelopmentBom,
                            DevelopmentBomCardRepository.findById(parentId).orElseThrow(DevelopmentCardNotFoundException::new),
                            newItemRepository.findById(req.getNewItemId()).orElseThrow(ItemNotFoundException::new)
                    )
            );
        }

        return willBeSavedChildrenList;
    }

}
