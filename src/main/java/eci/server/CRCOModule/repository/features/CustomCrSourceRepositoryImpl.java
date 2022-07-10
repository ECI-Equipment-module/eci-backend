package eci.server.CRCOModule.repository.features;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import eci.server.CRCOModule.dto.featureresponse.CrSourceReadResponse;
import eci.server.CRCOModule.dto.featurescond.CrSourceReadCondition;
import eci.server.CRCOModule.entity.features.CrSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.Function;

import static com.querydsl.core.types.Projections.constructor;
import static eci.server.CRCOModule.entity.features.QCrSource.crSource;

@Transactional(readOnly = true)
public class CustomCrSourceRepositoryImpl extends QuerydslRepositorySupport implements CustomCrSourceRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public CustomCrSourceRepositoryImpl(JPAQueryFactory jpaQueryFactory) {
        super(CrSource.class);
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    public Page<CrSourceReadResponse> findAllByCondition(CrSourceReadCondition cond) {
        Pageable pageable = PageRequest.of(cond.getPage(), cond.getSize());
        Predicate predicate = createPredicate(cond);
        return new PageImpl<>(fetchAll(predicate, pageable), pageable, fetchCount(predicate));
    }


    private List<CrSourceReadResponse> fetchAll(Predicate predicate, Pageable pageable) { // 6
        return getQuerydsl().applyPagination(
                pageable,
                jpaQueryFactory
                        .select(constructor(
                                CrSourceReadResponse.class,
                                crSource.id,
                                crSource.name
                        ))
                        .from(crSource)
                        .where(predicate)
                        .orderBy(crSource.id.desc())
        ).fetch();
    }

    private Long fetchCount(Predicate predicate) { // 7
        return jpaQueryFactory.select(
                        crSource.count()
                ).from(crSource).
                where(predicate).fetchOne();
    }

    private Predicate createPredicate(CrSourceReadCondition cond) { // 8
        return new BooleanBuilder();
    }


    private <T> Predicate orConditions(List<T> values, Function<T, BooleanExpression> term) { // 11
        return values.stream()
                .map(term)
                .reduce(BooleanExpression::or)
                .orElse(null);
    }
}
