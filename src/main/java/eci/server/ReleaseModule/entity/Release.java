package eci.server.ReleaseModule.entity;

import eci.server.CRCOModule.entity.CoCoEffect;
import eci.server.CRCOModule.entity.co.ChangeOrder;
import eci.server.ItemModule.entity.entitycommon.EntityDate;
import eci.server.ItemModule.entity.member.Member;
import eci.server.NewItemModule.entity.NewItem;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.List;

@Getter
@Entity
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Release extends EntityDate {
    @Id

    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQUENCE2")
    @SequenceGenerator(name="SEQUENCE2", sequenceName="SEQUENCE2", allocationSize=1)

    private Long id;

    @Column(nullable = false)
    private String releaseTitle;

    @Lob
    @Column(nullable = false)
    private String releaseContent;

    @Column
    private String releaseNumber;
    //save 할 시에 type + id 값으로 지정

    @OneToOne
    @JoinColumn(name = "new_item_id")
    private NewItem newItem;

    @OneToOne
    @JoinColumn(name = "co_id")
    private ChangeOrder changeOrder;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "releaseType_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private ReleaseType releaseType;


    @OneToMany(
            mappedBy = "releaseOrganization",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY)
    //affected item
    private List<ReleaseOrganization> releaseOrganization;

    @OneToMany(
            mappedBy = "release",
            cascade = CascadeType.PERSIST,
            orphanRemoval = true
    )
    private List<ReleaseAttachment> releaseAttachments;

}
