package eci.server.NewItemModule.dto.newItem.page;

import eci.server.NewItemModule.dto.newItem.NewItemPagingDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class  NewItemPagingDtoList {

    private List<String> indexes;
    private Long totalElements;
    private Integer totalPages;
    private boolean hasNext;
    private List<NewItemPagingDto> content;

    public static NewItemPagingDtoList toDto(Page<NewItemPagingDto> page) {

        List<String> indexes = new ArrayList<>(); // 인덱스 종류 추가
        for(Field field : NewItemPagingDto.class.getDeclaredFields()){
            indexes.add(field.getName());
        }

        return new NewItemPagingDtoList(indexes, page.getTotalElements(), page.getTotalPages(), page.hasNext(), page.getContent());
    }
}

