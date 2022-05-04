package eci.server.ProjectModule.dto.projectType;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
@AllArgsConstructor
public class ProjectTypeListDto {
    private Long totalElements;
    private Integer totalPages;
    private boolean hasNext;
    private List<ProjectTypeReadResponse> content;

    public static ProjectTypeListDto toDto(Page<ProjectTypeReadResponse> page) {
        return new ProjectTypeListDto(page.getTotalElements(), page.getTotalPages(), page.hasNext(), page.getContent());
    }
}

