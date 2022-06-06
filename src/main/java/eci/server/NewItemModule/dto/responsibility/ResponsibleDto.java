package eci.server.NewItemModule.dto.responsibility;

import eci.server.BomModule.entity.Bom;
import eci.server.DesignModule.entity.design.Design;
import eci.server.config.guard.BomGuard;
import eci.server.config.guard.DesignGuard;
import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class ResponsibleDto {
    private String name;
    private String range;

    public ResponsibleDto toDto(
            String name,
            String range

    ) {
        return new ResponsibleDto(
                name,
                range
        );
    }
}
