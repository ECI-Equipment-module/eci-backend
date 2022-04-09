package eci.server.ItemModule.dto.manufacture;

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
}
