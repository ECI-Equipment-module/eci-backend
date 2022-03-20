package eci.server.web.item.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
@Getter
@NoArgsConstructor
public class ItemResponseDtos{

    public static final int INDEX_PARAMETER = 1;
    private int pageNumber;
    private int totalPages;
    private List<ItemResponseDto> items;

    public ItemResponseDtos(int pageNumber, int totalPages, List<ItemResponseDto> items){
        this.pageNumber = pageNumber + INDEX_PARAMETER;
        this.totalPages = totalPages;
        this.items = items;
    }

    @Builder
        public ItemResponseDtos(List<ItemResponseDto> items){
        this.items = items ;
    }

}
