package eci.server.CRCOModule.repository.features;

import eci.server.CRCOModule.dto.featureresponse.CrSourceReadResponse;
import eci.server.CRCOModule.dto.featurescond.CrSourceReadCondition;
import org.springframework.data.domain.Page;

public interface CustomCrSourceRepository {
    Page<CrSourceReadResponse> findAllByCondition(CrSourceReadCondition cond);
}
