package eci.server.ItemModule.dto.newRoute.routeProduct;

import com.fasterxml.jackson.annotation.JsonInclude;
import eci.server.ItemModule.dto.member.MemberDto;
import eci.server.ItemModule.entity.newRoute.RouteProduct;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import static java.util.stream.Collectors.toList;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RouteProductDto {

    private Long id;
    private Integer sequence;
    private String type;
    private String name;
    private String comment;
    private boolean passed;
    private boolean rejected;
    private boolean refusal;
    private boolean show;
    private List<MemberDto> member;

    public static List<RouteProductDto> toProductDtoList(
            List <RouteProduct> RouteProducts
    ) {
        List<RouteProductDto> routeProductList = RouteProducts.stream().map(
                c -> new RouteProductDto(
                        c.getId(),
                        c.getSequence(),
                        c.getType().getName(),
                        c.getRoute_name(),
                        c.getComments(),
                        c.isPassed(),
                        c.isRejected(),
                        c.isRefusal(),
                        c.isRoute_show(),
                        MemberDto.toDtoList(
                                c.getMembers().stream().map(
                                        m -> m.getMember()
                                ).collect(toList())
                        )
                )
        ).collect(
                toList()
        );
        return routeProductList;
    }

    public static RouteProductDto toDto(RouteProduct routeProduct) {

        return new RouteProductDto(
                routeProduct.getId(),
                routeProduct.getSequence(),
                routeProduct.getType().getName(),
                routeProduct.getRoute_name(),
                routeProduct.getComments(),
                routeProduct.isPassed(),
                routeProduct.isRejected(),
                routeProduct.isRefusal(),
                routeProduct.isRoute_show(),
                MemberDto.toDtoList(
                        routeProduct.getMembers().stream().map(
                                m -> m.getMember()
                        ).collect(toList())
                )
        );
    }


}