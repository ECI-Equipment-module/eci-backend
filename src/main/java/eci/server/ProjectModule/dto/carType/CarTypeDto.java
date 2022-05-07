package eci.server.ProjectModule.dto.carType;

import eci.server.ProjectModule.entity.project.CarType;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CarTypeDto {
    private Long id;
    private String name;

    public static CarTypeDto toDto(CarType carType){

        return new CarTypeDto(
                carType.getId(),
                carType.getName()
        );
    }
}

