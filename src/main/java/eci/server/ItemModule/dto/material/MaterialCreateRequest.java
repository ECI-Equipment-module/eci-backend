package eci.server.ItemModule.dto.material;

import eci.server.ItemModule.entity.material.Material;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MaterialCreateRequest {

    @NotBlank(message = "코드를 입력해주세요.")
    private String code;

    @NotBlank(message = "이름을 입력해주세요.")
    private String name;

    public Material toEntity(MaterialCreateRequest req) {

        return new Material(
                req.code,
                req.name
        );
    }
}
