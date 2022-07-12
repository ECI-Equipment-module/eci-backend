package eci.server.CRCOModule.dto.featuresdtos;

import eci.server.CRCOModule.dto.featureresponse.CrImportanceResponse;
import eci.server.ProjectModule.dto.clientOrg.ClientOrganizationReadResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class CrImportanceListDto {

    private List<String> indexes;
    private Long totalElements;
    private Integer totalPages;
    private boolean hasNext;
    private List<CrImportanceResponse> content;

    public static CrImportanceListDto toDto(Page<CrImportanceResponse> page) {

        List<String> indexes = new ArrayList<>(); // 인덱스 종류 추가
        for(Field field : CrImportanceResponse.class.getDeclaredFields()){
            indexes.add(field.getName());
        }

        return new CrImportanceListDto(indexes, page.getTotalElements(),
                page.getTotalPages(), page.hasNext(), page.getContent());
    }
}
