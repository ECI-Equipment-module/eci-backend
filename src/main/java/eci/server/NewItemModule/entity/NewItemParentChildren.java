package eci.server.NewItemModule.entity;

import eci.server.NewItemModule.entity.maker.NewItemMakerId;
import eci.server.NewItemModule.entity.supplier.Maker;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EqualsAndHashCode
@IdClass(NewItemParentChildrenId.class)
public class NewItemParentChildren {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private NewItem parent;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "children_id")
    private NewItem children;

}
