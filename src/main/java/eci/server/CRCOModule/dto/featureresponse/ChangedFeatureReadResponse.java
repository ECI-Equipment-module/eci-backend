package eci.server.CRCOModule.dto.featureresponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangedFeatureReadResponse{
    private Long id;
    private String name;
}

