//package eci.server.BomModule.entity;
//
//import lombok.AccessLevel;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import org.hibernate.annotations.OnDelete;
//import org.hibernate.annotations.OnDeleteAction;
//
//import javax.persistence.*;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
//@Getter
//@Entity
//@NoArgsConstructor(access = AccessLevel.PROTECTED)
//public class PreliminaryBomCard {
//    @Id
////    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQUENCE2")
//    @SequenceGenerator(name="SEQUENCE2", sequenceName="SEQUENCE2", allocationSize=1)
//    private Long id;
//
//    @Column(nullable = false)
//    private String cardNumber;
//
//    @Column(nullable = false)
//    private String cardName;
//
//    @Column(nullable = false)
//    private String classification;
//
//    @Column(nullable = false)
//    private String cardType;
//
//    @Column(nullable = false)
//    private String sharing;
//
//    @Column(nullable = false)
//    private boolean deleted;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "preliminaryBom_id", nullable = false)
//    @OnDelete(action = OnDeleteAction.CASCADE)
//    private PreliminaryBom preliminaryBom;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "parent_id")
//    @OnDelete(action = OnDeleteAction.CASCADE)
//    private PreliminaryBomCard parent;
//
//    @OneToMany(mappedBy = "parent")
//    private List<PreliminaryBomCard> children = new ArrayList<>(); // 5
//
//    public PreliminaryBomCard(
//            String number,
//            String name,
//            String classification,
//            String type,
//            String sharing,
//            PreliminaryBom preliminaryBom,
//            PreliminaryBomCard parent) {
//        this.cardNumber = number;
//        this.cardName = name;
//        this.classification = classification;
//        this.cardType = type;
//        this.sharing = sharing;
//        this.preliminaryBom = preliminaryBom;
//        this.parent = parent;
//        this.deleted = false;
//    }
//
//    public Optional<PreliminaryBomCard> findDeletablePreliminaryBomCard() { // 6
//        return hasChildren() ? Optional.empty() : Optional.of(findDeletablePreliminaryBomCardByParent());
//    }
//
//    //실제로 삭제하는 것이 아니라 삭제 표시
//    //하위 댓글이 남아있어서 실제로 제거할 수 없는 댓글
//    public void delete() {
//        this.deleted = true;
//    }
//
//    private PreliminaryBomCard findDeletablePreliminaryBomCardByParent() {
//        return isDeletableParent() ? getParent().findDeletablePreliminaryBomCardByParent() : this;
//    }
//
//    private boolean hasChildren() {
//        return getChildren().size() != 0;
//    }
//
//    private boolean isDeletableParent() {
//        return getParent() != null && getParent().isDeleted() && getParent().getChildren().size() == 1;
//    }
//}
