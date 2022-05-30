package eci.server.NewItemModule.repository.item;

import eci.server.NewItemModule.dto.NewItemDto;
import eci.server.NewItemModule.dto.item.NewItemReadCondition;
import org.springframework.data.domain.Page;

public interface CustomNewItemRepository {
    Page<NewItemDto> findAllByCondition(NewItemReadCondition cond);

}
