package eci.server.ItemModule.repository.manufacture;


import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import eci.server.ItemModule.dto.manufacture.MakerReadCondition;
import eci.server.ItemModule.dto.manufacture.MakerSimpleDto;
import eci.server.NewItemModule.entity.supplier.Maker;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.Function;

import static com.querydsl.core.types.Projections.constructor;
import static eci.server.NewItemModule.entity.supplier.QMaker.maker;

@Transactional(readOnly = true)
public class CustomMakerRepositoryImpl extends QuerydslRepositorySupport implements CustomMakerRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public CustomMakerRepositoryImpl(JPAQueryFactory jpaQueryFactory) {
        super(Maker.class);
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    public Page<MakerSimpleDto> findAllByCondition(MakerReadCondition cond) {
        Pageable pageable = PageRequest.of(cond.getPage(), cond.getSize());
        Predicate predicate = createPredicate(cond);
        return new PageImpl<>(fetchAll(predicate, pageable), pageable, fetchCount(predicate));
    }


    private List<MakerSimpleDto> fetchAll(Predicate predicate, Pageable pageable) {
        return getQuerydsl().applyPagination(
                pageable,
                jpaQueryFactory
                        .select(constructor(
                                MakerSimpleDto.class,
                                maker.id,
                                maker.code,
                                maker.name
                        ))
                        .from(maker)
                        .where(predicate)
                        .orderBy(maker.id.desc())
        ).fetch();
    }

    private Long fetchCount(Predicate predicate) {
        return jpaQueryFactory.select(
                        maker.count()
                ).from(maker).
                where(predicate).fetchOne();
    }

    private Predicate createPredicate(MakerReadCondition cond) { // 8
        return new BooleanBuilder();
    }


    private <T> Predicate orConditions(List<T> values, Function<T, BooleanExpression> term) { // 11
        return values.stream()
                .map(term)
                .reduce(BooleanExpression::or)
                .orElse(null);
    }
}
