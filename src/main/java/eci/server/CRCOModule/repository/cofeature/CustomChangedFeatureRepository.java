package eci.server.CRCOModule.repository.cofeature;

import eci.server.CRCOModule.dto.featureresponse.ChangedFeatureReadResponse;
import eci.server.CRCOModule.dto.featurescond.ChangedFeatureReadCondition;
import org.springframework.data.domain.Page;

public interface CustomChangedFeatureRepository {
    Page<ChangedFeatureReadResponse> findAllByCondition(ChangedFeatureReadCondition cond);
}
