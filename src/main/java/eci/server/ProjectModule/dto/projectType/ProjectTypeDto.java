package eci.server.ProjectModule.dto.projectType;

import eci.server.ProjectModule.entity.project.ProjectType;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProjectTypeDto {
    private Long id;
    private String name;

    public static ProjectTypeDto toDto(ProjectType projectType){

        return new ProjectTypeDto(
                projectType.getId(),
                projectType.getName()
        );
    }
}
