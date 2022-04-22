package eci.server.ItemModule.entity.newRoute;

import eci.server.ItemModule.dto.newRoute.RouteProductUpdateRequest;
import eci.server.ItemModule.entity.item.ItemManufacture;
import eci.server.ItemModule.entity.material.ItemMaterial;
import eci.server.ItemModule.entity.member.Member;
import eci.server.ItemModule.entitycommon.EntityDate;
import eci.server.ItemModule.exception.RouteProductNotFoundException;
import eci.server.ItemModule.repository.newRoute.RouteProductRepository;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Slf4j
public class RouteProduct extends EntityDate {

    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="SEQUENCE1")
    @SequenceGenerator(name="SEQUENCE1", sequenceName="SEQUENCE1", allocationSize=1)
    private Long id;

    /**
     * 라우트에서의 순서를 나타냄
     */
    @Column(nullable = false)
    private Integer sequence;

    /**
     * request, approve, review, design, complete 중 하나
     */
    @Column(nullable = false)
    private String type;

    /**
     * comment 남기기
     */
    @Column(nullable = false)
    @Lob
    private String comments;

    /**
     * false : 하기 전 , true : 실행 후
     */
    @Column(nullable = false)
    private boolean passed;

    /**
     * 거절당하고 새로 만들어진 라우트 제작물이라면
     * rejected = true (Rejected 투두)
     */
    @Column(nullable = false)
    private boolean rejected;

    /**
     * 화면에 띄울 변수
     */
    @Column(nullable = false)
    private boolean show;

    @Column(nullable = false)
    private boolean disabled;

    @OneToMany(
            mappedBy = "routeProduct",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY)
    private List<RouteProductMember> members;

    /**
     * routeproduct 가 속하는 route 하나
     */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "newRoute_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private NewRoute newRoute;
    /**
     * 라우트 프로덕트 생성자 (reject 시 재 생산용)
     */
    public RouteProduct(
            Integer sequence,
            String type,
            String comments,
            boolean passed,
            boolean rejected,
            boolean show,
            boolean disabled,
            List<Member> member,
            NewRoute newRoute

    ) {

        this.sequence = sequence;
        this.type = type;
        this.comments = comments;
        this.passed = passed;
        this.rejected = rejected;
        this.show = show;
        this.disabled = disabled;
        this.members = member.stream().map(
                        r -> new RouteProductMember(
                                this, r)
                )
                .collect(toList());
        this.newRoute = newRoute;

    }


    /**
     * 라우트 프로덕트 승인 시 업데이트
     * @param id
     * @param req
     * @param routeProductRepository
     * @return
     */
    public RouteProductUpdateRequest update(

            Long id,    /**기존 애들 용**/
            RouteProductUpdateRequest req,
            RouteProductRepository routeProductRepository

    ) {

        RouteProduct routeProduct =
                routeProductRepository
                        .findById(id)
                        .orElseThrow(RouteProductNotFoundException::new);

        this.sequence = routeProduct.getSequence();
        this.type = routeProduct.getType();
        this.comments = req.getComment();
        this.passed = true;
        this.rejected = false;
        this.newRoute = routeProduct.getNewRoute();
        this.show = true;
        this.disabled = false;
        return req;
    }

    public void setPassed(boolean passed) {
        this.passed = passed;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setComment(String comment) {
        this.comments = comment;
    }

    public void setShow(boolean show) {
        this.show = show;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public void setRejected(boolean rejected) {
        this.rejected = rejected;
    }
}
