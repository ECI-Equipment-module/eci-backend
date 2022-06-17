package eci.server.DesignModule.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import eci.server.DesignModule.dto.DesignReadCondition;
import eci.server.DesignModule.dto.DesignReadDto;
import eci.server.DesignModule.entity.design.Design;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;
import java.util.function.Function;

import static com.querydsl.core.types.Projections.constructor;
import static eci.server.ItemModule.entity.newRoute.QRouteProduct.routeProduct;
import static eci.server.DesignModule.entity.design.QDesign.design;
import static eci.server.NewItemModule.entity.QNewItem.newItem;

public class CustomDesignRepositoryImpl extends QuerydslRepositorySupport implements CustomDesignRepository {


    private final JPAQueryFactory jpaQueryFactory;

    public CustomDesignRepositoryImpl(JPAQueryFactory jpaQueryFactory) { // 4
        super(Design.class);
        this.jpaQueryFactory = jpaQueryFactory;
    }

//    @Override
//    public Page<ProjectReadDto> findAllByCondition(ProjectReadCondition cond, ProjectMemberRequest req) {
//        Pageable pageable = PageRequest.of(cond.getPage(), cond.getSize());
//        Predicate predicate = createPredicate(cond);
//        return new PageImpl<>(fetchAll(predicate, pageable), pageable, fetchCount(predicate));
//    }

    @Override
    public Page<DesignReadDto> findAllByCondition(DesignReadCondition cond) {
        Pageable pageable = PageRequest.of(cond.getPage(), cond.getSize());
        Predicate predicate = createPredicate(cond);
        return new PageImpl<>(fetchAll(predicate, pageable), pageable, fetchCount(predicate));
    }


    private List<DesignReadDto> fetchAll(Predicate predicate, Pageable pageable){//,ProjectMemberRequest req) { // 6
        return getQuerydsl().applyPagination(
                pageable,
                jpaQueryFactory
                        .select(constructor(
                                DesignReadDto.class,
                                design.id,

                                newItem.name,
                                newItem.itemNumber,

                                design.tempsave,

                                routeProduct.route_name,

                                design.createdAt

                        ))
                        .from(design)

                        .join(newItem).on(design.newItem.id.eq(newItem.id))

                        .join(routeProduct).on(design.id.eq(routeProduct.project.id))

//                        .join(member).on(project.member.id.eq(memberId))
                        //지금 로그인된 멤버가 작성한 프로젝트

                        .where(predicate)

                        .orderBy(design.id.desc())


        ).fetch();
    }

    private Long fetchCount(Predicate predicate) { // 7
        return jpaQueryFactory.select(
                        design.count()
                ).from(design).
                where(predicate).fetchOne();
    }

    private Predicate createPredicate(DesignReadCondition cond) { // 8
        return new BooleanBuilder();
    }


    private <T> Predicate orConditions(List<T> values, Function<T, BooleanExpression > term) { // 11
        return values.stream()
                .map(term)
                .reduce(BooleanExpression::or)
                .orElse(null);
    }
}

