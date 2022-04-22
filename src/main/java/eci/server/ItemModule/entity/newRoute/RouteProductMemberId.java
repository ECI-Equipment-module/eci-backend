package eci.server.ItemModule.entity.newRoute;

import eci.server.ItemModule.entity.member.Member;
import lombok.*;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@EqualsAndHashCode
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class RouteProductMemberId implements Serializable {
    private RouteProduct routeProduct;
    private Member member;
}
