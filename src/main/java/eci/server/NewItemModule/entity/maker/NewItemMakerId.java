package eci.server.NewItemModule.entity.maker;

import eci.server.NewItemModule.entity.supplier.Maker;
import eci.server.NewItemModule.entity.NewItem;
import lombok.*;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@EqualsAndHashCode
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class NewItemMakerId implements Serializable {
    private NewItem newItem;
    private Maker maker;
}

