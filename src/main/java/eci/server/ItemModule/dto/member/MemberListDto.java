package eci.server.ItemModule.dto.member;

import lombok.AllArgsConstructor;

import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
@AllArgsConstructor
public class MemberListDto {
    private Long totalElements;
    private Integer totalPages;
    private boolean hasNext;
    private List<MemberSimpleDto> content;

    public static MemberListDto toDto(Page<MemberSimpleDto> page) {
        return new MemberListDto(page.getTotalElements(), page.getTotalPages(), page.hasNext(), page.getContent());
    }
}