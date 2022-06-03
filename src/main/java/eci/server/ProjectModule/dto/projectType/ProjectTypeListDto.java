package eci.server.ProjectModule.dto.projectType;

import eci.server.ItemModule.dto.color.ColorReadResonse;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class ProjectTypeListDto {

    private List<String> indexes;
    private Long totalElements;
    private Integer totalPages;
    private boolean hasNext;
    private List<ProjectTypeReadResponse> content;

    public static ProjectTypeListDto toDto(Page<ProjectTypeReadResponse> page) {

        List<String> indexes = new ArrayList<>(); // 인덱스 종류 추가
        for(Field field : ProjectTypeReadResponse.class.getDeclaredFields()){
            indexes.add(field.getName());
        }

        return new ProjectTypeListDto(indexes, page.getTotalElements(), page.getTotalPages(), page.hasNext(), page.getContent());
    }

}

