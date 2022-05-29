package eci.server.NewItemModule.dto.coatingcommon;

import eci.server.ItemModule.dto.color.ColorReadResonse;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
@AllArgsConstructor
public class CoatingListDto {
    private Long totalElements;
    private Integer totalPages;
    private boolean hasNext;
    private List<CoatingReadResponse> content;

    public static CoatingListDto toDto(Page<CoatingReadResponse> page) {
        return new CoatingListDto(page.getTotalElements(), page.getTotalPages(), page.hasNext(), page.getContent());
    }
}
