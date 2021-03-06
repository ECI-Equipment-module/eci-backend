package eci.server.ItemModule.entity.material;

import eci.server.ItemModule.entitycommon.EntityDate;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Material extends EntityDate {
    @Id

    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQUENCE2")
    @SequenceGenerator(name="SEQUENCE2", sequenceName="SEQUENCE2", allocationSize=1)

    private Long id;

    @Column(nullable = false)
    private String code;

    @Column(nullable = false)
    private String name;

    public Material(
            String code,
            String name
    ){
        this.code = code;
        this.name = name;
    }
}


