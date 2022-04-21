package eci.server.ItemModule.dto.newRoute;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Lob;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

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
public class NewRouteRejectRequest {

    @NotNull(message = "거절될 아이를 입력해주세요")
    private Integer rejectedSequence;

    @NotNull(message = "거부 코멘트를 입력해주세요")
    @Lob
    private String comment;

}
