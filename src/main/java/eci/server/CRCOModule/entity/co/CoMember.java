package eci.server.CRCOModule.entity.co;

import eci.server.ItemModule.entity.member.Member;
import eci.server.ItemModule.entity.newRoute.RouteProductMemberId;
import eci.server.ProjectModule.entity.project.Project;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EqualsAndHashCode
@IdClass(CoMemberId.class)
public class CoMember{

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "co_id")
    private ChangeOrder changeOrder;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

}

