package eci.server.ProjectModule.repository.carType;

import eci.server.ProjectModule.dto.carType.CarTypeReadCondition;
import eci.server.ProjectModule.dto.carType.CarTypeReadResponse;
import org.springframework.data.domain.Page;

public interface CustomCarTypeRepository {
    Page<CarTypeReadResponse> findAllByCondition(CarTypeReadCondition cond);
}


