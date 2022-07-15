//package eci.server.ReleaseModule.entity;
//
//import lombok.*;
//
//import javax.persistence.*;
//
//@Entity
//@Getter
//@NoArgsConstructor(access = AccessLevel.PROTECTED)
//@AllArgsConstructor
//@EqualsAndHashCode
//@IdClass(ReleaseOrgReleaseId.class)
//public class ReleaseOrgRelease {
//
//    @Id
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "release_org_id")
//    private ReleaseOrganization releaseOrganization;
//
//    @Id
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "release_id")
//    private Release release;
//
//}