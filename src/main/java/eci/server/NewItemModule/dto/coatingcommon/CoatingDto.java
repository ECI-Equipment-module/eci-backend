package eci.server.NewItemModule.dto.coatingcommon;

import eci.server.NewItemModule.entity.coating.CoatingType;
import eci.server.NewItemModule.entity.coating.CoatingWay;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CoatingDto {
    private Long id;
    private String name;

    public static CoatingDto toDto(CoatingType c){
        return new CoatingDto(c.getId(), c.getName());
    }

    public static CoatingDto toDto(CoatingWay c){
        return new CoatingDto(c.getId(), c.getName());
    }
}
