package eci.server.ProjectModule.repository.produceOrg;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import eci.server.ProjectModule.dto.produceOrg.ProduceOrganizationReadCondition;
import eci.server.ProjectModule.dto.produceOrg.ProduceOrganizationReadResponse;
import eci.server.ProjectModule.entity.project.ProjectType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.Function;

import static com.querydsl.core.types.Projections.constructor;
import static eci.server.ProjectModule.entity.project.QProduceOrganization.produceOrganization;

@Transactional(readOnly = true)
public class CustomProduceOrganizationRepositoryImpl extends QuerydslRepositorySupport implements CustomProduceOrganizationRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public CustomProduceOrganizationRepositoryImpl(JPAQueryFactory jpaQueryFactory) { // 4
        super(ProjectType.class);
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    public Page<ProduceOrganizationReadResponse> findAllByCondition(ProduceOrganizationReadCondition cond) {
        Pageable pageable = PageRequest.of(cond.getPage(), cond.getSize());
        Predicate predicate = createPredicate(cond);
        return new PageImpl<>(fetchAll(predicate, pageable), pageable, fetchCount(predicate));
    }


    private List<ProduceOrganizationReadResponse> fetchAll(Predicate predicate, Pageable pageable) { // 6
        return getQuerydsl().applyPagination(
                pageable,
                jpaQueryFactory
                        .select(constructor(
                                ProduceOrganizationReadResponse.class,
                                produceOrganization.id,
                                produceOrganization.name
                        ))
                        .from(produceOrganization)
                        .where(predicate)
                        .orderBy(produceOrganization.id.desc())
        ).fetch();
    }

    private Long fetchCount(Predicate predicate) { // 7
        return jpaQueryFactory.select(
                        produceOrganization.count()
                ).from(produceOrganization).
                where(predicate).fetchOne();
    }

    private Predicate createPredicate(ProduceOrganizationReadCondition cond) { // 8
        return new BooleanBuilder();
    }


    private <T> Predicate orConditions(List<T> values, Function<T, BooleanExpression> term) { // 11
        return values.stream()
                .map(term)
                .reduce(BooleanExpression::or)
                .orElse(null);
    }
}
