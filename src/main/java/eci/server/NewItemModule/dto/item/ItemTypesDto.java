package eci.server.NewItemModule.dto.item;

import eci.server.ItemModule.dto.newRoute.RouteTypeDto;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ItemTypesDto {

    private Long id;

    private String name;

    private RouteTypeDto routeType;

}
