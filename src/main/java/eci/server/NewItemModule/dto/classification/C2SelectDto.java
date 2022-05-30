package eci.server.NewItemModule.dto.classification;

import eci.server.NewItemModule.entity.classification.Classification2;
import eci.server.NewItemModule.repository.classification.Classification2Repository;
import eci.server.NewItemModule.repository.classification.Classification3Repository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class C2SelectDto {
    private Long id;
    private String name;
    private boolean last;
    List<C3SelectDto> c3SelectDtos;

    public static C2SelectDto toDto(
            Classification2 classification2,
            Classification3Repository classification3Repository) {
        return new C2SelectDto(
                classification2.getId(),
                classification2.getName(),
                classification2.isLast(),
                C3SelectDto.toDtoList(
                        classification3Repository.findByClassification2(classification2)
                )
        );
    }


    public static List<C2SelectDto> toDtoList(
            List <Classification2> classification2List,
            Classification2Repository classification2Repository,
            Classification3Repository classification3Repository
    ) {
        List<C2SelectDto> classification1SelectDtoList
                = classification2List.stream().map(
                c -> new C2SelectDto(
                        c.getId(),
                        c.getName(),
                        c.isLast(),
                        C3SelectDto.toDtoList(
                                classification3Repository.findByClassification2(c)
                        )
                )
        ).collect(
                toList()
        );
        return classification1SelectDtoList;
    }

}

