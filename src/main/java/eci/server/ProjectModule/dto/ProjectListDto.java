package eci.server.ProjectModule.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
@AllArgsConstructor
public class ProjectListDto{
        private Long totalElements;
    private Integer totalPages;
    private boolean hasNext;
    private List<ProjectReadDto> content;

    public static ProjectListDto toDto(Page<ProjectReadDto> page) {
        return new ProjectListDto(page.getTotalElements(), page.getTotalPages(), page.hasNext(), page.getContent());
    }
}
