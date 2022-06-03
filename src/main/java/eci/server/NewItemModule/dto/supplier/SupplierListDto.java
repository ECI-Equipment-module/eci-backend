package eci.server.NewItemModule.dto.supplier;

import eci.server.ItemModule.dto.item.ItemProjectCreateDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class SupplierListDto {

    private List<String> indexes;
    private Long totalElements;
    private Integer totalPages;
    private boolean hasNext;
    private List<SupplierReadResponse> content;

    public static SupplierListDto toDto(Page<SupplierReadResponse> page) {

        List<String> indexes = new ArrayList<>(); // 인덱스 종류 추가
        for(Field field : SupplierReadResponse.class.getDeclaredFields()){
            indexes.add(field.getName());
        }

        return new SupplierListDto(indexes, page.getTotalElements(), page.getTotalPages(), page.hasNext(), page.getContent());
    }
}
