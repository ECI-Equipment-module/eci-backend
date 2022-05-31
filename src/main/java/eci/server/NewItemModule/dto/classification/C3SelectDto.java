package eci.server.NewItemModule.dto.classification;

import eci.server.NewItemModule.entity.classification.Classification3;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class C3SelectDto {
    private Long id;
    private String name;
    private Integer last;

    public static C3SelectDto toDto(Classification3 classification3) {
        return new C3SelectDto(
                classification3.getId(),
                classification3.getName(),
                classification3.getLast()
        );
    }


    public static List<C3SelectDto> toDtoList(
            List <Classification3> classification3List
    ) {
        List<C3SelectDto> c3SelectDtoList
                = classification3List.stream().map(
                c -> new C3SelectDto(
                        c.getId(),
                        c.getName(),
                        c.getLast()
                )
        ).collect(
                toList()
        );
        return c3SelectDtoList;
    }
}

