package eci.server.entity.route;

import eci.server.entity.entitycommon.EntityDate;
import eci.server.entity.item.Item;
import eci.server.entity.member.Member;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Slf4j
public class Route extends EntityDate {

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="SEQUENCE1")
    @SequenceGenerator(name="SEQUENCE1", sequenceName="SEQUENCE1", allocationSize=1)
    private Long id;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private String workflow;

    @Column(nullable = false)
    private String workflowPhase;

    @Column(nullable = false)
    private Character lifecycleStatus;

    @Column(nullable = false)
    private Integer revisedCnt;

    /**
     * 최신 라우트만 true
     */
    @Column(nullable = false)
    private boolean is_power;

    /**
     * 삭제 여부 표시
     */
    @Column(nullable = false)
    private boolean deleted;

    /**
     * 아이템 작성자의 아이디
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member member;

//    @Column(nullable = false)
//    @Lob
//    private String request_comment;

    /**
     * 리뷰어 지정자의 아이디
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reviewer_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member reviewer;

//    @Column(nullable = false)
//    @Lob
//    private String review_comment;

    /**
     * 승인자의 아이디
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approver_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member approval;

//    @Column(nullable = false)
//    @Lob
//    private String approve_comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Item item; // 3

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Route parent; // 4

    @OneToMany(mappedBy = "parent")
    private List<Route> children = new ArrayList<>(); // 5

    public Route(String type, String workflow, String workflowPhase, Character lifecycleStatus, Integer revisedCnt, Member member, Member reviewer, Member approval, Item item, Route parent) {
        this.type = type;
        this.workflow = workflow;
        this.workflowPhase = workflowPhase;
        this.lifecycleStatus = lifecycleStatus;
        this.revisedCnt = revisedCnt;
        this.member = member;
        this.reviewer = reviewer;
        this.approval = approval;
        this.item = item;
        this.parent = parent;
        this.deleted = false;
    }

    // Comment.java
    public Optional<Route> findDeletableRoute() {
        return hasChildren() ? Optional.empty() : Optional.of(findDeletableRouteByParent());
    }

    public void delete() {
        this.deleted = true;
    }

    private Route findDeletableRouteByParent() { // 1
        if (isDeletedParent()) {
            Route deletableParent = getParent().findDeletableRouteByParent();
            if(getParent().getChildren().size() == 1) return deletableParent;
        }
        return this;
    }

    private boolean hasChildren() {
        return getChildren().size() != 0;
    }

    private boolean isDeletedParent() { // 2
        return getParent() != null && getParent().isDeleted();
    }
}