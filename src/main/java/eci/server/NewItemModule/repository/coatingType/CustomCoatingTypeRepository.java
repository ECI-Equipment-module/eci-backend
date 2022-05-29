package eci.server.NewItemModule.repository.coatingType;

import eci.server.NewItemModule.dto.coatingcommon.CoatingReadResponse;
import eci.server.NewItemModule.dto.coatingType.CoatingTypeReadCondition;
import org.springframework.data.domain.Page;

public interface CustomCoatingTypeRepository {
    Page<CoatingReadResponse> findAllByCondition(CoatingTypeReadCondition cond);
}