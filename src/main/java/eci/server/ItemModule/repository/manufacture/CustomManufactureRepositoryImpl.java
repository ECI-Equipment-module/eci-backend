package eci.server.ItemModule.repository.manufacture;


import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import eci.server.ItemModule.dto.manufacture.ManufactureReadCondition;
import eci.server.ItemModule.dto.manufacture.ManufactureSimpleDto;
import eci.server.ItemModule.entity.item.Manufacture;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.Function;

import static com.querydsl.core.types.Projections.constructor;
import static eci.server.ItemModule.entity.item.QManufacture.manufacture;

@Transactional(readOnly = true)
public class CustomManufactureRepositoryImpl extends QuerydslRepositorySupport implements CustomManufactureRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public CustomManufactureRepositoryImpl(JPAQueryFactory jpaQueryFactory) {
        super(Manufacture.class);
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    public Page<ManufactureSimpleDto> findAllByCondition(ManufactureReadCondition cond) {
        Pageable pageable = PageRequest.of(cond.getPage(), cond.getSize());
        Predicate predicate = createPredicate(cond);
        return new PageImpl<>(fetchAll(predicate, pageable), pageable, fetchCount(predicate));
    }


    private List<ManufactureSimpleDto> fetchAll(Predicate predicate, Pageable pageable) {
        return getQuerydsl().applyPagination(
                pageable,
                jpaQueryFactory
                        .select(constructor(
                                ManufactureSimpleDto.class,
                                manufacture.id,
                                manufacture.code,
                                manufacture.name
                        ))
                        .from(manufacture)
                        .where(predicate)
                        .orderBy(manufacture.id.desc())
        ).fetch();
    }

    private Long fetchCount(Predicate predicate) {
        return jpaQueryFactory.select(
                        manufacture.count()
                ).from(manufacture).
                where(predicate).fetchOne();
    }

    private Predicate createPredicate(ManufactureReadCondition cond) { // 8
        return new BooleanBuilder();
    }


    private <T> Predicate orConditions(List<T> values, Function<T, BooleanExpression> term) { // 11
        return values.stream()
                .map(term)
                .reduce(BooleanExpression::or)
                .orElse(null);
    }
}
