package eci.server.ItemModule.dto.newRoute.routeOrdering;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class RouteRejectPossibleResponse {
    private List<SeqAndName> rejectPossibleIds;
    //[시퀀스 번호, 그 카드 이름]
}
