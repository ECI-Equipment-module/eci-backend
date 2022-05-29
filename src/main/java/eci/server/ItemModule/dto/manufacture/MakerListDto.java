package eci.server.ItemModule.dto.manufacture;


import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;
@Data
@AllArgsConstructor
public class MakerListDto {
    private Long totalElements;
    private Integer totalPages;
    private boolean hasNext;
    private List<MakerSimpleDto> content;

    public static MakerListDto toDto(Page<MakerSimpleDto> page) {
        return new MakerListDto(page.getTotalElements(), page.getTotalPages(), page.hasNext(), page.getContent());
    }
}
