package eci.server.NewItemModule.repository.supplier;

import eci.server.NewItemModule.dto.supplier.SupplierReadCondition;
import eci.server.NewItemModule.dto.supplier.SupplierReadResponse;
import org.springframework.data.domain.Page;

public interface CustomSupplierRepository {
    Page<SupplierReadResponse> findAllByCondition(SupplierReadCondition cond);
}
