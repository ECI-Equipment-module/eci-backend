package eci.server.ProjectModule.repository.produceOrg;

import eci.server.ProjectModule.dto.produceOrg.ProduceOrganizationReadCondition;
import eci.server.ProjectModule.dto.produceOrg.ProduceOrganizationReadResponse;
import org.springframework.data.domain.Page;

public interface CustomProduceOrganizationRepository {
    Page<ProduceOrganizationReadResponse> findAllByCondition(ProduceOrganizationReadCondition cond);
}