package eci.server.ReleaseModule.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;
import java.util.Set;


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
            cascade = CascadeType.ALL,//.MERGE,
            orphanRemoval = true,
            fetch = FetchType.LAZY)
    private Set<ReleaseOrgRelease> releaseOrgReleases;

    public ReleaseOrganization(
            String name
    ){
        this.name = name;
    }
}
