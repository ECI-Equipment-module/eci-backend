package eci.server.CRCOModule.entity;

import eci.server.ItemModule.entity.member.Member;
import eci.server.ProjectModule.entity.project.ClientOrganization;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Entity
@Setter

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChangeOrder {
    @Id
    //    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQUENCE3")
    @SequenceGenerator(name="SEQUENCE3", sequenceName="SEQUENCE3", allocationSize=1)
    private Long id;

    @Column(nullable = false)
    private String coNumber;

    @Column
    private LocalDate coPublishPeriod;

    @Column
    private LocalDate coReceivedPeriod;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "clientOrganization_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private ClientOrganization clientOrganization;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "member_id",
            nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "modifier_id",
            nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member modifier;

    @Column(nullable = false)
    private Boolean tempsave;

    @Column(nullable = false)
    private Boolean readonly; //05-12반영

}
