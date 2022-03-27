package eci.server.repository.item;

import eci.server.dto.item.ItemReadCondition;
import eci.server.dto.item.ItemSimpleDto;
import org.springframework.data.domain.Page;

/**
 * 검색 조건에 대한 정보가 담긴
 * ItemReadCondition 전달
 * Page로 반환하여
 * 페이징 결과에 대한 각종 정보 확인
 */
public interface CustomItemRepository {
    Page<ItemSimpleDto> findAllByCondition(ItemReadCondition cond);
}