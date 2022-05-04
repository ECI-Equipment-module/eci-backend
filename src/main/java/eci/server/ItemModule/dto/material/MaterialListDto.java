package eci.server.ItemModule.dto.material;

import lombok.AllArgsConstructor;

import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
@AllArgsConstructor
public class MaterialListDto {
    private Long totalElements;
    private Integer totalPages;
    private boolean hasNext;
    private List<MaterialSimpleDto> content;

    public static MaterialListDto toDto(Page<MaterialSimpleDto> page) {
        return new MaterialListDto(page.getTotalElements(), page.getTotalPages(), page.hasNext(), page.getContent());
    }
}