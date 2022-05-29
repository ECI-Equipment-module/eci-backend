package eci.server.NewItemModule.repository.coatingWay;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import eci.server.NewItemModule.dto.coatingcommon.CoatingReadResponse;
import eci.server.NewItemModule.dto.coatingWay.CoatingWayReadCondition;
import eci.server.NewItemModule.entity.coating.CoatingType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.Function;

import static com.querydsl.core.types.Projections.constructor;
import static eci.server.NewItemModule.entity.coating.QCoatingWay.coatingWay;

@Transactional(readOnly = true)
public class CustomCoatingWayRepositoryImpl extends QuerydslRepositorySupport implements CustomCoatingWayRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public CustomCoatingWayRepositoryImpl(JPAQueryFactory jpaQueryFactory) { // 4
        super(CoatingType.class);
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    public Page<CoatingReadResponse> findAllByCondition(CoatingWayReadCondition cond) {
        Pageable pageable = PageRequest.of(cond.getPage(), cond.getSize());
        Predicate predicate = createPredicate(cond);
        return new PageImpl<>(fetchAll(predicate, pageable), pageable, fetchCount(predicate));
    }


    private List<CoatingReadResponse> fetchAll(Predicate predicate, Pageable pageable) { // 6
        return getQuerydsl().applyPagination(
                pageable,
                jpaQueryFactory
                        .select(constructor(
                                CoatingReadResponse.class,
                                coatingWay.id,
                                coatingWay.name))
                        .from(coatingWay)
                        .where(predicate)
                        .orderBy(coatingWay.id.desc())
        ).fetch();
    }

    private Long fetchCount(Predicate predicate) { // 7
        return jpaQueryFactory.select(
                        coatingWay.count()
                ).from(coatingWay).
                where(predicate).fetchOne();
    }

    private Predicate createPredicate(CoatingWayReadCondition cond) { // 8
        return new BooleanBuilder();
    }


    private <T> Predicate orConditions(List<T> values, Function<T, BooleanExpression> term) { // 11
        return values.stream()
                .map(term)
                .reduce(BooleanExpression::or)
                .orElse(null);
    }
}
