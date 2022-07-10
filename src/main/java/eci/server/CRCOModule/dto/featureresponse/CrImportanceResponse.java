package eci.server.CRCOModule.dto.featureresponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CrImportanceResponse {
    private Long id;
    private String name;
}
