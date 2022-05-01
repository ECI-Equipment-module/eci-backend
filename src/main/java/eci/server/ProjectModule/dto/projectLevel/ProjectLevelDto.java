package eci.server.ProjectModule.dto.projectLevel;

import eci.server.ProjectModule.entity.project.ProduceOrganization;
import eci.server.ProjectModule.entity.project.ProjectLevel;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProjectLevelDto {
    private Long id;
    private String name;

    public static ProjectLevelDto toDto(ProjectLevel projectLevel){

        return new ProjectLevelDto(
                projectLevel.getId(),
                projectLevel.getName()
        );
    }
}
