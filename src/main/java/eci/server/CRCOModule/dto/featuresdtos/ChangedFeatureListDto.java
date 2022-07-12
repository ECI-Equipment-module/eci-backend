package eci.server.CRCOModule.dto.featuresdtos;

import eci.server.CRCOModule.dto.featureresponse.ChangedFeatureReadResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class ChangedFeatureListDto{

    private List<String> indexes;
    private Long totalElements;
    private Integer totalPages;
    private boolean hasNext;
    private List<ChangedFeatureReadResponse> content;

    public static ChangedFeatureListDto toDto(Page<ChangedFeatureReadResponse> page) {

        List<String> indexes = new ArrayList<>(); // 인덱스 종류 추가
        indexes.add("id");
        indexes.add("변경항목");

        return new ChangedFeatureListDto(indexes, page.getTotalElements(),
                page.getTotalPages(), page.hasNext(), page.getContent());
    }
}

