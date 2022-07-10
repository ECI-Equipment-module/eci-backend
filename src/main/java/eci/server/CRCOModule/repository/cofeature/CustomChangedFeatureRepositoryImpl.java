package eci.server.CRCOModule.repository.cofeature;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import eci.server.CRCOModule.dto.featureresponse.ChangedFeatureReadResponse;
import eci.server.CRCOModule.dto.featureresponse.CoStageReadResponse;
import eci.server.CRCOModule.dto.featurescond.ChangedFeatureReadCondition;
import eci.server.CRCOModule.dto.featurescond.CoStageReadCondition;
import eci.server.CRCOModule.entity.cofeatures.ChangedFeature;
import eci.server.CRCOModule.entity.cofeatures.CoStage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.Function;

import static com.querydsl.core.types.Projections.constructor;
import static eci.server.CRCOModule.entity.cofeatures.QChangedFeature.changedFeature;

@Transactional(readOnly = true)
public class CustomChangedFeatureRepositoryImpl extends QuerydslRepositorySupport implements CustomChangedFeatureRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public CustomChangedFeatureRepositoryImpl(JPAQueryFactory jpaQueryFactory) {
        super(ChangedFeature.class);
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    public Page<ChangedFeatureReadResponse> findAllByCondition(ChangedFeatureReadCondition cond) {
        Pageable pageable = PageRequest.of(cond.getPage(), cond.getSize());
        Predicate predicate = createPredicate(cond);
        return new PageImpl<>(fetchAll(predicate, pageable), pageable, fetchCount(predicate));
    }


    private List<ChangedFeatureReadResponse> fetchAll(Predicate predicate, Pageable pageable) { // 6
        return getQuerydsl().applyPagination(
                pageable,
                jpaQueryFactory
                        .select(constructor(
                                ChangedFeatureReadResponse.class,
                                changedFeature.id,
                                changedFeature.name
                        ))
                        .from(changedFeature)
                        .where(predicate)
                        .orderBy(changedFeature.id.desc())
        ).fetch();
    }

    private Long fetchCount(Predicate predicate) { // 7
        return jpaQueryFactory.select(
                        changedFeature.count()
                ).from(changedFeature).
                where(predicate).fetchOne();
    }

    private Predicate createPredicate(ChangedFeatureReadCondition cond) { // 8
        return new BooleanBuilder();
    }


    private <T> Predicate orConditions(List<T> values, Function<T, BooleanExpression> term) { // 11
        return values.stream()
                .map(term)
                .reduce(BooleanExpression::or)
                .orElse(null);
    }
}


