package eci.server.CRCOModule.entity;

import eci.server.CRCOModule.entity.ChangeOrder;
import eci.server.NewItemModule.entity.NewItem;
import lombok.*;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@EqualsAndHashCode
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class CoNewItemId implements Serializable {
    private ChangeOrder changeOrder;
    private NewItem newItem;
}
