package eci.server.ItemModule.entity.item;

import eci.server.ItemModule.entity.member.Member;
import eci.server.ItemModule.entity.member.Role;
import lombok.*;

import javax.persistence.Embeddable;

@Embeddable
@EqualsAndHashCode
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ItemMaterialId {
    private Item item;
    private Material material;
}
