package eci.server.CRCOModule.dto.featuresdtos;

import eci.server.CRCOModule.entity.features.CrImportance;
import eci.server.CRCOModule.entity.features.CrSource;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class  CrSourceDto{
    private Long id;
    private String name;

    public static  CrSourceDto toDto(){

        return new  CrSourceDto(

        );
    }

    public static  CrSourceDto toDto(CrSource crSource){

        return new  CrSourceDto(
                crSource.getId(),
                crSource.getName()
        );
    }
}

