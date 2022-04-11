package eci.server.ItemModule.repository.material;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import eci.server.ItemModule.dto.material.MaterialReadCondition;
import eci.server.ItemModule.dto.material.MaterialSimpleDto;
import eci.server.ItemModule.entity.material.Material;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.Function;

import static com.querydsl.core.types.Projections.constructor;
import static eci.server.ItemModule.entity.material.QMaterial.material;


@Transactional(readOnly = true)
public class CustomMaterialRepositoryImpl extends QuerydslRepositorySupport implements CustomMaterialRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public CustomMaterialRepositoryImpl(JPAQueryFactory jpaQueryFactory) { // 4
        super(Material.class);
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    public Page<MaterialSimpleDto> findAllByCondition(MaterialReadCondition cond) {
        Pageable pageable = PageRequest.of(cond.getPage(), cond.getSize());
        Predicate predicate = createPredicate(cond);
        return new PageImpl<>(fetchAll(predicate, pageable), pageable, fetchCount(predicate));
    }


    private List<MaterialSimpleDto> fetchAll(Predicate predicate, Pageable pageable) { // 6
        return getQuerydsl().applyPagination(
                pageable,
                jpaQueryFactory
                        .select(constructor(
                                MaterialSimpleDto.class,
                                material.id,
                                material.code,
                                material.name
                        ))
                        .from(material)
                        .where(predicate)
                        .orderBy(material.id.desc())
        ).fetch();
    }

    private Long fetchCount(Predicate predicate) { // 7
        return jpaQueryFactory.select(
                        material.count()
                ).from(material).
                where(predicate).fetchOne();
    }

    private Predicate createPredicate(MaterialReadCondition cond) { // 8
        return new BooleanBuilder();
    }


    private <T> Predicate orConditions(List<T> values, Function<T, BooleanExpression> term) { // 11
        return values.stream()
                .map(term)
                .reduce(BooleanExpression::or)
                .orElse(null);
    }
}
