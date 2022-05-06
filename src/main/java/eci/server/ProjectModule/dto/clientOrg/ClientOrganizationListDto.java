package eci.server.ProjectModule.dto.clientOrg;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
@AllArgsConstructor
    public class ClientOrganizationListDto {
        private Long totalElements;
        private Integer totalPages;
        private boolean hasNext;
        private List<ClientOrganizationReadResponse> content;

        public static ClientOrganizationListDto toDto(Page<ClientOrganizationReadResponse> page) {
            return new ClientOrganizationListDto(page.getTotalElements(), page.getTotalPages(), page.hasNext(), page.getContent());
        }
    }
