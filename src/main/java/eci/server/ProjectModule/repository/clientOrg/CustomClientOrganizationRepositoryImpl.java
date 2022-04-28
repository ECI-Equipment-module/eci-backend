package eci.server.ProjectModule.repository.clientOrg;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import eci.server.ProjectModule.dto.clientOrg.ClientOrganizationReadCondition;
import eci.server.ProjectModule.dto.clientOrg.ClientOrganizationReadResponse;
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
import static eci.server.ProjectModule.entity.QClientOrganization.clientOrganization;

@Transactional(readOnly = true)
public class CustomClientOrganizationRepositoryImpl extends QuerydslRepositorySupport implements CustomClientOrganizationRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public CustomClientOrganizationRepositoryImpl(JPAQueryFactory jpaQueryFactory) { // 4
        super(ProjectType.class);
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    public Page<ClientOrganizationReadResponse> findAllByCondition(ClientOrganizationReadCondition cond) {
        Pageable pageable = PageRequest.of(cond.getPage(), cond.getSize());
        Predicate predicate = createPredicate(cond);
        return new PageImpl<>(fetchAll(predicate, pageable), pageable, fetchCount(predicate));
    }


    private List<ClientOrganizationReadResponse> fetchAll(Predicate predicate, Pageable pageable) { // 6
        return getQuerydsl().applyPagination(
                pageable,
                jpaQueryFactory
                        .select(constructor(
                                ClientOrganizationReadResponse.class,
                                clientOrganization.id,
                                clientOrganization.name
                        ))
                        .from(clientOrganization)
                        .where(predicate)
                        .orderBy(clientOrganization.id.desc())
        ).fetch();
    }

    private Long fetchCount(Predicate predicate) { // 7
        return jpaQueryFactory.select(
                        clientOrganization.count()
                ).from(clientOrganization).
                where(predicate).fetchOne();
    }

    private Predicate createPredicate(ClientOrganizationReadCondition cond) { // 8
        return new BooleanBuilder();
    }


    private <T> Predicate orConditions(List<T> values, Function<T, BooleanExpression> term) { // 11
        return values.stream()
                .map(term)
                .reduce(BooleanExpression::or)
                .orElse(null);
    }
}
