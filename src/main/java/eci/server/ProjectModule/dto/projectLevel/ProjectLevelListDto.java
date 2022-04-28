package eci.server.ProjectModule.dto.projectLevel;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
@AllArgsConstructor
public class ProjectLevelListDto {
    private Long totalElements;
    private Integer totalPages;
    private boolean hasNext;
    private List<ProjectLevelReadResponse> postList;

    public static ProjectLevelListDto toDto(Page<ProjectLevelReadResponse> page) {
        return new ProjectLevelListDto(page.getTotalElements(), page.getTotalPages(), page.hasNext(), page.getContent());
    }
}
