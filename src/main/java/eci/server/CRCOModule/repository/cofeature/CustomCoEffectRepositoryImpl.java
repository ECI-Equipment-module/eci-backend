package eci.server.CRCOModule.repository.cofeature;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import eci.server.CRCOModule.dto.featureresponse.CoEffectReadResponse;
import eci.server.CRCOModule.dto.featurescond.CoEffectReadCondition;
import eci.server.CRCOModule.entity.cofeatures.CoEffect;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.Function;

import static com.querydsl.core.types.Projections.constructor;
import static eci.server.CRCOModule.entity.cofeatures.QCoEffect.coEffect;

@Transactional(readOnly = true)
public class CustomCoEffectRepositoryImpl extends QuerydslRepositorySupport implements CustomCoEffectRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public CustomCoEffectRepositoryImpl(JPAQueryFactory jpaQueryFactory) {
        super(CoEffect.class);
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    public Page<CoEffectReadResponse> findAllByCondition(CoEffectReadCondition cond) {
        Pageable pageable = PageRequest.of(cond.getPage(), cond.getSize());
        Predicate predicate = createPredicate(cond);
        return new PageImpl<>(fetchAll(predicate, pageable), pageable, fetchCount(predicate));
    }


    private List<CoEffectReadResponse> fetchAll(Predicate predicate, Pageable pageable) { // 6
        return getQuerydsl().applyPagination(
                pageable,
                jpaQueryFactory
                        .select(constructor(
                                CoEffectReadResponse.class,
                                coEffect.id,
                                coEffect.name
                        ))
                        .from(coEffect)
                        .where(predicate)
                        .orderBy(coEffect.id.desc())
        ).fetch();
    }

    private Long fetchCount(Predicate predicate) { // 7
        return jpaQueryFactory.select(
                        coEffect.count()
                ).from(coEffect).
                where(predicate).fetchOne();
    }

    private Predicate createPredicate(CoEffectReadCondition cond) { // 8
        return new BooleanBuilder();
    }


    private <T> Predicate orConditions(List<T> values, Function<T, BooleanExpression> term) { // 11
        return values.stream()
                .map(term)
                .reduce(BooleanExpression::or)
                .orElse(null);
    }
}
