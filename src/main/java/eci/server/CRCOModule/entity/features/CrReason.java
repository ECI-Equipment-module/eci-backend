package eci.server.CRCOModule.entity.features;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CrReason {
    @Id

<<<<<<< HEAD

=======
>>>>>>> be-feature/0712-deploy
        @GeneratedValue(strategy = GenerationType.IDENTITY)
    //@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQUENCE2")
    //@SequenceGenerator(name="SEQUENCE2", sequenceName="SEQUENCE2", allocationSize=1)

<<<<<<< HEAD
=======

>>>>>>> be-feature/0712-deploy

    private Long id;

    @Column(nullable = false)
    private String name;

    public CrReason(
            String name
    ){
        this.name = name;
    }

}
