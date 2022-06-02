package eci.server.NewItemModule.dto.maker;


import eci.server.ItemModule.dto.color.ColorDto;
import eci.server.ItemModule.entity.item.Color;
import eci.server.NewItemModule.entity.supplier.Maker;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MakerDto {
    private Long id;
    private String code;
    private String name;

    public static MakerDto toDto(Maker maker) {
        return new MakerDto(
                maker.getId(),
                maker.getCode(),
                maker.getName()
        );
    }

}
