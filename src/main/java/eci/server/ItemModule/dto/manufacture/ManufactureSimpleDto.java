package eci.server.ItemModule.dto.manufacture;

import eci.server.ItemModule.entity.item.Manufacture;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ManufactureSimpleDto {
    Long id;
    private String code;
    private String name;

    public static ManufactureSimpleDto toDto(Manufacture manufacture) {
        return new ManufactureSimpleDto(
                manufacture.getId(),
                manufacture.getCode(),
                manufacture.getName());
    }

}
