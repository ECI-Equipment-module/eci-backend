package eci.server.NewItemModule.repository.supplier;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import eci.server.NewItemModule.dto.supplier.SupplierReadCondition;
import eci.server.NewItemModule.dto.supplier.SupplierReadResponse;
import eci.server.NewItemModule.entity.supplier.Supplier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.Function;

import static com.querydsl.core.types.Projections.constructor;
import static eci.server.NewItemModule.entity.supplier.QSupplier.supplier;

@Transactional(readOnly = true)
public class CustomSupplierRepositoryImpl extends QuerydslRepositorySupport implements CustomSupplierRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public CustomSupplierRepositoryImpl(JPAQueryFactory jpaQueryFactory) { // 4
        super(Supplier.class);
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    public Page<SupplierReadResponse> findAllByCondition(SupplierReadCondition cond) {
        Pageable pageable = PageRequest.of(cond.getPage(), cond.getSize());
        Predicate predicate = createPredicate(cond);
        return new PageImpl<>(fetchAll(predicate, pageable), pageable, fetchCount(predicate));
    }


    private List<SupplierReadResponse> fetchAll(Predicate predicate, Pageable pageable) { // 6
        return getQuerydsl().applyPagination(
                pageable,
                jpaQueryFactory
                        .select(constructor(
                                SupplierReadResponse.class,
                                supplier.id,
                                supplier.name
                        ))
                        .from(supplier)
                        .where(predicate)
                        .orderBy(supplier.id.desc())
        ).fetch();
    }

    private Long fetchCount(Predicate predicate) { // 7
        return jpaQueryFactory.select(
                        supplier.count()
                ).from(supplier).
                where(predicate).fetchOne();
    }

    private Predicate createPredicate(SupplierReadCondition cond) { // 8
        return new BooleanBuilder();
    }


    private <T> Predicate orConditions(List<T> values, Function<T, BooleanExpression> term) { // 11
        return values.stream()
                .map(term)
                .reduce(BooleanExpression::or)
                .orElse(null);
    }
}
