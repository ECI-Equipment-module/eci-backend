package eci.server.BomModule.dto;

import eci.server.BomModule.entity.DevelopmentBomCard;
import eci.server.ItemModule.helper.NestedConvertHelper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DevelopmentBomCardDto {

    private Long id;
    private String classification;
    private String name;
    private String itemTypes;
    private String itemNumber;
    //private String thumbnailAddress;
    private String sharing;
    private boolean deleted;
    private List<DevelopmentBomCardDto> children;


    public static List<DevelopmentBomCardDto> toDtoList(List<DevelopmentBomCard> DevelopmentBomCards) {
        //계층형 구조로 손쉽게 변환
        NestedConvertHelper helper = NestedConvertHelper.newInstance(

                DevelopmentBomCards, //계층형 구조로 변환할 엔티티 목록
                c -> new DevelopmentBomCardDto(
                        c.isDeleted() ? null :c.getId(),//엔티티를 DTO로 변환하는 함수
                        c.isDeleted() ? null :c.getClassification(),
                        c.isDeleted() ? null :c.getCardName(),
                        c.isDeleted() ? null :c.getCardType(),
                        c.isDeleted() ? null :c.getCardNumber(),
                        c.isDeleted() ? null :c.getSharing(),
                        c.isDeleted() ? null :c.isDeleted(),
                        //c.getThumbnailAddress(),
                        c.isDeleted() ? null :new ArrayList<>()
                ),
                c -> c.getParent(),// 엔티티의 부모를 반환하는 함수를,
                c -> c.getId(), // 엔티티의 ID를 반환
                d -> d.getChildren()

        ); // DTO의 자식 목록을 반환하는 함수
        return helper.convert();
    }

}
