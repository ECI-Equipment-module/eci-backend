package eci.server.ItemModule.repository.manufacture;

import eci.server.ItemModule.dto.manufacture.MakerReadCondition;
import eci.server.ItemModule.dto.manufacture.MakerSimpleDto;
import org.springframework.data.domain.Page;

public interface CustomMakerRepository {
    Page<MakerSimpleDto> findAllByCondition(MakerReadCondition cond);
}
