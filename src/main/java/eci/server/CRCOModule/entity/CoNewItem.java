package eci.server.CRCOModule.entity;

import eci.server.ItemModule.entity.member.Member;
import eci.server.ItemModule.entity.member.Role;
import eci.server.NewItemModule.entity.NewItem;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EqualsAndHashCode
@IdClass(CoNewItemId.class)
public class CoNewItem {
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "co_id")
    private ChangeOrder changeOrder;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "new_item_id")
    private NewItem newItem;

}
