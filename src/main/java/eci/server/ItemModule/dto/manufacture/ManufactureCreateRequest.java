package eci.server.ItemModule.dto.manufacture;
import eci.server.ItemModule.entity.manufacture.Manufacture;

import javax.validation.constraints.NotBlank;

public class ManufactureCreateRequest {
    @NotBlank(message = "코드를 입력해주세요.")
    private String code;

    @NotBlank(message = "이름을 입력해주세요.")
    private String name;

    public Manufacture toEntity(ManufactureCreateRequest req) {

        return new Manufacture(
                req.code,
                req.name
        );
    }
}
