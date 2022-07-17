package eci.server.NewItemModule.entity;

import eci.server.ItemModule.entity.member.Member;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EqualsAndHashCode
@IdClass(NewItemMemberId.class)
public class NewItemMember{

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "new_item_id")
    private NewItem newItem;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

}


