package eci.server.ItemModule.dto.newRoute;

import eci.server.ItemModule.entity.member.Member;
import eci.server.ItemModule.entity.newRoute.NewRoute;
import eci.server.ItemModule.entity.newRoute.NewRouteType;
import eci.server.ItemModule.entity.newRoute.RouteProduct;
import eci.server.ItemModule.exception.member.MemberNotFoundException;
import eci.server.ItemModule.repository.member.MemberRepository;

import java.util.ArrayList;
import java.util.List;

public class RouteProductCreateRequest2 {


    public static RouteProduct toEntity(
            NewRouteCreateRequest2 req,
            NewRoute newRoute,
            NewRouteType newRouteType,
            MemberRepository memberRepository
    ) {

        List routeProduct = List.of((newRouteType.routeType[req.getType()]));

        List<Member> member1 = new ArrayList<>();
        member1.add(memberRepository.findById(req.getMemberId())
                .orElseThrow(MemberNotFoundException::new));


        return new RouteProduct(
                0,
                (String) routeProduct.get(0),
                req.getRequestComment(),
                false,// 수행된 지 여부
                false, //거절당했었는지 여부
                true, // 보여질 대상
                false, // 거절 가능한 대상
                member1,
                newRoute

        );
    }
}
