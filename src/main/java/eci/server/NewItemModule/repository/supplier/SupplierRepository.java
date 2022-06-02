package eci.server.NewItemModule.repository.supplier;

import eci.server.NewItemModule.entity.supplier.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SupplierRepository extends JpaRepository<Supplier, Long>, CustomSupplierRepository{
}
