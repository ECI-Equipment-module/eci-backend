package eci.server.ItemModule.dto.manufacture;

import eci.server.ItemModule.entity.item.Manufacture;
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

    public static MakerSimpleDto toDto(Manufacture manufacture) {
        return new MakerSimpleDto(
                manufacture.getId(),
                manufacture.getCode(),
                manufacture.getName());
    }

}
