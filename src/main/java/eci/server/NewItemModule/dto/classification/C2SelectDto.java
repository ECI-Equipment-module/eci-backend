package eci.server.NewItemModule.dto.classification;

import eci.server.DocumentModule.entity.classification.DocClassification2;
import eci.server.DocumentModule.repository.DocTagRepository;
import eci.server.NewItemModule.entity.classification.Classification2;
import eci.server.NewItemModule.repository.classification.Classification2Repository;
import eci.server.NewItemModule.repository.classification.Classification3Repository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class C2SelectDto {
    private Long id;
    private String name;
    private Integer last;
    private String value; //value"	: 파트/메카니컬/LCD"
    private String classification; //classification" :	:	1/1/99999
    List<C3SelectDto> c3SelectDtos;

//    public static C2SelectDto toDto(
//            Classification2 classification2,
//            Classification3Repository classification3Repository) {
//        return new C2SelectDto(
//                classification2.getId(),
//                classification2.getName(),
//                classification2.getLast(),
//                C3SelectDto.toDtoList(
//                        classification3Repository.findByClassification2(classification2)
//                )
//        );
//    }


    public static List<C2SelectDto> toDtoList(
            String beforeName,
            String beforeId,
            List <Classification2> classification2List,
            Classification2Repository classification2Repository,
            Classification3Repository classification3Repository
    ) {

        List<C2SelectDto> classification1SelectDtoList
                = classification2List.stream().map(
                c -> new C2SelectDto(
                        c.getId(),
                        c.getName(),
                        c.getLast(),

                        beforeName+"/"+c.getName(),
                        beforeId+"/"+c.getId()+"/"+(c.getLast()==1?"99999":""),

                        C3SelectDto.toDtoList(
                                beforeName+"/"+c.getName(),
                                beforeId+"/"+c.getId(),
                                classification3Repository.findByClassification2(c)

                        )
                )
        ).collect(
                toList()
        );
        return classification1SelectDtoList;
    }


    public static List<C2SelectDto> toDocDtoList(
            String beforeName,
            String beforeId,
            List <DocClassification2> docClassification2s,
            DocTagRepository docTagRepository
    ) {

        List<C2SelectDto> classification1SelectDtoList
                = docClassification2s.stream().map(
                c -> new C2SelectDto(
                        c.getId(),
                        c.getName(),
                        c.getLast(),

                        beforeName+"/"+c.getName(),
                        beforeId+"/"+c.getId(),

                        C3SelectDto.toDocDtoList(
                                beforeName+"/"+c.getName(),
                                beforeId+"/"+c.getId(),
                                docTagRepository.findByDocClassification2(c)

                        )
                )
        ).collect(
                toList()
        );
        return classification1SelectDtoList;
    }


}

