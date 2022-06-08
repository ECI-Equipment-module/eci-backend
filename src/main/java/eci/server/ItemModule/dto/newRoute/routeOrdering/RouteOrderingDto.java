package eci.server.ItemModule.dto.newRoute.routeOrdering;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import eci.server.BomModule.repository.BomRepository;
import eci.server.BomModule.repository.PreliminaryBomRepository;
import eci.server.ItemModule.dto.newRoute.routeProduct.RouteProductDto;
import eci.server.ItemModule.entity.newRoute.RouteOrdering;
import eci.server.ItemModule.repository.newRoute.RouteOrderingRepository;
import eci.server.ItemModule.repository.newRoute.RouteProductRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RouteOrderingDto {

    private Long id;
    private String type;
    private String lifecycleStatus;
    private String workflowphase;
    private Integer revisedCnt;
    private Integer present;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;
    private List<RouteProductDto> routeProductList;

    //05-24 추가
    private RouteRejectPossibleResponse response;


    public static List <RouteOrderingDto> toDtoList(
            List <RouteOrdering> NewRoutes,
            RouteProductRepository routeProductRepository,
            RouteOrderingRepository routeOrderingRepository,
            BomRepository bomRepository,
            PreliminaryBomRepository preliminaryBomRepository
    ) {

        List<SeqAndName> tmpList = new ArrayList<>();
        RouteRejectPossibleResponse tmpRouteRejectPossibleResponse = new RouteRejectPossibleResponse(tmpList);

        List<RouteOrderingDto> newRouteDtos = NewRoutes.stream().map(
                c -> new RouteOrderingDto(
                        c.getId(),
                        c.getType(),
                        c.getLifecycleStatus(),
                        c.getLifecycleStatus().equals("RELEASE")?//release와 같다면 workflow는 complete
                                "Complete" : "In Progress",
                        c.getRevisedCnt() + 65,
                        c.getPresent(),
                        c.getCreatedAt(),
                        RouteProductDto.toProductDtoList(
                                routeProductRepository.findAllByRouteOrdering(
                                        routeOrderingRepository.findById(c.getId()).orElseThrow()
                        )
                        ),

                        tmpRouteRejectPossibleResponse


//                        routeProductRepository.findAllByRouteOrdering(
//                                routeOrderingRepository.findById(c.getId()).orElseThrow()


        )
        ).collect(
                toList()
        );
        return newRouteDtos;
    }

    public static RouteOrderingDto toDto(
            RouteOrdering Route,
            RouteProductRepository routeProductRepository,
            RouteOrderingRepository routeOrderingRepository,
            RouteRejectPossibleResponse routeRejectPossibleResponse
    ) {

        return new RouteOrderingDto(
                Route.getId(),
                Route.getType(),
                Route.getLifecycleStatus(),
                Route.getLifecycleStatus().equals("COMPLETE")?
                        "COMPLETE":"WORKING",
                Route.getRevisedCnt(),
                Route.getPresent(),
                Route.getCreatedAt(),

                RouteProductDto.toProductDtoList(
                        routeProductRepository.findAllByRouteOrdering(
                        routeOrderingRepository.findById(
                                Route.getId()
                        )
                                .orElseThrow()
        )
                ),

                routeRejectPossibleResponse

        );
    }

}