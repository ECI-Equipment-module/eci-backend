package eci.server.CRCOModule.dto.featuresdtos;

import eci.server.CRCOModule.entity.features.CrReason;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class  CrReasonDto{
    private Long id;
    private String value;

    public static  CrReasonDto toDto(){

        return new  CrReasonDto(

        );
    }

    public static  CrReasonDto toDto(CrReason crReason){

        return new  CrReasonDto(
                crReason.getId(),
                crReason.getName()
        );
    }
}

