package eci.server.NewItemModule.dto.supplier;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
@AllArgsConstructor
public class SupplierListDto {
    private Long totalElements;
    private Integer totalPages;
    private boolean hasNext;
    private List<SupplierReadResponse> content;

    public static SupplierListDto toDto(Page<SupplierReadResponse> page) {
        return new SupplierListDto(page.getTotalElements(), page.getTotalPages(), page.hasNext(), page.getContent());
    }
}

