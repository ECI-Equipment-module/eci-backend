package eci.server.ItemModule.entity.route;

import eci.server.ItemModule.dto.route.RouteUpdateRequest;
import eci.server.ItemModule.entity.entitycommon.EntityDate;
import eci.server.ItemModule.entity.item.Item;
import eci.server.ItemModule.entity.member.Member;
import eci.server.ItemModule.exception.member.sign.MemberNotFoundException;
import eci.server.ItemModule.repository.item.ItemRepository;
import eci.server.ItemModule.repository.member.MemberRepository;
import eci.server.ItemModule.repository.route.RouteRepository;
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
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    private String lifecycleStatus;

    @Column(nullable = false)
    private int revisedCnt;

//    /**
//     * 최신 라우트만 true
//     */
//    @Column(nullable = false)
//    private boolean is_power;

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

    @Column(nullable = false)
    @Lob
    private String applicant_comment;

    /**
     * 리뷰어 지정자의 아이디
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reviewer_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member reviewer;

    @Column(nullable = false)
    @Lob
    private String reviewer_comment;

    /**
     * 승인자의 아이디
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approver_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member approver;

    @Column(nullable = false)
    @Lob
    private String approver_comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Route parent;

    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY)
    private List<Route> children = new ArrayList<>();

    /**
     * default : true
     * 값이 true라면 임시저장 가능, false라면 수정 불가능
     */
    @Column
    private Boolean inProgress;


    public Route(
            String type,
            String workflow,
            String workflowPhase,
            String lifecycleStatus,
            Integer revisedCnt,
            Member member,
            String applicant_comment,
            Member reviewer,
            String reviewer_comment,
            Member approver,
            String approver_comment,
            Item item,
            Route parent,
            Boolean inProgress
    ) {
        this.type = type;
        this.workflow = workflow;
        this.workflowPhase = workflowPhase;
        this.lifecycleStatus = lifecycleStatus;
        this.revisedCnt = revisedCnt;
        this.member = member;
        this.applicant_comment = applicant_comment;
        this.reviewer = reviewer;
        this.reviewer_comment = reviewer_comment;
        this.approver = approver;
        this.approver_comment = approver_comment;
        this.item = item;
        this.parent = parent;
        this.deleted = false;
        this.inProgress = true;
    }


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

    public RouteUpdateRequest update(RouteUpdateRequest req, MemberRepository memberRepository, ItemRepository itemRepository, RouteRepository routeRepository) {
        this.type = req.getType();
        this.workflow = req.getWorkflow();
        this.workflowPhase = req.getWorkflow(); //workflow 설정하면 그것에 맞는 이미지 파일 돌려주기
        this.lifecycleStatus = req.getLifecycleStatus();
        this.revisedCnt = req.getRevisedCnt()+64;
        this.applicant_comment =req.getApplicant_comment();
        this.reviewer = memberRepository.findById(req.getReviewerId()).orElseThrow(MemberNotFoundException::new);
        this.reviewer_comment = req.getReviewer_comment();
        this.approver = memberRepository.findById(req.getApproverId()).orElseThrow(MemberNotFoundException::new);
        this.approver_comment = req.getApprover_comment();
        this.parent = null;
        this.inProgress = req.getInProgress();

        return req;
    }

}