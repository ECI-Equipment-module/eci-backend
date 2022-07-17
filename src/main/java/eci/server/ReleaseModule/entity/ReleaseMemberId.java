package eci.server.ReleaseModule.entity;

import eci.server.ItemModule.entity.member.Member;
import lombok.*;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@EqualsAndHashCode
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class  ReleaseMemberId implements Serializable {
    private Releasing releasing;
    private Member member;
}

