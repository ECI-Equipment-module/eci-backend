package eci.server.NewItemModule.repository.maker;

import eci.server.NewItemModule.dto.maker.CustomMakerResponse;
import eci.server.NewItemModule.dto.maker.MakerReadCondition;
import org.springframework.data.domain.Page;

public interface CustomMakerRepository {
    Page<CustomMakerResponse> findAllByCondition(MakerReadCondition cond);
}
