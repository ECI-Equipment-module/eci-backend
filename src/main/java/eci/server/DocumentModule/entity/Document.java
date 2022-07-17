package eci.server.DocumentModule.entity;

import eci.server.DocumentModule.entity.classification.DocClassification;
import eci.server.ItemModule.entity.member.Member;
import eci.server.ItemModule.entitycommon.EntityDate;
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
public class Document extends EntityDate {
    @Id

    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQUENCE2")
    @SequenceGenerator(name = "SEQUENCE2", sequenceName = "SEQUENCE2", allocationSize = 1)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doc1_id", nullable = false)
    @JoinColumn(name = "doc2_id", nullable = false)
    private DocClassification classification;

    @Column(nullable = false)
    private String documentTitle;

    @Lob
    @Column(nullable = false)
    private String documentContent;

    @Column
    private String documentNumber;
    //save 할 시에 type + id 값으로 지정
;

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

    @Column(nullable = false)
    private boolean revise_progress;

    @Column
    private int revision;

    @Column
    private Integer released;

    //nullable
    @OneToOne
    @JoinColumn(name = "revise_id")
    private Document reviseTargetNewItem;

    @OneToMany(
            mappedBy = "document",
            cascade = CascadeType.PERSIST,
            orphanRemoval = true
    )
    private List<DocumentAttachment> attachments;




}

