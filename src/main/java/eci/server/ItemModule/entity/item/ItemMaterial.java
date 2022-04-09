package eci.server.ItemModule.entity.item;

import eci.server.ItemModule.entity.member.Member;
import eci.server.ItemModule.entity.member.MemberRoleId;
import eci.server.ItemModule.entity.member.Role;
import eci.server.ItemModule.entitycommon.EntityDate;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EqualsAndHashCode
@IdClass(ItemMaterialId.class)
public class ItemMaterial extends EntityDate {
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "material_id")
    private Material material;
}
