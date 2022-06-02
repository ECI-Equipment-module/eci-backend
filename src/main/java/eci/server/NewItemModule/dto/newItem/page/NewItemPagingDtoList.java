package eci.server.NewItemModule.dto.newItem.page;

import eci.server.NewItemModule.dto.newItem.NewItemPagingDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
@AllArgsConstructor
public class  NewItemPagingDtoList {
    private Long totalElements;
    private Integer totalPages;
    private boolean hasNext;
    private List<NewItemPagingDto> content;

    public static NewItemPagingDtoList toDto(Page<NewItemPagingDto> page) {
        return new NewItemPagingDtoList(page.getTotalElements(), page.getTotalPages(), page.hasNext(), page.getContent());
    }
}

