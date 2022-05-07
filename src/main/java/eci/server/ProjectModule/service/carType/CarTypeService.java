package eci.server.ProjectModule.service.carType;

import eci.server.ProjectModule.dto.carType.CarTypeListDto;
import eci.server.ProjectModule.dto.carType.CarTypeReadCondition;
import eci.server.ProjectModule.dto.clientOrg.ClientOrganizationListDto;
import eci.server.ProjectModule.dto.clientOrg.ClientOrganizationReadCondition;
import eci.server.ProjectModule.repository.carType.CarTypeRepository;
import eci.server.ProjectModule.repository.clientOrg.ClientOrganizationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CarTypeService {

    private final CarTypeRepository carTypeRepository;

    public CarTypeListDto readAll(CarTypeReadCondition cond) {
        return CarTypeListDto.toDto(
                carTypeRepository.findAllByCondition(cond)
        );
    }

}
