package eci.server.ProjectModule.dto.produceOrg;

import eci.server.ItemModule.dto.item.ItemProjectCreateDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class ProduceOrganizationListDto {

    private List<String> indexes;
    private Long totalElements;
    private Integer totalPages;
    private boolean hasNext;
    private List<ProduceOrganizationReadResponse> content;

    public static ProduceOrganizationListDto toDto(Page<ProduceOrganizationReadResponse> page) {

        List<String> indexes = new ArrayList<>(); // 인덱스 종류 추가
        for(Field field : ProduceOrganizationReadResponse.class.getDeclaredFields()){
            indexes.add(field.getName());
        }

        return new ProduceOrganizationListDto(indexes, page.getTotalElements(), page.getTotalPages(), page.hasNext(), page.getContent());
    }
}
