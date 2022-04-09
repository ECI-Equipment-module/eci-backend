package eci.server.ItemModule.dto.material;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MaterialSimpleDto {
    Long id;
    private String code;
    private String name;
}
