package eci.server.NewItemModule.repository.coatingWay;

import eci.server.NewItemModule.dto.coatingcommon.CoatingReadResponse;
import eci.server.NewItemModule.dto.coatingWay.CoatingWayReadCondition;
import org.springframework.data.domain.Page;

public interface CustomCoatingWayRepository {
    Page<CoatingReadResponse> findAllByCondition(CoatingWayReadCondition cond);
}