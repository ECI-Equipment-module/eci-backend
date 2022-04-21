package eci.server.ItemModule.dto.newRoute;

import eci.server.ItemModule.entity.newRoute.NewRoute;
import eci.server.ItemModule.entity.newRoute.NewRouteType;
import eci.server.ItemModule.entity.newRoute.RouteProduct;
import eci.server.ItemModule.exception.member.MemberNotFoundException;
import eci.server.ItemModule.repository.member.MemberRepository;

import java.util.ArrayList;
import java.util.List;

public class RouteProductCreateRequest4 {

    public static List<RouteProduct> toEntityList(
            NewRouteCreateRequest4 req,
            NewRoute newRoute,
            NewRouteType newRouteType,
            MemberRepository memberRepository
    ) {

        List<RouteProduct> willSaveRouteProductList
                = new ArrayList<>();


        List routeProduct = List.of((newRouteType.routeType[req.getType()]));

        RouteProduct routeProduct1 = new RouteProduct(
                0,
                (String) routeProduct.get(0),
                req.getRequestComment(),
                false,
                false,
                true,
                false,
                memberRepository.findById(req.getMemberId()).orElseThrow(MemberNotFoundException::new),
                newRoute

        );

        RouteProduct routeProduct2 = new RouteProduct(
                1,
                (String) routeProduct.get(1),
                "default",
                false,
                false,
                true,
                false,
                memberRepository.findById(req.getMemberId2()).orElseThrow(MemberNotFoundException::new),
                newRoute

        );

        RouteProduct routeProduct3 = new RouteProduct(
                2,
                (String) routeProduct.get(2),
                "default",
                false,
                false,
                true,
                false,
                memberRepository.findById(req.getMemberId3()).orElseThrow(MemberNotFoundException::new),
                newRoute

        );

        willSaveRouteProductList.add(routeProduct1);
        willSaveRouteProductList.add(routeProduct2);
        willSaveRouteProductList.add(routeProduct3);

        return willSaveRouteProductList;

    }
}
