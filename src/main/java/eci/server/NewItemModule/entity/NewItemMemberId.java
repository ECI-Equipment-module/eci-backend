package eci.server.NewItemModule.entity;

import eci.server.ItemModule.entity.member.Member;
import eci.server.ItemModule.entity.newRoute.RouteProduct;
import lombok.*;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@EqualsAndHashCode
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class NewItemMemberId implements Serializable {
    private NewItem newItem;
    private Member member;
}

