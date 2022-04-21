package eci.server.ItemModule.dto.newRoute;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import eci.server.ItemModule.dto.member.MemberDto;
import eci.server.ItemModule.entity.newRoute.NewRoute;
import eci.server.ItemModule.entity.newRoute.RouteProduct;
import eci.server.ItemModule.entity.route.Route;
import eci.server.ItemModule.helper.NestedConvertHelper;
import eci.server.ItemModule.repository.newRoute.NewRouteRepository;
import eci.server.ItemModule.repository.newRoute.RouteProductRepository;
import eci.server.ItemModule.repository.route.RouteRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewRouteDto {

    private Long id;
    private String type;
    private String lifecycleStatus;
    private String workflowphase;
    private Integer revisedCnt;
    private Integer present;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;
    private List<RouteProductDto> routeProductList;


    public static List <NewRouteDto> toDtoList(
            List <NewRoute> NewRoutes,
            RouteProductRepository routeProductRepository,
            NewRouteRepository newRouteRepository
    ) {


        List<NewRouteDto> newRouteDtos = NewRoutes.stream().map(
                c -> new NewRouteDto(
                        c.getId(),
                        c.getType(),
                        c.getLifecycleStatus(),
                        c.getLifecycleStatus().equals("Release")?//release와 같다면 workflow는 complete
                                "Complete" : "In Progress",
                        c.getRevisedCnt() + 65,
                        c.getPresent(),
                        c.getCreatedAt(),
                        RouteProductDto.toProductDtoList(
                                routeProductRepository.findAllByNewRoute(
                                        newRouteRepository.findById(c.getId()).orElseThrow()
                        )
                        )

//                        routeProductRepository.findAllByNewRoute(
//                                newRouteRepository.findById(c.getId()).orElseThrow()


        )
        ).collect(
                toList()
        );
        return newRouteDtos;
    }

    public static NewRouteDto toDto(
            NewRoute Route,
            RouteProductRepository routeProductRepository,
            NewRouteRepository newRouteRepository
    ) {

        return new NewRouteDto(
                Route.getId(),
                Route.getType(),
                Route.getLifecycleStatus(),
                Route.getLifecycleStatus().equals("Release")?
                        "COMPLETE":"IN_PROGRESS",
                Route.getRevisedCnt(),
                Route.getPresent(),
                Route.getCreatedAt(),
                RouteProductDto.toProductDtoList(
                        routeProductRepository.findAllByNewRoute(
                        newRouteRepository.findById(
                                Route.getId()
                        )
                                .orElseThrow()
        )
                )

        );
    }

}