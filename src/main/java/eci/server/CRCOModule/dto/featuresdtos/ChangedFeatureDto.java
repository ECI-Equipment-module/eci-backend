package eci.server.CRCOModule.dto.featuresdtos;

import eci.server.CRCOModule.entity.cofeatures.ChangedFeature;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class  ChangedFeatureDto{
    private Long id;
    private String name;

    public static  ChangedFeatureDto toDto(){

        return new  ChangedFeatureDto(

        );
    }

    public static  ChangedFeatureDto toDto(ChangedFeature ChangedFeature){

        return new  ChangedFeatureDto(
                ChangedFeature.getId()==null?-1L:ChangedFeature.getId(),
                ChangedFeature.getName()
        );
    }
}

