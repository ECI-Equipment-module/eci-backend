package eci.server.NewItemModule.dto.classification;

import eci.server.NewItemModule.entity.classification.Classification1;
import eci.server.NewItemModule.repository.classification.Classification2Repository;
import eci.server.NewItemModule.repository.classification.Classification3Repository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class C1SelectDto {
    private Long id;
    private String name;
    private Integer last;

    List<C2SelectDto> c2SelectDtos;

    public static C1SelectDto toDto(
            Classification1 classification1,
            Classification2Repository classification2Repository,
            Classification3Repository classification3Repository) {

        return new C1SelectDto(
                classification1.getId(),
                classification1.getName(),
                classification1.getLast(),
                C2SelectDto.toDtoList(
                        classification1.getId().toString(),
                        classification1.getName(),
                        classification2Repository.findByClassification1(classification1),
                        classification2Repository,
                        classification3Repository
                )
        );
    }


    public static List<C1SelectDto> toDtoList(
            List <Classification1> classification1List,
            Classification2Repository classification2Repository,
            Classification3Repository classification3Repository
    ) {

        List<C1SelectDto> c1SelectDtoList
                = classification1List.stream().map(
                c -> new C1SelectDto(
                        c.getId(),
                        c.getName(),
                        c.getLast(),
                        C2SelectDto.toDtoList(
                                c.getName(),
                                c.getId().toString(),
                                classification2Repository.findByClassification1(c),
                                //classification2Repository.findAllByClassification2(),
                                classification2Repository,
                                classification3Repository
                        )

                )
        ).collect(
                toList()
        );
        return c1SelectDtoList;
    }
}

