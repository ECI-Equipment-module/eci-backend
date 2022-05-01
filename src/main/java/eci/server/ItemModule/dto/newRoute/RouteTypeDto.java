package eci.server.ItemModule.dto.newRoute;

import eci.server.ItemModule.entity.newRoute.RouteType;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RouteTypeDto {
    private Long id;

    private String name;

    private String module;

    private String todo;

    public static RouteTypeDto toDto(RouteType routeType){

        return new RouteTypeDto(
                routeType.getId(),
                routeType.getName(),
                routeType.getModule(),
                routeType.getTodo()
        );
    }
}
