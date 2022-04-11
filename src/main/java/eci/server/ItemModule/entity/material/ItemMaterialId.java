package eci.server.ItemModule.entity.material;

import eci.server.ItemModule.entity.item.Item;
import eci.server.ItemModule.entity.material.Material;
import lombok.*;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@EqualsAndHashCode
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ItemMaterialId implements Serializable {
    private Item item;
    private Material material;
}
