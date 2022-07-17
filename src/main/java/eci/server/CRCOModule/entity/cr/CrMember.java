package eci.server.CRCOModule.entity.cr;

import eci.server.CRCOModule.entity.co.ChangeOrder;
import eci.server.CRCOModule.entity.co.CoMemberId;
import eci.server.ItemModule.entity.member.Member;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EqualsAndHashCode
@IdClass(CrMemberId.class)
public class CrMember{

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cr_id")
    private ChangeRequest changeRequest;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

}
