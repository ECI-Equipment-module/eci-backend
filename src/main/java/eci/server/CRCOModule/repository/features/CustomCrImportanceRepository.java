package eci.server.CRCOModule.repository.features;

import eci.server.CRCOModule.dto.featureresponse.CrImportanceResponse;
import eci.server.CRCOModule.dto.featurescond.CrImportanceReadCondition;
import org.springframework.data.domain.Page;

public interface CustomCrImportanceRepository {
    Page<CrImportanceResponse> findAllByCondition(CrImportanceReadCondition cond);
}
