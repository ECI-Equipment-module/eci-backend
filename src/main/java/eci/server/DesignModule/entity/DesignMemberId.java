package eci.server.DesignModule.entity;

import eci.server.DesignModule.entity.design.Design;
import eci.server.ItemModule.entity.member.Member;
import lombok.*;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@EqualsAndHashCode
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class DesignMemberId implements Serializable {
    private Design design;
    private Member member;
}

