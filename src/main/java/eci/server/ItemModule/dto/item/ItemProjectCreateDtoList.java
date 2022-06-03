package eci.server.ItemModule.dto.item;

import eci.server.ItemModule.dto.color.ColorReadResonse;
import eci.server.ProjectModule.dto.carType.CarTypeReadResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
@Data
@AllArgsConstructor
public class ItemProjectCreateDtoList {

    private List<String> indexes;
    private Long totalElements;
    private Integer totalPages;
    private boolean hasNext;
    private List<ItemProjectCreateDto> content;

    public static ItemProjectCreateDtoList toDto(Page<ItemProjectCreateDto> page) {

        List<String> indexes = new ArrayList<>(); // 인덱스 종류 추가
        for(Field field : ItemProjectCreateDto.class.getDeclaredFields()){
            indexes.add(field.getName());
        }

        return new ItemProjectCreateDtoList(indexes, page.getTotalElements(), page.getTotalPages(), page.hasNext(), page.getContent());
    }
}
