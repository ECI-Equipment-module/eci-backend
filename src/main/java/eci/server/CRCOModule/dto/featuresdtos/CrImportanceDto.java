package eci.server.CRCOModule.dto.featuresdtos;

import eci.server.CRCOModule.entity.features.CrImportance;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class  CrImportanceDto{
    private Long id;
    private String name;

    public static  CrImportanceDto toDto(){

        return new  CrImportanceDto(

        );
    }

    public static  CrImportanceDto toDto(CrImportance crImportance){

        return new  CrImportanceDto(
                crImportance.getId(),
                crImportance.getName()
        );
    }
}
