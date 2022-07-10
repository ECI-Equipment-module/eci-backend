package eci.server.CRCOModule.repository.features;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import eci.server.CRCOModule.dto.featureresponse.CrImportanceResponse;
import eci.server.CRCOModule.dto.featurescond.CrImportanceReadCondition;
import eci.server.CRCOModule.entity.features.CrImportance;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.Function;

import static com.querydsl.core.types.Projections.constructor;
import static eci.server.CRCOModule.entity.features.QCrImportance.crImportance;


@Transactional(readOnly = true)
public class CustomCrImportanceRepositoryImpl extends QuerydslRepositorySupport implements CustomCrImportanceRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public CustomCrImportanceRepositoryImpl(JPAQueryFactory jpaQueryFactory) {
        super(CrImportance.class);
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    public Page<CrImportanceResponse> findAllByCondition(CrImportanceReadCondition cond) {
        Pageable pageable = PageRequest.of(cond.getPage(), cond.getSize());
        Predicate predicate = createPredicate(cond);
        return new PageImpl<>(fetchAll(predicate, pageable), pageable, fetchCount(predicate));
    }


    private List<CrImportanceResponse> fetchAll(Predicate predicate, Pageable pageable) { // 6
        return getQuerydsl().applyPagination(
                pageable,
                jpaQueryFactory
                        .select(constructor(
                                CrImportanceResponse.class,
                                crImportance.id,
                                crImportance.name
                        ))
                        .from(crImportance)
                        .where(predicate)
                        .orderBy(crImportance.id.desc())
        ).fetch();
    }

    private Long fetchCount(Predicate predicate) { // 7
        return jpaQueryFactory.select(
                        crImportance.count()
                ).from(crImportance).
                where(predicate).fetchOne();
    }

    private Predicate createPredicate(CrImportanceReadCondition cond) { // 8
        return new BooleanBuilder();
    }


    private <T> Predicate orConditions(List<T> values, Function<T, BooleanExpression> term) { // 11
        return values.stream()
                .map(term)
                .reduce(BooleanExpression::or)
                .orElse(null);
    }
}
