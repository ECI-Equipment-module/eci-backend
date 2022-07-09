package eci.server.CRCOModule.entity;

import eci.server.CRCOModule.entity.cofeatures.CoEffect;
import eci.server.CRCOModule.entity.cofeatures.CoStage;
import eci.server.CRCOModule.entity.features.CrImportance;
import eci.server.CRCOModule.entity.features.CrReason;
import eci.server.ItemModule.entity.member.Member;
import eci.server.ProjectModule.entity.project.CarType;
import eci.server.ProjectModule.entity.project.ClientOrganization;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "clientOrganization_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private ClientOrganization clientOrganization;

    @Column(nullable = false)
    private String coNumber;

    @Column
    private LocalDate coPublishPeriod;

    @Column
    private LocalDate coReceivedPeriod;

    @Column
    private boolean difference;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "carType_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private CarType carType;

    @Column
    private boolean costDifferent;

    @Column
    private String costDifference;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "co_reason_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private CrReason coReason;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "co_stage_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private CoStage coStage;

    @Column
    private LocalDate applyPeriod;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "co_effect_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private CoEffect coEffect;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "co_importance_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private CrImportance coImportance;

    @Column(nullable = false)
    private String name;

    @Lob
    @Column(nullable = false)
    private String content;

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

    @OneToMany(
            mappedBy = "changeOrder",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY)
    private List<ChangeRequest> changeRequests;

    @OneToMany(
            mappedBy = "changeOrder",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY)
    private List<CoNewItem> coNewItems;

}
