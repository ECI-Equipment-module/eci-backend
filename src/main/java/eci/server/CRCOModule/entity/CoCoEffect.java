package eci.server.CRCOModule.entity;

import eci.server.CRCOModule.entity.co.ChangeOrder;
import eci.server.CRCOModule.entity.cofeatures.CoEffect;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
//@EqualsAndHashCode
//@IdClass(CoCoEffectId.class)
public class CoCoEffect {
//    @Id
//    private Long id;
    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQUENCE2")
    @SequenceGenerator(name="SEQUENCE2", sequenceName="SEQUENCE2", allocationSize=1)
    private Long id;

//    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "co_id")
    private ChangeOrder changeOrder;

//    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "co_effect_id")
    private CoEffect coEffect;

    public CoCoEffect(
            ChangeOrder changeOrder,
            CoEffect coEffect
    ){
        this.changeOrder = changeOrder;
        this.coEffect = coEffect;
    }


}
