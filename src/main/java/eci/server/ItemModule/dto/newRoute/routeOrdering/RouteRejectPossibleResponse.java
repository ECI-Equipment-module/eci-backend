package eci.server.ItemModule.dto.newRoute.routeOrdering;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class RouteRejectPossibleResponse {
    private List<Long> rejectPossibleIds;
}
