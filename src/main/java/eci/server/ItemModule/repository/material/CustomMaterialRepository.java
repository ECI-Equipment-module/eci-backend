package eci.server.ItemModule.repository.material;

import eci.server.ItemModule.dto.material.MaterialReadCondition;
import eci.server.ItemModule.dto.material.MaterialSimpleDto;
import org.springframework.data.domain.Page;

public interface CustomMaterialRepository {
    Page<MaterialSimpleDto> findAllByCondition(MaterialReadCondition cond);
}
