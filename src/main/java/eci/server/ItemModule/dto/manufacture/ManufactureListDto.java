package eci.server.ItemModule.dto.manufacture;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;
@Data
@AllArgsConstructor
public class ManufactureListDto {
    private Long totalElements;
    private Integer totalPages;
    private boolean hasNext;
    private List<ManufactureSimpleDto> postList;

    public static ManufactureListDto toDto(Page<ManufactureSimpleDto> page) {
        return new ManufactureListDto(page.getTotalElements(), page.getTotalPages(), page.hasNext(), page.getContent());
    }
}
