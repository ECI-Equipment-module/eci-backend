package eci.server.ProjectModule.dto.projectType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectTypeReadResponse {
    private Long id;
    private String name;
}