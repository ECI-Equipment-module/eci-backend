package eci.server.ItemModule.dto.manufacture;


import eci.server.ProjectModule.dto.carType.CarTypeReadResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
@Data
@AllArgsConstructor
public class MakerListDto {

    private List<String> indexes;
    private Long totalElements;
    private Integer totalPages;
    private boolean hasNext;
    private List<MakerSimpleDto> content;

    public static MakerListDto toDto(Page<MakerSimpleDto> page) {

        List<String> indexes = new ArrayList<>(); // 인덱스 종류 추가
        for(Field field : MakerSimpleDto.class.getDeclaredFields()){
            indexes.add(field.getName());
        }

        return new MakerListDto(indexes , page.getTotalElements(), page.getTotalPages(), page.hasNext(), page.getContent());
    }
}
