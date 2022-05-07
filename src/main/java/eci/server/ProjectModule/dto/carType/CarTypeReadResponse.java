package eci.server.ProjectModule.dto.carType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CarTypeReadResponse {
    private Long id;
    private String name;
}
