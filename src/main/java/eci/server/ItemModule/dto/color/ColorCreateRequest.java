package eci.server.ItemModule.dto.color;

import eci.server.ItemModule.entity.item.Color;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ColorCreateRequest {

        @NotBlank(message = "색깔 코드를 입력해주세요.")
        private String code;

        @NotBlank(message = "색을 입력해주세요.")
        private String color;

        public Color toEntity(ColorCreateRequest req) {

            return new Color(
                    req.code,
                    req.color
            );
    }

}