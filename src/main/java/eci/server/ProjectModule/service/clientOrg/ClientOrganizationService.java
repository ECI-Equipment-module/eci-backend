package eci.server.ProjectModule.service.clientOrg;

import eci.server.ProjectModule.dto.clientOrg.ClientOrganizationListDto;
import eci.server.ProjectModule.dto.clientOrg.ClientOrganizationReadCondition;
import eci.server.ProjectModule.repository.clientOrg.ClientOrganizationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

    @Service
    @Transactional(readOnly = true)
    @RequiredArgsConstructor
    public class ClientOrganizationService {

        private final ClientOrganizationRepository clientOrganizationRepository;

        public ClientOrganizationListDto readAll(ClientOrganizationReadCondition cond) {
            return ClientOrganizationListDto.toDto(
                    clientOrganizationRepository.findAllByCondition(cond)
            );
        }

    }
