package eci.server.DesignModule.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Data
public class DesignContentDto {

    private String cardNumber;
    private String cardName;
    private String classification;
    private String cardType;
    private String sharing;
    private Long preliminaryBomId;
    private List<DesignContentDto> children;

    public DesignContentDto(){
        super();
    }

}
