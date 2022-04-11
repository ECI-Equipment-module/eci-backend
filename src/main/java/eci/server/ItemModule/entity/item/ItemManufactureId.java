package eci.server.ItemModule.entity.item;

import lombok.*;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@EqualsAndHashCode
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ItemManufactureId implements Serializable {
    private Item item;
    private Manufacture manufacture;
}
