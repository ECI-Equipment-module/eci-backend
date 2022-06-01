package eci.server.ProjectModule.dto.carType;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class CarTypeListDto {

    private List<String> indexes;
    private Long totalElements;
    private Integer totalPages;
    private boolean hasNext;
    private List<CarTypeReadResponse> content;

    public static CarTypeListDto toDto(Page<CarTypeReadResponse> page) {

        List<String> indexes = new ArrayList<>(); // 인덱스 종류 추가
        for(Field field : CarTypeReadResponse.class.getDeclaredFields()){
            indexes.add(field.getName());
        }

        return new CarTypeListDto(indexes, page.getTotalElements(), page.getTotalPages(), page.hasNext(), page.getContent());
    }
}
