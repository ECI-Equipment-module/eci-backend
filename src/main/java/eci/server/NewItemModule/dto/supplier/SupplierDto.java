package eci.server.NewItemModule.dto.supplier;

import eci.server.NewItemModule.entity.supplier.Supplier;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SupplierDto {
    private Long id;
    private String name;

    public static SupplierDto toDto(){

        return new SupplierDto(
            99999L, "NONE"
        );
    }

    public static SupplierDto toDto(Supplier supplier){

        return new SupplierDto(
                supplier.getId(),
                supplier.getName()
        );
    }
}
