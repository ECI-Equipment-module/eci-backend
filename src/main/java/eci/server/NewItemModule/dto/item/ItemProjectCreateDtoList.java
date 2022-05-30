package eci.server.NewItemModule.dto.item;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;
@Data
@AllArgsConstructor
public class ItemProjectCreateDtoList {
    private Long totalElements;
    private Integer totalPages;
    private boolean hasNext;
    private List<ItemProjectCreateDto> content;

    public static ItemProjectCreateDtoList toDto(Page<ItemProjectCreateDto> page) {
        return new ItemProjectCreateDtoList(page.getTotalElements(), page.getTotalPages(), page.hasNext(), page.getContent());
    }
}
