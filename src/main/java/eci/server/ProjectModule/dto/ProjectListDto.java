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
<<<<<<< HEAD
    private List<ProjectReadDto> content;

=======

    private List<ProjectReadDto> content;


>>>>>>> 3f9be407f2102ba559ea51c74810a0b8fc4e145a
    public static ProjectListDto toDto(Page<ProjectReadDto> page) {
        return new ProjectListDto(page.getTotalElements(), page.getTotalPages(), page.hasNext(), page.getContent());
    }
}
