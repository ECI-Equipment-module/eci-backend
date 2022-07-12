package eci.server.CRCOModule.service.features;

import eci.server.CRCOModule.dto.featurescond.ChangedFeatureReadCondition;
import eci.server.CRCOModule.dto.featuresdtos.ChangedFeatureListDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChangedFeatureService {

    private final eci.server.CRCOModule.repository.cofeature.ChangedFeatureRepository ChangedFeatureRepository;

    public ChangedFeatureListDto readAll(ChangedFeatureReadCondition cond) {
        return ChangedFeatureListDto.toDto(
                ChangedFeatureRepository.findAllByCondition(cond)
        );
    }

}

