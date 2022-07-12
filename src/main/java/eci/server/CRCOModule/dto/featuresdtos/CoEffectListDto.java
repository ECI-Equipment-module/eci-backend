package eci.server.CRCOModule.dto.featuresdtos;

import eci.server.CRCOModule.dto.featureresponse.CoEffectReadResponse;
import eci.server.CRCOModule.dto.featureresponse.CrImportanceResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class CoEffectListDto{

    private List<String> indexes;
    private Long totalElements;
    private Integer totalPages;
    private boolean hasNext;
    private List<CoEffectReadResponse> content;

    public static CoEffectListDto toDto(Page<CoEffectReadResponse> page) {

        List<String> indexes = new ArrayList<>(); // 인덱스 종류 추가
        indexes.add("id");
        indexes.add("name");

        return new CoEffectListDto(indexes, page.getTotalElements(),
                page.getTotalPages(), page.hasNext(), page.getContent());
    }
}

