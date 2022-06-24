package eci.server.ProjectModule.dto.carType;

import eci.server.ProjectModule.entity.project.CarType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CarTypeDto {
    private Long id;
    private String name;


    public static CarTypeDto toDto(CarType carType){

        return new CarTypeDto(
                carType.getId(),
                carType.getName()
        );
    }


    public static CarTypeDto toDto(){

        return new CarTypeDto(
                -1L,
                ""
        );
    }

}

