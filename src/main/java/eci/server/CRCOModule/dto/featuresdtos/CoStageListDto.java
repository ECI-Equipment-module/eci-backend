package eci.server.CRCOModule.dto.featuresdtos;

import eci.server.CRCOModule.dto.featureresponse.CoStageReadResponse;
import eci.server.CRCOModule.dto.featureresponse.CrImportanceResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class CoStageListDto{

    private List<String> indexes;
    private Long totalElements;
    private Integer totalPages;
    private boolean hasNext;
    private List<CoStageReadResponse> content;

    public static CoStageListDto toDto(Page<CoStageReadResponse> page) {

        List<String> indexes = new ArrayList<>(); // 인덱스 종류 추가
        for(Field field : CrImportanceResponse.class.getDeclaredFields()){
            indexes.add(field.getName());
        }

        return new CoStageListDto(indexes, page.getTotalElements(),
                page.getTotalPages(), page.hasNext(), page.getContent());
    }
}
