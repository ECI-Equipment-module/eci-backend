package eci.server.web.item.dto;

import eci.server.domain.item.Item;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ItemSaveRequestDto {
    private String name;
    private String type;
    private Integer revised_cnt;
    private Character revision;
    private Double weight;
    private Double width;
    private Double height;

    @Builder
    public ItemSaveRequestDto(String name, String type, Integer revised_cnt,
                              Character revision, Double weight, Double height, Double width) {
        this.name = name;
        this.type = type;
        this.revised_cnt = revised_cnt;
        this.revision = revision;
        this.weight = weight;
        this.height = height;
        this.width = width;
    }

    public Item toEntity(){
        return Item.builder()
                .name(name)
                .type(type)
                .revised_cnt(revised_cnt)
                .revision(revision)
                .weight(weight)
                .height(height)
                .width(width)
                .build();
    }



}
