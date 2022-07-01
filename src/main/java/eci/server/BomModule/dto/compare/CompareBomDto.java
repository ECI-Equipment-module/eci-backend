package eci.server.BomModule.dto.compare;

import eci.server.NewItemModule.entity.NewItem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompareBomDto {

    private Long cardId;
    private String cardNumber;
    private String cardName;
    private String classification;
    private String cardType;
    private String sharing;
    @Nullable
    private int different; // null 이 default


    public static CompareBomDto toCompareBomDto(
            NewItem newItem,
            int different
    ){
        return new CompareBomDto(
                newItem.getId(),
                newItem.getItemNumber(),
                newItem.getName(),

                newItem.getClassification().getClassification1().getName()
                        +"/" + newItem.getClassification().getClassification2().getName()+
                        ( newItem.getClassification().getClassification3().getId().equals(99999L)?
                                "":
                                "/" + newItem.getClassification().getClassification3().getName()
                        ),

                newItem.getItemTypes().getItemType().name(),

                newItem.isSharing()?"공용":"전용",
                different
        );
    }


    public static List<CompareBomDto> toCompareBomDtoList(
            Set<NewItem> newItemList,
            Set<NewItem> compareItem,
            Set<NewItem> againstItem
    ){

        return newItemList.stream().map(
                newItem ->
                        new CompareBomDto(
                                newItem.getId(),
                                newItem.getItemNumber(),
                                newItem.getName(),

                                newItem.getClassification().getClassification1().getName()
                                        +"/" + newItem.getClassification().getClassification2().getName()+
                                        ( newItem.getClassification().getClassification3().getId().equals(99999L)?
                                                "":
                                                "/" + newItem.getClassification().getClassification3().getName()
                                        ),

                                newItem.getItemTypes().getItemType().name(),

                                newItem.isSharing()?"공용":"전용",

                                (!compareItem.contains(newItem))?
                                        1:
                                        ((!againstItem.contains(newItem))?
                                        2:-1
                                        )

                        )
        ).collect(Collectors.toList());
    }
}
