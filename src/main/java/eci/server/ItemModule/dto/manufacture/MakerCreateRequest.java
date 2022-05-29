package eci.server.ItemModule.dto.manufacture;
import eci.server.ItemModule.entity.manufacture.Maker;

import javax.validation.constraints.NotBlank;

public class MakerCreateRequest {
    @NotBlank(message = "코드를 입력해주세요.")
    private String code;

    @NotBlank(message = "이름을 입력해주세요.")
    private String name;

    public Maker toEntity(MakerCreateRequest req) {

        return new Maker(
                req.code,
                req.name
        );
    }
}
