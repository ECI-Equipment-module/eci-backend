package eci.server.ItemModule.dto.color;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ColorReadResonse {
    private Long id;
    private String code;
    private String name;
}
