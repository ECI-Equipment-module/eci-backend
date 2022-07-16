package eci.server.CRCOModule.dto.featuresdtos;

import eci.server.CRCOModule.entity.CoCoEffect;
import eci.server.CRCOModule.entity.cofeatures.CoEffect;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

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

    public static List<CoEffectDto> toDtoList(Collection<CoCoEffect> coEffect){

        List<CoEffect> onlyCoEffects =
                coEffect.stream().map(
                        c->c.getCoEffect()
        ).collect(Collectors.toList());

        return onlyCoEffects.stream().map(
                c ->new  CoEffectDto(
                        c.getId(),
                        c.getName()
                )
        ).collect(Collectors.toList());

    }

    public static List<CoEffectDto> toDtoList(){
        List<CoEffectDto> coEffectDtos= new ArrayList<>();

        return coEffectDtos;

    }
}

