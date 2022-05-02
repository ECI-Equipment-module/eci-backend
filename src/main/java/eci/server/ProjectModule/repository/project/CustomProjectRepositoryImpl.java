package eci.server.ProjectModule.repository.project;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import eci.server.ItemModule.dto.member.MemberReadCondition;
import eci.server.ItemModule.dto.member.MemberSimpleDto;
import eci.server.ItemModule.entity.member.Member;
import eci.server.ProjectModule.dto.ProjectReadCondition;
import eci.server.ProjectModule.dto.ProjectReadDto;
import eci.server.ProjectModule.entity.project.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.Function;

import static com.querydsl.core.types.Projections.constructor;
import static eci.server.ItemModule.entity.item.QItem.item;
import static eci.server.ProjectModule.entity.project.QProject.project;

@Transactional(readOnly = true)
public class CustomProjectRepositoryImpl extends QuerydslRepositorySupport implements CustomProjectRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public CustomProjectRepositoryImpl(JPAQueryFactory jpaQueryFactory) { // 4
        super(Project.class);
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    public Page<ProjectReadDto> findAllByCondition(ProjectReadCondition cond) {
        Pageable pageable = PageRequest.of(cond.getPage(), cond.getSize());
        Predicate predicate = createPredicate(cond);
        return new PageImpl<>(fetchAll(predicate, pageable), pageable, fetchCount(predicate));
    }


    private List<ProjectReadDto> fetchAll(Predicate predicate, Pageable pageable) { // 6
        return getQuerydsl().applyPagination(
                pageable,
                jpaQueryFactory
                        .select(constructor(
                                ProjectReadDto.class,
                                project.id,
                                project.projectNumber,
                                project.name,
                                project.carType,
                                item.type,
                                item.name,
                                item.itemNumber,
                                project.revision,

                                project.startPeriod,
                                project.overPeriod,

                                project.tempsave

                        ))
                        .from(project)
                        .join(item).on(project.item.id.eq(item.id))

                        .where(predicate)
                        .orderBy(project.id.desc())
        ).fetch();
    }

    private Long fetchCount(Predicate predicate) { // 7
        return jpaQueryFactory.select(
                        project.count()
                ).from(project).
                where(predicate).fetchOne();
    }

    private Predicate createPredicate(ProjectReadCondition cond) { // 8
        return new BooleanBuilder();
    }


    private <T> Predicate orConditions(List<T> values, Function<T, BooleanExpression> term) { // 11
        return values.stream()
                .map(term)
                .reduce(BooleanExpression::or)
                .orElse(null);
    }
}