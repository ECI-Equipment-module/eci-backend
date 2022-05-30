package eci.server.NewItemModule.repository.maker;

import eci.server.NewItemModule.dto.supplier.SupplierReadCondition;
import eci.server.NewItemModule.dto.supplier.SupplierReadResponse;
import org.springframework.data.domain.Page;

public interface CustomMakerRepository {
    Page<SupplierReadResponse> findAllByCondition(SupplierReadCondition cond);
}
