package eci.server.CRCOModule.service.features;

import eci.server.CRCOModule.dto.featurescond.CrImportanceReadCondition;
import eci.server.CRCOModule.dto.featuresdtos.CrImportanceListDto;
import eci.server.CRCOModule.repository.features.CrImportanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CrImportanceService {

    private final CrImportanceRepository crImportanceRepository;

    public CrImportanceListDto readAll(CrImportanceReadCondition cond) {
        return CrImportanceListDto.toDto(
                crImportanceRepository.findAllByCondition(cond)
        );

    }

}
