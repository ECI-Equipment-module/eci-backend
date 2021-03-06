package eci.server.BomModule.dto.prelimianry;

import eci.server.BomModule.entity.PreliminaryBom;
import eci.server.BomModule.exception.PreliminaryBomNotFoundException;
import eci.server.BomModule.repository.PreliminaryBomRepository;
import eci.server.NewItemModule.entity.JsonSave;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JsonSaveCreateRequest {
    @NotNull
    @NotBlank(message = "preliminary json 내용을 입력해주세요.")
    private String content;

    @NotNull
    private Long preliminaryId;

    public JsonSave toEntity (
            JsonSaveCreateRequest req,
            String text,
            PreliminaryBomRepository preliminaryBomRepository){

        PreliminaryBom preliminaryBom = preliminaryBomRepository.findById(req.getPreliminaryId()).orElseThrow(PreliminaryBomNotFoundException::new);

            return new JsonSave(
                    text,
                    preliminaryBom

            );

    }

}
