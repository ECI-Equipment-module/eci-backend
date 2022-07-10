package eci.server.CRCOModule.dto.featuresdtos;

import eci.server.CRCOModule.entity.cofeatures.CoEffect;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class  CoEffectDto{
    private Long id;
    private String name;

    public static  CoEffectDto toDto(){

        return new  CoEffectDto(

        );
    }

    public static  CoEffectDto toDto(CoEffect coEffect){

        return new  CoEffectDto(
                coEffect.getId(),
                coEffect.getName()
        );
    }
}

