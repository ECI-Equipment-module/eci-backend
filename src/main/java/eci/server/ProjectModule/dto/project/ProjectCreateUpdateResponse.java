package eci.server.ProjectModule.dto.project;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProjectCreateUpdateResponse{
    private Long id;
    private Long routeId;

}


