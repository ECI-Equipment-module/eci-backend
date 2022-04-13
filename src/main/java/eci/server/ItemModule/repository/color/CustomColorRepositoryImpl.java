package eci.server.ItemModule.repository.color;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import eci.server.ItemModule.dto.color.ColorReadCondition;
import eci.server.ItemModule.dto.color.ColorReadResonse;
import eci.server.ItemModule.entity.item.Color;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import static com.querydsl.core.types.Projections.constructor;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.Function;

import static eci.server.ItemModule.entity.item.QColor.color1;

@Transactional(readOnly = true)
public class CustomColorRepositoryImpl extends QuerydslRepositorySupport implements CustomColorRepository{

    private final JPAQueryFactory jpaQueryFactory;

    public CustomColorRepositoryImpl(JPAQueryFactory jpaQueryFactory) { // 4
        super(Color.class);
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    public Page<ColorReadResonse> findAllByCondition(ColorReadCondition cond) {
        Pageable pageable = PageRequest.of(cond.getPage(), cond.getSize());
        Predicate predicate = createPredicate(cond);
        return new PageImpl<>(fetchAll(predicate, pageable), pageable, fetchCount(predicate));
    }


    private List<ColorReadResonse> fetchAll(Predicate predicate, Pageable pageable) { // 6
        return getQuerydsl().applyPagination(
                pageable,
                jpaQueryFactory
                        .select(constructor(
                                ColorReadResonse.class,
                                color1.id,
                                color1.code,
                                color1.color))
                        .from(color1)
                        .where(predicate)
                        .orderBy(color1.id.desc())
        ).fetch();
    }

    private Long fetchCount(Predicate predicate) { // 7
        return jpaQueryFactory.select(
                color1.count()
        ).from(color1).
                where(predicate).fetchOne();
    }

    private Predicate createPredicate(ColorReadCondition cond) { // 8
        return new BooleanBuilder();
    }


    private <T> Predicate orConditions(List<T> values, Function<T, BooleanExpression> term) { // 11
        return values.stream()
                .map(term)
                .reduce(BooleanExpression::or)
                .orElse(null);
    }
}
