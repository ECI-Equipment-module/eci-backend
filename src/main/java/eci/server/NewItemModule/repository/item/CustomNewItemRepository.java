package eci.server.NewItemModule.repository.item;

import eci.server.ItemModule.dto.color.ColorReadCondition;
import eci.server.ItemModule.dto.color.ColorReadResonse;
import eci.server.NewItemModule.dto.newItem.NewItemPagingDto;
import eci.server.NewItemModule.dto.newItem.NewItemReadCondition;
import org.springframework.data.domain.Page;

public interface CustomNewItemRepository {

    Page<NewItemPagingDto> findAllByCondition(NewItemReadCondition cond);
}
