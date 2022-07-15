package eci.server.ReleaseModule.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReleaseOrganization{
    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQUENCE2")
    @SequenceGenerator(name="SEQUENCE2", sequenceName="SEQUENCE2", allocationSize=1)

    private Long id;

    @Column(nullable = false)
    private String name;

    @OneToMany(
            mappedBy = "releaseOrganization",
            cascade = CascadeType.REMOVE,
            fetch = FetchType.LAZY)
    private List<ReleaseOrgRelease> coCoEffects;


    public ReleaseOrganization(
            String name
    ){
        this.name = name;
    }
}
