package eci.server.ProjectModule.dto.projectType;

import eci.server.ProjectModule.entity.project.ProjectType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectTypeDto {
    private Long id;
    private String name;

    public static ProjectTypeDto toDto(){

        return new ProjectTypeDto(

        );
    }

    public static ProjectTypeDto toDto(ProjectType projectType){

        return new ProjectTypeDto(
                projectType.getId(),
                projectType.getName()
        );
    }
}
