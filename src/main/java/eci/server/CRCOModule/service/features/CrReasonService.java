package eci.server.CRCOModule.service.features;

import eci.server.CRCOModule.dto.featurescond.CrReasonReadCondition;
import eci.server.CRCOModule.dto.featuresdtos.CrReasonListDto;
import eci.server.CRCOModule.repository.features.CrReasonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CrReasonService {

    private final CrReasonRepository crReasonRepository;

        public CrReasonListDto readAll(CrReasonReadCondition cond) {
            return CrReasonListDto.toDto(
                    crReasonRepository.findAllByCondition(cond)
            );

    }

}

