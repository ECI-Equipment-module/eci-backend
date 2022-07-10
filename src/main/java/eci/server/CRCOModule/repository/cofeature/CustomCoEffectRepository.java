package eci.server.CRCOModule.repository.cofeature;

import eci.server.CRCOModule.dto.featureresponse.CoEffectReadResponse;
import eci.server.CRCOModule.dto.featurescond.CoEffectReadCondition;
import org.springframework.data.domain.Page;

public interface CustomCoEffectRepository {
    Page<CoEffectReadResponse> findAllByCondition(CoEffectReadCondition cond);
}
