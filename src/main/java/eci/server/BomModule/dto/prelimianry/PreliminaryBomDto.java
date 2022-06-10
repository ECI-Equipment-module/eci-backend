package eci.server.BomModule.dto.prelimianry;

import eci.server.BomModule.entity.PreliminaryBom;
import eci.server.NewItemModule.entity.JsonSave;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PreliminaryBomDto {
    String info;

    public static PreliminaryBomDto toDto(PreliminaryBom preliminaryBom){
        StringBuilder res= new StringBuilder();
        List<JsonSave> all = preliminaryBom.getJsonList();
        for(JsonSave s : all){
            res.append(s.getJsonText());
        }

        return new PreliminaryBomDto(
                res.toString()
        );
    }

}
