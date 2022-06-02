package eci.server.NewItemModule.service.supplier;

import eci.server.NewItemModule.dto.supplier.SupplierListDto;
import eci.server.NewItemModule.dto.supplier.SupplierReadCondition;
import eci.server.NewItemModule.repository.supplier.SupplierRepository;import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SupplierService {

    private final SupplierRepository supplierRepository;

    public SupplierListDto readAll(SupplierReadCondition cond) {
        return SupplierListDto.toDto(
                supplierRepository.findAllByCondition(cond)
        );
    }

}
