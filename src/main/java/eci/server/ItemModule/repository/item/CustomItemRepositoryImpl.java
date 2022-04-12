package eci.server.ItemModule.repository.item;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import eci.server.ItemModule.dto.item.ItemReadCondition;
import eci.server.ItemModule.dto.item.ItemSimpleDto;
import eci.server.ItemModule.entity.item.Item;
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

/**
 * CustomItemRepository의 구현체
 */
@Transactional(readOnly = true) // 1
public class CustomItemRepositoryImpl extends QuerydslRepositorySupport implements CustomItemRepository { // 2

    private final JPAQueryFactory jpaQueryFactory; // 3

    public CustomItemRepositoryImpl(JPAQueryFactory jpaQueryFactory) { // 4
        super(Item.class);
        this.jpaQueryFactory = jpaQueryFactory;
    }

    /**
     *  전달받은 ItemReadCondition(검색)으로
     *  Predicate와 PageRequest를 생성 &
     *  조회 쿼리와 카운트 쿼리를 수행한 결과를 Page의 구현체로 반환
     * @param cond
     * @return Page
     */
    @Override
    public Page<ItemSimpleDto> findAllByCondition(ItemReadCondition cond) { // 5
        Pageable pageable = PageRequest.of(cond.getPage(), cond.getSize());
        Predicate predicate = createPredicate(cond);
        System.out.println("customitemrepository impleeeeeeeeeeeeeeeeeeeeee");
        System.out.println(fetchAll(predicate, pageable).toString());
        return new PageImpl<>(fetchAll(predicate, pageable), pageable, fetchCount(predicate));
    }

    /**
     * 아이템 목록을 ItemSimpleDto로 조회한 결과 반환환
     * @param predicate
     * @param pageable
     * @return getQuerydsl().applyPagination (페이징 적용 쿼리)
     */
    private List<ItemSimpleDto> fetchAll(Predicate predicate, Pageable pageable) { // 6
        List<ItemSimpleDto> itemSimpleDtos = getQuerydsl().applyPagination(
                pageable,
                jpaQueryFactory
                        .select(constructor
                                (ItemSimpleDto.class,
                                        item.id,
                                        item.name,
                                        item.type,
                                        item.width,
                                        item.height,
                                        item.weight,
                                        item.member.username,
                                        item.createdAt
//                                        item.thumbnail
                                        )
                        )
                        .from(item)
                        .join(item.member)
                        .where(predicate)
                        .orderBy(item.id.desc())
        ).fetch();

        return itemSimpleDtos;
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