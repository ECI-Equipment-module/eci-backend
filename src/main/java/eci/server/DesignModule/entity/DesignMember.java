package eci.server.DesignModule.entity;

import eci.server.CRCOModule.entity.cr.ChangeRequest;
import eci.server.CRCOModule.entity.cr.CrMemberId;
import eci.server.ItemModule.entity.member.Member;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EqualsAndHashCode
@IdClass(DesignMemberId.class)
public class DesignMember {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cr_id")
    private ChangeRequest changeRequest;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

}

