package eci.server.ProjectModule.repository.clientOrg;

import eci.server.ProjectModule.dto.clientOrg.ClientOrganizationReadCondition;
import eci.server.ProjectModule.dto.clientOrg.ClientOrganizationReadResponse;
import org.springframework.data.domain.Page;

public interface CustomClientOrganizationRepository {
    Page<ClientOrganizationReadResponse> findAllByCondition(ClientOrganizationReadCondition cond);
}
