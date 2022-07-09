package eci.server.CRCOModule.repository.features;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import eci.server.CRCOModule.dto.featureresponse.CrReasonReadResponse;
import eci.server.CRCOModule.dto.featurescond.CrReasonReadCondition;
import eci.server.CRCOModule.entity.features.CrReason;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.Function;

import static com.querydsl.core.types.Projections.constructor;
import static eci.server.CRCOModule.entity.features.QCrReason.crReason;

@Transactional(readOnly = true)
public class CustomCrReasonRepositoryImpl extends QuerydslRepositorySupport implements CustomCrReasonRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public CustomCrReasonRepositoryImpl(JPAQueryFactory jpaQueryFactory) {
        super(CrReason.class);
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    public Page<CrReasonReadResponse> findAllByCondition(CrReasonReadCondition cond) {
        Pageable pageable = PageRequest.of(cond.getPage(), cond.getSize());
        Predicate predicate = createPredicate(cond);
        return new PageImpl<>(fetchAll(predicate, pageable), pageable, fetchCount(predicate));
    }


    private List<CrReasonReadResponse> fetchAll(Predicate predicate, Pageable pageable) {
        return getQuerydsl().applyPagination(
                pageable,
                jpaQueryFactory
                        .select(constructor(
                                CrReasonReadResponse.class,
                                crReason.id,
                                crReason.name
                        ))
                        .from(crReason)
                        .where(predicate)
                        .orderBy(crReason.id.desc())
        ).fetch();
    }

    private Long fetchCount(Predicate predicate) { // 7
        return jpaQueryFactory.select(
                        crReason.count()
                ).from(crReason).
                where(predicate).fetchOne();
    }

    private Predicate createPredicate(CrReasonReadCondition cond) { // 8
        return new BooleanBuilder();
    }


    private <T> Predicate orConditions(List<T> values, Function<T, BooleanExpression> term) { // 11
        return values.stream()
                .map(term)
                .reduce(BooleanExpression::or)
                .orElse(null);
    }
}
