package eci.server.ItemModule.dto.color;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
@AllArgsConstructor
public class ColorListDto {
    private Long totalElements;
    private Integer totalPages;
    private boolean hasNext;
    private List<ColorReadResonse> content;

    public static ColorListDto toDto(Page<ColorReadResonse> page) {
        return new ColorListDto(page.getTotalElements(), page.getTotalPages(), page.hasNext(), page.getContent());
    }
}
