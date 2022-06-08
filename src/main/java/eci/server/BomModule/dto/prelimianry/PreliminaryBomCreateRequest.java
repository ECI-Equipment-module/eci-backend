package eci.server.BomModule.dto.prelimianry;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PreliminaryBomCreateRequest {
    @NotNull
    @NotBlank(message = "preliminary json 내용을 입력해주세요.")
    private String content;

}
