package eci.server.ItemModule.repository.manufacture;

import eci.server.ItemModule.dto.manufacture.ManufactureReadCondition;
import eci.server.ItemModule.dto.manufacture.ManufactureSimpleDto;
import org.springframework.data.domain.Page;

public interface CustomManufactureRepository {
    Page<ManufactureSimpleDto> findAllByCondition(ManufactureReadCondition cond);
}
