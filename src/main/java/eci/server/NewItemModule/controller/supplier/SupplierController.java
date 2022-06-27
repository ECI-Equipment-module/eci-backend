package eci.server.NewItemModule.controller.supplier;

import eci.server.ItemModule.dto.response.Response;
import eci.server.NewItemModule.dto.supplier.SupplierReadCondition;
import eci.server.NewItemModule.service.supplier.SupplierService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@CrossOrigin(origins = "https://localhost:3000")
public class SupplierController {
    private final SupplierService supplierService;

    @GetMapping("/supplierOrganizationId")
    @ResponseStatus(HttpStatus.OK)
    public Response readAll(@Valid SupplierReadCondition cond) {
        return Response.success(
                supplierService.
                        readAll(cond));
    }
}
