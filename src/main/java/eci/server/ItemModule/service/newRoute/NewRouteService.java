package eci.server.ItemModule.service.newRoute;

import eci.server.ItemModule.dto.newRoute.*;
import eci.server.ItemModule.dto.route.*;
import eci.server.ItemModule.entity.newRoute.NewRoute;
import eci.server.ItemModule.entity.newRoute.NewRouteType;
import eci.server.ItemModule.entity.newRoute.RouteProduct;
import eci.server.ItemModule.exception.route.RouteNotFoundException;
import eci.server.ItemModule.repository.item.ItemRepository;
import eci.server.ItemModule.repository.member.MemberRepository;
import eci.server.ItemModule.repository.newRoute.NewRouteRepository;
import eci.server.ItemModule.repository.newRoute.RouteProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class NewRouteService {
    private final RouteProductRepository routeProductRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;
    private final NewRouteRepository newRouteRepository;
    private final NewRouteType newRouteType;


    public NewRouteDto read(Long id) {
        return NewRouteDto.toDto(
                newRouteRepository.findById(id).orElseThrow(RouteNotFoundException::new),
                routeProductRepository,
                newRouteRepository
        );
    }

    public List<NewRouteDto> readAll(NewRouteReadCondition cond) {

        List<NewRoute> newRoutes = newRouteRepository.findByItem(
                itemRepository.findById(cond.getItemId())
                        .orElseThrow(RouteNotFoundException::new)
        );

        return NewRouteDto.toDtoList(
                newRoutes,
                routeProductRepository,
                newRouteRepository
        );
    }

    @Transactional
    public void create2(NewRouteCreateRequest2 req) {
        NewRoute newRoute = newRouteRepository.save(NewRouteCreateRequest2.toEntity(
                req,
                itemRepository,
                newRouteType
                )
        );
        routeProductRepository.save(RouteProductCreateRequest2.toEntity(
                req,
                newRoute,
                newRouteType,
                memberRepository

        ));
    }

    @Transactional
    public void create4(NewRouteCreateRequest4 req) {
        NewRoute newRoute = newRouteRepository.save(NewRouteCreateRequest4.toEntity(
                        req,
                        itemRepository,
                        newRouteType
                )
        );

        List<RouteProduct> routeProductList =
                RouteProductCreateRequest4.toEntityList(
                req,
                newRoute,
                newRouteType,
                memberRepository

        );

        for(RouteProduct routeProduct : routeProductList ){
            routeProductRepository.save(routeProduct);
        }

    }

    @Transactional
    public List<RouteProductDto> rejectUpdate(
            Long id,
            String rejectComment,
            Integer rejectedSequence
    ) {

        NewRoute newRoute = newRouteRepository.findById(id).orElseThrow(RouteNotFoundException::new);

        //추가적으로 만들어진 애들 반환
        List<RouteProduct> rejectUpdatedRouteProductList = newRoute.rejectUpdate(
                id,
                rejectComment,
                rejectedSequence,
                newRouteRepository,
                routeProductRepository

        );



        List<RouteProduct> addedProducts = new ArrayList<>();

        // 새로 만들어진 애들 저장
        for(RouteProduct routeProduct:rejectUpdatedRouteProductList){
            addedProducts.add(routeProductRepository.save(routeProduct));
        }

        // present는 현재 만들어진 애로 설정
        newRoute.setPresent(addedProducts.get(0).getSequence());

        List<RouteProductDto> allProductList =
                RouteProductDto.toProductDtoList(
                        routeProductRepository.findAllByNewRoute(newRoute)
                );

        // 기존 + 새 라우트프로덕트까지 해서 돌려주기
        return allProductList;
    }

    @Transactional
    public RouteUpdateResponse update(Long id, NewRouteUpdateRequest req) {
        NewRoute newRoute = newRouteRepository
                .findById(id)
                .orElseThrow(RouteNotFoundException::new);

        NewRouteUpdateRequest newRouteUpdateRequest =
                newRoute.update(
                req,
                routeProductRepository
        );

        return new RouteUpdateResponse(id);
    }

}