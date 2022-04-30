package eci.server.ItemModule.service.newRoute;

import eci.server.ItemModule.dto.newRoute.*;
import eci.server.ItemModule.dto.route.*;
import eci.server.ItemModule.entity.item.ItemType;
import eci.server.ItemModule.entity.newRoute.RouteOrdering;
import eci.server.ItemModule.entity.newRoute.RoutePreset;
import eci.server.ItemModule.entity.newRoute.RouteProduct;
import eci.server.ItemModule.exception.route.RouteNotFoundException;
import eci.server.ItemModule.repository.item.ItemRepository;
import eci.server.ItemModule.repository.member.MemberRepository;
import eci.server.ItemModule.repository.newRoute.NewRouteRepository;
import eci.server.ItemModule.repository.newRoute.RouteProductRepository;
import eci.server.ItemModule.repository.newRoute.RouteTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RouteOrderingService {
    private final RouteProductRepository routeProductRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;
    private final NewRouteRepository newRouteRepository;
    private final RoutePreset routePreset;//private final ItemType itemType;
    private final RouteTypeRepository routeTypeRepository;


    public RouteOrderingDto read(Long id) {
        return RouteOrderingDto.toDto(
                newRouteRepository.findById(id).orElseThrow(RouteNotFoundException::new),
                routeProductRepository,
                newRouteRepository
        );
    }

    public List<RouteOrderingDto> readAll(RouteOrderingReadCondition cond) {

        List<RouteOrdering> newRoutes = newRouteRepository.findByItem(
                itemRepository.findById(cond.getItemId())
                        .orElseThrow(RouteNotFoundException::new)
        );

        return RouteOrderingDto.toDtoList(
                newRoutes,
                routeProductRepository,
                newRouteRepository
        );
    }

    @Transactional
    public RouteOrderingCreateResponse create(RouteOrderingCreateRequest req) {
        RouteOrdering newRoute = newRouteRepository.save(RouteOrderingCreateRequest.toEntity(
                        req,
                        itemRepository,
                        routePreset,
                routeTypeRepository


                        //itemType
                )
        );

        List<RouteProduct> routeProductList =
                RouteProductCreateRequest.toEntityList(
                        req,
                        newRoute,
                        routePreset,
                        memberRepository,
                        routeTypeRepository
                        //itemType

                );

        for(RouteProduct routeProduct : routeProductList ){
            routeProductRepository.save(routeProduct);
        }

        return new RouteOrderingCreateResponse(newRoute.getId());
    }

    @Transactional
    public List<RouteProductDto> rejectUpdate(
            Long id,
            String rejectComment,
            Integer rejectedSequence
    ) {

        RouteOrdering routeOrdering = newRouteRepository.findById(id).orElseThrow(RouteNotFoundException::new);

        //추가적으로 만들어진 애들 반환
        List<RouteProduct> rejectUpdatedRouteProductList = routeOrdering.rejectUpdate(
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

        // 처음으로 복제된 애는 거부대상 아이의 복제품 => 얘의 set reject=true로 변경
        addedProducts.get(0).setRejected(true);
        Integer range = addedProducts.get(0).getSequence()+1;
        //show=false인 애들 삭제할건데 검사범위는 거부대상아이 전의

//        // present는 현재 만들어진 애로 설정
//        newRoute.setPresent(addedProducts.get(0).getSequence());

        List<RouteProduct> deletedList =
        //isShow 가 false 인 것은 삭제 처리
                routeProductRepository.findAllByNewRoute(routeOrdering)
                        .subList(routeOrdering.getPresent()-1, range)
                        .stream().filter(
                                d -> d.isRoute_show()==false
                        )
                        .collect(
                                Collectors.toList()
                        );

        //본격 삭제 진행
        for(RouteProduct routeProduct : deletedList){
            routeProductRepository.delete(routeProduct);
        }

        List<RouteProductDto> allProductList =
                RouteProductDto.toProductDtoList(
                        routeProductRepository.findAllByNewRoute(routeOrdering)
                );
        // 기존 + 새 라우트프로덕트까지 해서 돌려주기
        return allProductList;
    }

    @Transactional
    public RouteUpdateResponse update(Long id, RouteOrderingUpdateRequest req) {
        RouteOrdering newRoute = newRouteRepository
                .findById(id)
                .orElseThrow(RouteNotFoundException::new);

        RouteOrderingUpdateRequest newRouteUpdateRequest =
                newRoute.update(
                req,
                routeProductRepository
        );

        return new RouteUpdateResponse(id);
    }

}