package eci.server.ItemModule.dto.color;

import eci.server.ItemModule.entity.item.Color;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ColorDto {
    private Long id;
    private String code;
    private String name;

    public static ColorDto toDto(Color color) {
        return new ColorDto(
                color.getId(),
                color.getCode(),
                color.getColor()
        );
    }
}
