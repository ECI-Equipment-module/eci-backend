package eci.server.ProjectModule.dto.carType;

import eci.server.ProjectModule.dto.clientOrg.ClientOrganizationReadResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
@AllArgsConstructor
public class CarTypeListDto {
    private Long totalElements;
    private Integer totalPages;
    private boolean hasNext;
    private List<CarTypeReadResponse> content;

    public static CarTypeListDto toDto(Page<CarTypeReadResponse> page) {
        return new CarTypeListDto(page.getTotalElements(), page.getTotalPages(), page.hasNext(), page.getContent());
    }
}
