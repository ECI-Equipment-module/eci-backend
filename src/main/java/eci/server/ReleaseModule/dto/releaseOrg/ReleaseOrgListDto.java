package eci.server.ReleaseModule.dto.releaseOrg;

import eci.server.ProjectModule.dto.produceOrg.ProduceOrganizationReadResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class ReleaseOrgListDto{

    private List<String> indexes;
    private Long totalElements;
    private Integer totalPages;
    private boolean hasNext;
    private List<ProduceOrganizationReadResponse> content;

    public static ReleaseOrgListDto toDto(Page<ProduceOrganizationReadResponse> page) {

        List<String> indexes = new ArrayList<>(); // 인덱스 종류 추가
        indexes.add("id");
        indexes.add("배포처");

        return new ReleaseOrgListDto(indexes, page.getTotalElements(),
                page.getTotalPages(), page.hasNext(), page.getContent());
    }
}

