package eci.server.ReleaseModule.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EqualsAndHashCode
//@IdClass(ReleaseOrgReleaseId.class)
public class ReleaseOrgRelease {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQUENCE2")
    //@SequenceGenerator(name="SEQUENCE2", sequenceName="SEQUENCE2", allocationSize=1)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "release_org_id")
    private ReleaseOrganization releaseOrganization;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "release_id")
    private Releasing release;

    public ReleaseOrgRelease(
            ReleaseOrganization releaseOrganization,
            Releasing release
    ){
        this.releaseOrganization = releaseOrganization;
        this.release = release;
    }

}