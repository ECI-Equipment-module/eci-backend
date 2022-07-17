package eci.server.CRCOModule.entity.co;

import eci.server.ItemModule.entity.member.Member;
import lombok.*;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@EqualsAndHashCode
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class CoMemberId implements Serializable {
    private ChangeOrder changeOrder;
    private Member member;
}
