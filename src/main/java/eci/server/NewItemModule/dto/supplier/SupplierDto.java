package eci.server.NewItemModule.dto.supplier;

import eci.server.NewItemModule.entity.supplier.Supplier;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SupplierDto {
    private Long id;
    private String name;

    public static SupplierDto toDto(){

        return new SupplierDto();
    }

    public static SupplierDto toDto(Supplier supplier){

        return new SupplierDto(
                supplier.getId(),
                supplier.getName()
        );
    }
}
