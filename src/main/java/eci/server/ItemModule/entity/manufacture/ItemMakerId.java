package eci.server.ItemModule.entity.manufacture;

import eci.server.ItemModule.entity.item.Item;
import eci.server.NewItemModule.entity.supplier.Maker;
import lombok.*;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@EqualsAndHashCode
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ItemMakerId implements Serializable {
    private Item item;
    private Maker maker;
}