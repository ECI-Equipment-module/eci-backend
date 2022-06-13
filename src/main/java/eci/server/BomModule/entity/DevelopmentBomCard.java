package eci.server.BomModule.entity;

import eci.server.NewItemModule.entity.NewItem;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DevelopmentBomCard {
    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQUENCE2")
    @SequenceGenerator(name="SEQUENCE2", sequenceName="SEQUENCE2", allocationSize=1)
    private Long id;

    @Column(nullable = false)
    private String cardNumber;

    @Column(nullable = false)
    private String cardName;

    @Column(nullable = false)
    private String classification;

    @Column(nullable = false)
    private String cardType;

    @Column(nullable = false)
    private String sharing;

    @Column(nullable = false)
    private boolean deleted;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "development_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private DevelopmentBom developmentBom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private DevelopmentBomCard parent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "real_item_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private NewItem newItem;

    @OneToMany(mappedBy = "parent")
    private List<DevelopmentBomCard> children = new ArrayList<>();

    public DevelopmentBomCard(
            String number,
            String name,
            String classification,
            String type,
            String sharing,
            DevelopmentBom developmentBom,
            DevelopmentBomCard parent,
            NewItem newItem
    ) {
        this.cardNumber = number;
        this.cardName = name;
        this.classification = classification;
        this.cardType = type;
        this.sharing = sharing;
        this.developmentBom = developmentBom;
        this.parent = parent;
        this.deleted = false;
        this.newItem = newItem;
    }

    public Optional<DevelopmentBomCard> findDeletableDevelopmentBomCard() {
        return hasChildren() ? Optional.empty() : Optional.of(findDeletableDevelopmentBomCardByParent());
    }

    //실제로 삭제하는 것이 아니라 삭제 표시
    //하위 댓글이 남아있어서 실제로 제거할 수 없는 댓글
    public void delete() {
        this.deleted = true;
    }

    private DevelopmentBomCard findDeletableDevelopmentBomCardByParent() {
        return isDeletableParent() ? getParent().findDeletableDevelopmentBomCardByParent() : this;
    }

    private boolean hasChildren() {
        return getChildren().size() != 0;
    }

    private boolean isDeletableParent() {
        return getParent() != null && getParent().isDeleted() && getParent().getChildren().size() == 1;
    }
}
