package eci.server.CRCOModule.entity.cofeatures;

import eci.server.CRCOModule.entity.CoCoEffect;
import eci.server.CRCOModule.entity.CoNewItem;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CoEffect{
    @Id
        //@GeneratedValue(strategy = GenerationType.IDENTITY)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQUENCE2")
    @SequenceGenerator(name="SEQUENCE2", sequenceName="SEQUENCE2", allocationSize=1)

    private Long id;

    @Column(nullable = false)
    private String name;

    @OneToMany(
            mappedBy = "coEffect",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY)
    private List<CoCoEffect> coCoEffects;

    public CoEffect(
            String name
    ){
        this.name = name;
    }

}
