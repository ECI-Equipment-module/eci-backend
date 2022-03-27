package eci.server.repository.item;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import eci.server.dto.item.ItemReadCondition;
import eci.server.dto.item.ItemSimpleDto;
import eci.server.entity.item.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.Function;

import static com.querydsl.core.types.Projections.constructor;
import static eci.server.entity.item.QItem.item;

@Transactional(readOnly = true) // 1
public class CustomItemRepositoryImpl extends QuerydslRepositorySupport implements CustomItemRepository { // 2

    private final JPAQueryFactory jpaQueryFactory; // 3

    public CustomItemRepositoryImpl(JPAQueryFactory jpaQueryFactory) { // 4
        super(Item.class);
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    public Page<ItemSimpleDto> findAllByCondition(ItemReadCondition cond) { // 5
        Pageable pageable = PageRequest.of(cond.getPage(), cond.getSize());
        Predicate predicate = createPredicate(cond);
        return new PageImpl<>(fetchAll(predicate, pageable), pageable, fetchCount(predicate));
    }

    private List<ItemSimpleDto> fetchAll(Predicate predicate, Pageable pageable) { // 6
        return getQuerydsl().applyPagination(
                pageable,
                jpaQueryFactory
                        .select(constructor(ItemSimpleDto.class,item.id, item.name, item.type, item.width, item.height, item.weight, item.member.username,item.thumbnail, item.createdAt))
                        .from(item)
                        .join(item.member)
                        .where(predicate)
                        .orderBy(item.id.desc())
        ).fetch();
    }

    private Long fetchCount(Predicate predicate) { // 7
        return jpaQueryFactory.select(item.count()).from(item).where(predicate).fetchOne();
    }

    private Predicate createPredicate(ItemReadCondition cond) { // 8
        return new BooleanBuilder()
                .and(orConditionsByEqMemberIds(cond.getMemberId()));
    }

    private Predicate orConditionsByEqMemberIds(List<Long> memberIds) { // 10
        return orConditions(memberIds, item.member.id::eq);
    }

    private <T> Predicate orConditions(List<T> values, Function<T, BooleanExpression> term) { // 11
        return values.stream()
                .map(term)
                .reduce(BooleanExpression::or)
                .orElse(null);
    }
}