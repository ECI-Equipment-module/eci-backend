package eci.server.NewItemModule.dto.newItem;

import eci.server.ItemModule.helper.NestedConvertHelper;
import eci.server.NewItemModule.entity.NewItemParentChildren;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewItemChildDto {

    private Long id;
    private String classification;
    private String name;
    private String itemTypes;
    private String itemNumber;
    //private String thumbnailAddress;
    private String sharing;
    private List<NewItemChildDto> children;


    public static List<NewItemChildDto> toDtoList(List<NewItemParentChildren> NewItems) {
        //계층형 구조로 손쉽게 변환
        NestedConvertHelper helper = NestedConvertHelper.newInstance(

                NewItems, //계층형 구조로 변환할 엔티티 목록
                c -> new NewItemChildDto(
                        c.getChildren().getId(),//엔티티를 DTO로 변환하는 함수
                        c.getChildren().getClassification().getClassification1().getName()
                        +"/"+c.getChildren().getClassification().getClassification2().getName()+
                                (
                                        c.getChildren().getClassification().getClassification3().getId()==99999L?
                                        "":"/"+"/"+c.getChildren().getClassification().getClassification3().getName()
                                )
                        ,
                        c.getChildren().getName(),
                        c.getChildren().getItemTypes().getItemType().name(),
                        c.getChildren().getItemNumber(),
                        c.getChildren().isSharing()?"공용":"전용",
                        //c.getThumbnailAddress(),
                        new ArrayList<>()
                ),
                c -> c.getParent().getParent().get(0),// 엔티티의 부모를 반환하는 함수를,
                c -> c.getChildren().getId(), // 엔티티의 ID를 반환
                d -> d.getChildren()

        ); // DTO 의 자식 목록을 반환하는 함수
        return helper.convert();
    }

}
