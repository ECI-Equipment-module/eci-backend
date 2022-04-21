package eci.server.ItemModule.dto.newRoute;

import eci.server.ItemModule.entity.member.Member;
import eci.server.ItemModule.entity.newRoute.NewRoute;
import eci.server.ItemModule.entity.newRoute.NewRouteType;
import eci.server.ItemModule.entity.newRoute.RouteProduct;
import eci.server.ItemModule.exception.member.MemberNotFoundException;
import eci.server.ItemModule.repository.member.MemberRepository;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

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
        List<Member> member1 = new ArrayList<>();
        member1.add(memberRepository.findById(req.getMemberId())
                .orElseThrow(MemberNotFoundException::new));

        RouteProduct routeProduct1 = new RouteProduct(
                0,
                (String) routeProduct.get(0),
                req.getRequestComment(),
                false,
                false,
                true,
                false,
                member1,
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
                req.getMemberId2().stream().map(
                        i->
                                memberRepository.findById(i).
                                        orElseThrow(MemberNotFoundException::new)
                ).collect(
                        toList()
                )
                ,

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
                req.getMemberId3().stream().map(
                        i->
                                memberRepository.findById(i).
                                        orElseThrow(MemberNotFoundException::new)
                ).collect(
                        toList()
                ),
                newRoute

        );

        willSaveRouteProductList.add(routeProduct1);
        willSaveRouteProductList.add(routeProduct2);
        willSaveRouteProductList.add(routeProduct3);

        return willSaveRouteProductList;

    }
}
