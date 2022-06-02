package eci.server.NewItemModule.repository.coatingWay;

import eci.server.NewItemModule.dto.coatingcommon.CoatingDto;
import eci.server.NewItemModule.dto.coatingWay.CoatingWayReadCondition;
import org.springframework.data.domain.Page;

public interface CustomCoatingWayRepository {
    Page<CoatingDto> findAllByCondition(CoatingWayReadCondition cond);
}