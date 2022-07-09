package eci.server.CRCOModule.service.features;

import eci.server.CRCOModule.dto.featurescond.CrSourceReadCondition;
import eci.server.CRCOModule.dto.featuresdtos.CrSourceListDto;
import eci.server.CRCOModule.repository.features.CrSourceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CrSourceService {

    private final CrSourceRepository crSourceRepository;

    public CrSourceListDto readAll(CrSourceReadCondition cond) {
        return CrSourceListDto.toDto(
                crSourceRepository.findAllByCondition(cond)
        );

    }

}

