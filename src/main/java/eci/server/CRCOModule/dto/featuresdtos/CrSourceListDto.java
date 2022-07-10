package eci.server.CRCOModule.dto.featuresdtos;

import eci.server.CRCOModule.dto.featureresponse.CrImportanceResponse;
import eci.server.CRCOModule.dto.featureresponse.CrReasonReadResponse;
import eci.server.CRCOModule.dto.featureresponse.CrSourceReadResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class CrSourceListDto {

    private List<String> indexes;
    private Long totalElements;
    private Integer totalPages;
    private boolean hasNext;
    private List<CrSourceReadResponse> content;

    public static CrSourceListDto toDto(Page<CrSourceReadResponse> page) {

        List<String> indexes = new ArrayList<>(); // 인덱스 종류 추가
        for(Field field : CrImportanceResponse.class.getDeclaredFields()){
            indexes.add(field.getName());
        }

        return new CrSourceListDto(indexes, page.getTotalElements(),
                page.getTotalPages(), page.hasNext(), page.getContent());
    }
}

