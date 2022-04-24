package eci.server.ItemModule.dto.newRoute;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Lob;

@Data
@NoArgsConstructor
@AllArgsConstructor

/**
 * 아이템 프로덕트 update request, reject request
 * 수행하는 서비스 단에서
 * RouteUpdateRequest 수행하게 하기
 *
 * update request => newRouteUpdateRequest(newRoute.getPresent()+1)
 */
public class RouteOrderingUpdateRequest {
    @Lob
    private String comment;

}
