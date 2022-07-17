package eci.server.ReleaseModule.service;

import eci.server.ProjectModule.dto.produceOrg.ProduceOrganizationReadCondition;
import eci.server.ReleaseModule.dto.releaseOrg.ReleaseOrgListDto;
import eci.server.ReleaseModule.repository.ReleaseOrganizationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReleaseOrgService {

    private final ReleaseOrganizationRepository releaseOrganizationRepository;

    public ReleaseOrgListDto readAll(ProduceOrganizationReadCondition cond) {
        return ReleaseOrgListDto.toDto(
                releaseOrganizationRepository.findAllByCondition(cond)
        );
    }

}

