package eci.server.CRCOModule.entity.cr;

import eci.server.ItemModule.entity.member.Member;
import lombok.*;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@EqualsAndHashCode
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class CrMemberId implements Serializable {
    private ChangeRequest changeRequest;
    private Member member;
}

