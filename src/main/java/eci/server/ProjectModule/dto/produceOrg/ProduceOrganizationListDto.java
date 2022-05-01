package eci.server.ProjectModule.dto.produceOrg;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
@AllArgsConstructor
public class ProduceOrganizationListDto {
    private Long totalElements;
    private Integer totalPages;
    private boolean hasNext;
    private List<ProduceOrganizationReadResponse> postList;

    public static ProduceOrganizationListDto toDto(Page<ProduceOrganizationReadResponse> page) {
        return new ProduceOrganizationListDto(page.getTotalElements(), page.getTotalPages(), page.hasNext(), page.getContent());
    }
}
