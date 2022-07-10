package eci.server.CRCOModule.repository.cofeature;

import eci.server.CRCOModule.dto.featureresponse.CoStageReadResponse;
import eci.server.CRCOModule.dto.featurescond.CoStageReadCondition;
import org.springframework.data.domain.Page;

public interface CustomCoStageRepository {
    Page<CoStageReadResponse> findAllByCondition(CoStageReadCondition cond);
}

