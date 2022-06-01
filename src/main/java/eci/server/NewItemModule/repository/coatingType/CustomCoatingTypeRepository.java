package eci.server.NewItemModule.repository.coatingType;

import eci.server.NewItemModule.dto.coatingcommon.CoatingDto;
import eci.server.NewItemModule.dto.coatingType.CoatingTypeReadCondition;
import org.springframework.data.domain.Page;

public interface CustomCoatingTypeRepository {
    Page<CoatingDto> findAllByCondition(CoatingTypeReadCondition cond);
}