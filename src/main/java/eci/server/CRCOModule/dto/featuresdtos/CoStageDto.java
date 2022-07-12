package eci.server.CRCOModule.dto.featuresdtos;

import eci.server.CRCOModule.entity.cofeatures.CoStage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class  CoStageDto{
    private Long id;
    private String name;

    public static  CoStageDto toDto(){

        return new  CoStageDto(

        );
    }

    public static  CoStageDto toDto(CoStage CoStage){

        return new  CoStageDto(
                CoStage.getId(),
                CoStage.getName()
        );
    }
}


