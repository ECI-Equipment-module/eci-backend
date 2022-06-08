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

    public PreliminaryBomDto toDto(PreliminaryBom preliminaryBom){
        String res="";
        List<JsonSave> all = preliminaryBom.getJsonList();
        for(JsonSave s : all){
            res+=s.getJsonText();
        }

        return new PreliminaryBomDto(
                res
        );
    }

}
