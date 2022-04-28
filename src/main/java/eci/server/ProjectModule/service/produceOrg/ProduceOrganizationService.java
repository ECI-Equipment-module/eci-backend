package eci.server.ProjectModule.service.produceOrg;

import eci.server.ProjectModule.dto.produceOrg.ProduceOrganizationListDto;
import eci.server.ProjectModule.dto.produceOrg.ProduceOrganizationReadCondition;
import eci.server.ProjectModule.repository.produceOrg.ProduceOrganizationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProduceOrganizationService {

    private final ProduceOrganizationRepository produceOrganizationRepository;

    public ProduceOrganizationListDto readAll(ProduceOrganizationReadCondition cond) {
        return ProduceOrganizationListDto.toDto(
                produceOrganizationRepository.findAllByCondition(cond)
        );
    }

}
