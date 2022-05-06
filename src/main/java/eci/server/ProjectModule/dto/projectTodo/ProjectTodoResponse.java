package eci.server.ProjectModule.dto.projectTodo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectTodoResponse {
    Long projectId;
    String projectName;
    String projectType;
    Integer projectNumber;
}
