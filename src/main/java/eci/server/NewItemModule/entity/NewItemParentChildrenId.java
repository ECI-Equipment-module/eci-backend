package eci.server.NewItemModule.entity;

import eci.server.NewItemModule.entity.supplier.Maker;
import lombok.*;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@EqualsAndHashCode
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class NewItemParentChildrenId implements Serializable {
    private NewItem parent;
    private NewItem children;
}

