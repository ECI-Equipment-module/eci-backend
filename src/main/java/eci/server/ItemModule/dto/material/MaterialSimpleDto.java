package eci.server.ItemModule.dto.material;

import eci.server.ItemModule.entity.material.Material;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MaterialSimpleDto {
    Long id;
    private String code;
    private String name;

    public static MaterialSimpleDto toDto(Material material) {
        return new MaterialSimpleDto(
                material.getId(),
                material.getCode(),
                material.getName());
    }
}
