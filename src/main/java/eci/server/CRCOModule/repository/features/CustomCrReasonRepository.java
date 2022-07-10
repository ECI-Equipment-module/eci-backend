package eci.server.CRCOModule.repository.features;

import eci.server.CRCOModule.dto.featureresponse.CrReasonReadResponse;
import eci.server.CRCOModule.dto.featurescond.CrReasonReadCondition;
import org.springframework.data.domain.Page;


public interface CustomCrReasonRepository {
    Page<CrReasonReadResponse> findAllByCondition(CrReasonReadCondition cond);
}

