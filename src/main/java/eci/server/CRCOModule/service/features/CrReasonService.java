package eci.server.CRCOModule.service.features;

import eci.server.CRCOModule.dto.CrCreateRequest;
import eci.server.CRCOModule.dto.featurescond.CrReasonReadCondition;
import eci.server.CRCOModule.dto.featuresdtos.CrReasonListDto;
import eci.server.CRCOModule.entity.ChangeRequest;
import eci.server.CRCOModule.entity.features.CrReason;
import eci.server.CRCOModule.repository.cr.ChangeRequestRepository;
import eci.server.CRCOModule.repository.features.CrImportanceRepository;
import eci.server.CRCOModule.repository.features.CrReasonRepository;
import eci.server.CRCOModule.repository.features.CrSourceRepository;
import eci.server.NewItemModule.dto.newItem.create.NewItemCreateResponse;
import eci.server.ProjectModule.dto.clientOrg.ClientOrganizationListDto;
import eci.server.ProjectModule.dto.clientOrg.ClientOrganizationReadCondition;
import eci.server.ProjectModule.repository.clientOrg.ClientOrganizationRepository;
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

