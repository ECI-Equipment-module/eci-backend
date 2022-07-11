package eci.server.CRCOModule.entity;

import eci.server.CRCOModule.entity.CoNewItemId;
import eci.server.CRCOModule.entity.co.ChangeOrder;
import eci.server.CRCOModule.entity.cofeatures.CoEffect;
import eci.server.NewItemModule.entity.NewItem;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EqualsAndHashCode
@IdClass(CoCoEffectId.class)
public class CoCoEffect {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "co_id")
    private ChangeOrder changeOrder;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "co_effect_id")
    private CoEffect coEffect;

}
