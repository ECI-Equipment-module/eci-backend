package eci.server.ItemModule.dto.newRoute.routeProduct;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Lob;
import javax.validation.constraints.Null;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RouteProductUpdateRequest {

    @Null
    private Integer sequence;

    @Null
    private String type;

    @Lob
    private String comment;

    @Null
    private Long routeId;

}
