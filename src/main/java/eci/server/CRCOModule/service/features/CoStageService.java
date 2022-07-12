package eci.server.CRCOModule.service.features;

import eci.server.CRCOModule.dto.featurescond.CoStageReadCondition;
import eci.server.CRCOModule.dto.featuresdtos.CoStageListDto;
import eci.server.CRCOModule.repository.cofeature.CoStageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CoStageService {

    private final CoStageRepository CoStageRepository;

    public CoStageListDto readAll(CoStageReadCondition cond) {
        return CoStageListDto.toDto(
                CoStageRepository.findAllByCondition(cond)
        );
    }

}
