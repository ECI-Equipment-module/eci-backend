package eci.server.ProjectModule.dto;

import eci.server.ItemModule.dto.item.ItemSimpleDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
@AllArgsConstructor
public class ProjectListDto{
        private Long totalElements;
    private Integer totalPages;
    private boolean hasNext;
    private List<ItemSimpleDto> postList;

    public static eci.server.ItemModule.dto.item.ItemListDto toDto(Page<ItemSimpleDto> page) {
        return new eci.server.ItemModule.dto.item.ItemListDto(page.getTotalElements(), page.getTotalPages(), page.hasNext(), page.getContent());
    }
}
