package eci.server.ItemModule.entity.item;

import eci.server.ItemModule.entitycommon.EntityDate;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Color extends EntityDate {
    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQUENCE2")
    @SequenceGenerator(name="SEQUENCE2", sequenceName="SEQUENCE2", allocationSize=1)

    private Long id;

    @Column(nullable = false)
    private String code;

    @Column(nullable = false)
    private String color;

    public Color(
            String code,
            String color
    ){
        this.code=code;
        this.color=color;
    }
}
