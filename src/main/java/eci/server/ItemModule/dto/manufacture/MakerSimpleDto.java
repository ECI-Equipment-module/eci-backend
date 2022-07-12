package eci.server.ItemModule.dto.manufacture;

import eci.server.NewItemModule.entity.maker.NewItemMaker;
import eci.server.NewItemModule.entity.supplier.Maker;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MakerSimpleDto {
    Long id;
    private String code;
    private String name;

    public static MakerSimpleDto toDto() {
        return new MakerSimpleDto();
    }


    public static MakerSimpleDto toDto(Maker maker) {
        return new MakerSimpleDto(
                maker.getId(),
                maker.getCode(),
                maker.getName());
    }

    public static List<MakerSimpleDto> toDtoList(List<NewItemMaker> maker) {

        List<MakerSimpleDto> makerSimpleDtos = maker.stream().map(
                m -> new MakerSimpleDto(
                        m.getMaker().getId(),
                        m.getMaker().getCode(),
                        m.getMaker().getName()
                )
        ).collect(
                toList()
        );
        return makerSimpleDtos;

    }

}
