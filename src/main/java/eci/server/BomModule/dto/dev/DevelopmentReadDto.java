package eci.server.BomModule.dto.dev;

import eci.server.NewItemModule.dto.TempNewItemChildDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DevelopmentReadDto {

    private TempNewItemChildDto developmentBom;
    private Long routeId;
    private boolean preRejected;
    private boolean readonly;

    }


