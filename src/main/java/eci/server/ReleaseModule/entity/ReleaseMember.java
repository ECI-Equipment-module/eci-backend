package eci.server.ReleaseModule.entity;

import eci.server.ItemModule.entity.member.Member;
import eci.server.NewItemModule.entity.NewItem;
import eci.server.NewItemModule.entity.NewItemMemberId;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EqualsAndHashCode
@IdClass(ReleaseMemberId.class)
public class ReleaseMember{

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "release_id")
    private Releasing releasing;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

}
