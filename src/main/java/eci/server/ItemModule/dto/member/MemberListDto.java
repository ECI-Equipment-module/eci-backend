package eci.server.ItemModule.dto.member;

import eci.server.ItemModule.dto.color.ColorReadResonse;
import lombok.AllArgsConstructor;

import lombok.Data;
import org.springframework.data.domain.Page;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class MemberListDto {

    private List<String> indexes;
    private Long totalElements;
    private Integer totalPages;
    private boolean hasNext;
    private List<MemberSimpleDto> content;


    public static MemberListDto toDto(Page<MemberSimpleDto> page) {
        List<String> indexes = new ArrayList<>(); // 인덱스 종류 추가
        for(Field field : MemberSimpleDto.class.getDeclaredFields()){
            indexes.add(field.getName());
        }
        return new MemberListDto(indexes, page.getTotalElements(), page.getTotalPages(), page.hasNext(), page.getContent());
    }
}