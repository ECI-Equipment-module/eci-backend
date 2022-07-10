package eci.server.CRCOModule.service.features;

import eci.server.CRCOModule.dto.featurescond.CoEffectReadCondition;
import eci.server.CRCOModule.dto.featuresdtos.CoEffectListDto;
import eci.server.CRCOModule.repository.cofeature.CoEffectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CoEffectService {

    private final CoEffectRepository CoEffectRepository;

    public CoEffectListDto readAll(CoEffectReadCondition cond) {
        return CoEffectListDto.toDto(
                CoEffectRepository.findAllByCondition(cond)
        );
    }

}
