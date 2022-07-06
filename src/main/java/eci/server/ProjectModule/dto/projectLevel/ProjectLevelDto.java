package eci.server.ProjectModule.dto.projectLevel;

import eci.server.ProjectModule.entity.project.ProduceOrganization;
import eci.server.ProjectModule.entity.project.ProjectLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectLevelDto {
    private Long id;
    private String name;

    public static ProjectLevelDto toDto(){

        return new ProjectLevelDto();
    }

    public static ProjectLevelDto toDto(ProjectLevel projectLevel){

        return new ProjectLevelDto(
                projectLevel.getId(),
                projectLevel.getName()
        );
    }
}
