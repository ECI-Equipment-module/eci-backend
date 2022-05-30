package eci.server.ItemModule.dto.manufacture;

import eci.server.NewItemModule.entity.supplier.Maker;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MakerSimpleDto {
    Long id;
    private String code;
    private String name;

    public static MakerSimpleDto toDto(Maker maker) {
        return new MakerSimpleDto(
                maker.getId(),
                maker.getCode(),
                maker.getName());
    }

}
