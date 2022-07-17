package eci.server.ReleaseModule.dto;

import eci.server.CRCOModule.dto.co.CoSearchDto;
import eci.server.CRCOModule.dto.featureresponse.ChangedFeatureReadResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class CoListDto {

    private List<String> indexes;
    private Long totalElements;
    private Integer totalPages;
    private boolean hasNext;
        private List<CoSearchDto> contents;

    public static CoListDto toDto(Page<CoSearchDto> page) {

        List<String> indexes = new ArrayList<>(); // 인덱스 종류 추가
        indexes.add("id");
        indexes.add("CO Number");
        indexes.add("CO Type");
        indexes.add("이유");

        return new CoListDto(indexes, page.getTotalElements(),
                page.getTotalPages(), page.hasNext(), page.getContent());
    }
}


