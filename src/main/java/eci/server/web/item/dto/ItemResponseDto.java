package eci.server.web.item.dto;

import eci.server.domain.item.Item;
import eci.server.domain.route.Route;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class ItemResponseDto {
    private String name;
    private String type;
    private Integer revised_cnt;
    private Character revision;
    private Double weight;
    private Double width;
    private Double height;
    private List<Route> routeList;

@Builder
    public ItemResponseDto(Item entity) {
        this.name = entity.getName();
        this.type = entity.getType();
        this.revised_cnt = entity.getRevised_cnt();
        this.revision = entity.getRevision();
        this.weight = entity.getWeight();
        this.height = entity.getHeight();
        this.width = entity.getWidth();
        this.routeList = entity.getRouteList();
    }


    public ItemResponseDto(
            String type,
            String name,
            Character revision,
            Integer revised_cnt,
            Double width,
            Double weight,
            Double height,
            List<Route> routeList) {
        this.name = name;
        this.type = type;
        this.revised_cnt = revised_cnt;
        this.revision = revision;
        this.width = width;
        this.weight = weight;
        this.height = height;
        this.routeList = routeList;
    }
}
