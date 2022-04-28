package eci.server.ProjectModule.dto.projectLevel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectLevelReadResponse {
    private Long id;
    private String name;
}