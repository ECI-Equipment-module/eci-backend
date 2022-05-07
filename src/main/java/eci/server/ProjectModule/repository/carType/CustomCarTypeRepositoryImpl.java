package eci.server.ProjectModule.repository.carType;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import eci.server.ProjectModule.dto.carType.CarTypeReadCondition;
import eci.server.ProjectModule.dto.carType.CarTypeReadResponse;
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
import static eci.server.ProjectModule.entity.project.QCarType.carType;

@Transactional(readOnly = true)
public class CustomCarTypeRepositoryImpl extends QuerydslRepositorySupport implements CustomCarTypeRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public CustomCarTypeRepositoryImpl(JPAQueryFactory jpaQueryFactory) { // 4
        super(ProjectType.class);
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    public Page<CarTypeReadResponse> findAllByCondition(CarTypeReadCondition cond) {
        Pageable pageable = PageRequest.of(cond.getPage(), cond.getSize());
        Predicate predicate = createPredicate(cond);
        return new PageImpl<>(fetchAll(predicate, pageable), pageable, fetchCount(predicate));
    }


    private List<CarTypeReadResponse> fetchAll(Predicate predicate, Pageable pageable) { // 6
        return getQuerydsl().applyPagination(
                pageable,
                jpaQueryFactory
                        .select(constructor(
                                CarTypeReadResponse.class,
                                carType.id,
                                carType.name
                        ))
                        .from(carType)
                        .where(predicate)
                        .orderBy(carType.id.desc())
        ).fetch();
    }

    private Long fetchCount(Predicate predicate) { // 7
        return jpaQueryFactory.select(
                        carType.count()
                ).from(carType).
                where(predicate).fetchOne();
    }

    private Predicate createPredicate(CarTypeReadCondition cond) { // 8
        return new BooleanBuilder();
    }


    private <T> Predicate orConditions(List<T> values, Function<T, BooleanExpression> term) { // 11
        return values.stream()
                .map(term)
                .reduce(BooleanExpression::or)
                .orElse(null);
    }
}
