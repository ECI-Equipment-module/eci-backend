package eci.server.ReleaseModule.repository;

import eci.server.ProjectModule.dto.produceOrg.ProduceOrganizationReadCondition;
import eci.server.ProjectModule.dto.produceOrg.ProduceOrganizationReadResponse;
import org.springframework.data.domain.Page;

public interface CustomReleaseOrganizationRepository {
        Page<ProduceOrganizationReadResponse> findAllByCondition(ProduceOrganizationReadCondition cond);

    }
