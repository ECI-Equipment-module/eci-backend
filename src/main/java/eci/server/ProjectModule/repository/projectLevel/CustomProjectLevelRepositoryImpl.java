package eci.server.ProjectModule.repository.projectLevel;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import eci.server.ProjectModule.dto.projectLevel.ProjectLevelReadCondition;
import eci.server.ProjectModule.dto.projectLevel.ProjectLevelReadResponse;
import eci.server.ProjectModule.entity.project.ProjectLevel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.Function;

import static com.querydsl.core.types.Projections.constructor;
import static eci.server.ProjectModule.entity.project.QProjectLevel.projectLevel;

@Transactional(readOnly = true)
public class CustomProjectLevelRepositoryImpl extends QuerydslRepositorySupport implements CustomProjectLevelRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public CustomProjectLevelRepositoryImpl(JPAQueryFactory jpaQueryFactory) { // 4
        super(ProjectLevel.class);
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    public Page<ProjectLevelReadResponse> findAllByCondition(ProjectLevelReadCondition cond) {
        Pageable pageable = PageRequest.of(cond.getPage(), cond.getSize());
        Predicate predicate = createPredicate(cond);
        return new PageImpl<>(fetchAll(predicate, pageable), pageable, fetchCount(predicate));
    }


    private List<ProjectLevelReadResponse> fetchAll(Predicate predicate, Pageable pageable) { // 6
        return getQuerydsl().applyPagination(
                pageable,
                jpaQueryFactory
                        .select(constructor(
                                ProjectLevelReadResponse.class,
                                projectLevel.id,
                                projectLevel.name
                        ))
                        .from(projectLevel)
                        .where(predicate)
                        .orderBy(projectLevel.id.desc())
        ).fetch();
    }

    private Long fetchCount(Predicate predicate) { // 7
        return jpaQueryFactory.select(
                        projectLevel.count()
                ).from(projectLevel).
                where(predicate).fetchOne();
    }

    private Predicate createPredicate(ProjectLevelReadCondition cond) { // 8
        return new BooleanBuilder();
    }


    private <T> Predicate orConditions(List<T> values, Function<T, BooleanExpression> term) { // 11
        return values.stream()
                .map(term)
                .reduce(BooleanExpression::or)
                .orElse(null);
    }
}
