package eci.server.ItemModule.dto.item;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
@AllArgsConstructor
public class ItemListDto {
    private Long totalElements;
    private Integer totalPages;
    private boolean hasNext;
    private List<ItemSimpleDto> postList;

    public static ItemListDto toDto(Page<ItemSimpleDto> page) {
        return new ItemListDto(page.getTotalElements(), page.getTotalPages(), page.hasNext(), page.getContent());
    }
}