package eci.server.NewItemModule.repository.item;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import eci.server.ItemModule.dto.color.ColorReadCondition;

import eci.server.NewItemModule.dto.newItem.NewItemPagingDto;
import eci.server.NewItemModule.dto.newItem.NewItemReadCondition;
import eci.server.NewItemModule.entity.NewItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.Function;

import static com.querydsl.core.types.Projections.constructor;
import static eci.server.NewItemModule.entity.QNewItem.newItem;

    @Transactional(readOnly = true)
    public class CustomNewItemRepositoryImpl extends QuerydslRepositorySupport implements CustomNewItemRepository {

        private final JPAQueryFactory jpaQueryFactory;

        public CustomNewItemRepositoryImpl(JPAQueryFactory jpaQueryFactory) { // 4
            super(NewItem.class);
            this.jpaQueryFactory = jpaQueryFactory;
        }

        @Override
        public Page<NewItemPagingDto> findAllByCondition(NewItemReadCondition cond) {
            Pageable pageable = PageRequest.of(cond.getPage(), cond.getSize());
            Predicate predicate = createPredicate(cond);
            return new PageImpl<>(fetchAll(predicate, pageable), pageable, fetchCount(predicate));
        }


        private List<NewItemPagingDto> fetchAll(Predicate predicate, Pageable pageable) { // 6
            return getQuerydsl().applyPagination(
                    pageable,
                    jpaQueryFactory
                            .select(constructor(
                                    NewItemPagingDto.class,
                                    newItem.id,
                                    newItem.thumbnail,
                                    newItem.itemNumber,
                                    newItem.name,
                                    newItem.itemTypes.itemType,
                                    newItem.classification,
                                    newItem.sharing
                                    ))
                            .from(newItem)
                            .where(predicate)
                            .orderBy(newItem.id.desc())
            ).fetch();
        }

        private Long fetchCount(Predicate predicate) { // 7
            return jpaQueryFactory.select(
                            newItem.count()
                    ).from(newItem).
                    where(predicate).fetchOne();
        }

        private Predicate createPredicate(NewItemReadCondition cond) { // 8
            return new BooleanBuilder();
        }


        private <T> Predicate orConditions(List<T> values, Function<T, BooleanExpression> term) { // 11
            return values.stream()
                    .map(term)
                    .reduce(BooleanExpression::or)
                    .orElse(null);
        }
    }



