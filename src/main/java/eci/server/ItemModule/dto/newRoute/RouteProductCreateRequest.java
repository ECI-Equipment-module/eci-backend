package eci.server.ItemModule.dto.newRoute;

import eci.server.ItemModule.entity.member.Member;
import eci.server.ItemModule.entity.newRoute.NewRoute;
import eci.server.ItemModule.entity.newRoute.NewRouteType;
import eci.server.ItemModule.entity.newRoute.RouteProduct;
import eci.server.ItemModule.exception.member.MemberNotFoundException;
import eci.server.ItemModule.exception.route.MemberNotAssignedException;
import eci.server.ItemModule.repository.member.MemberRepository;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class RouteProductCreateRequest {

    public static List<RouteProduct> toEntityList(
            NewRouteCreateRequest req,
            NewRoute newRoute,
            NewRouteType newRouteType,
            MemberRepository memberRepository
    ) {

        List<RouteProduct> willBeSavedRouteProductList
                = new ArrayList<>();

        List routeProductType = List.of((newRouteType.routeType[req.getType()]));
        //만들어져야 하는 객체는 타입 길이에서 complete 뺀 만큼
        Integer neededRouteProductCnt = routeProductType.size()-1;

        //request member 제외해서 일을 빼줌
        if(neededRouteProductCnt-1 != req.getMemberIds().size()) {
            //멤버가 할당되지 않아서 짝이 안맞아
                throw new MemberNotAssignedException();
        }
        // 1) request RouteProduct는 별도 생성 (코멘트 및 멤버 지정 이슈)

        List<Member> member1 = new ArrayList<>();
        //request하는 사람은 한 명인데, 리스트로 만드는 이유는
        //routeProduct에선 member을 List로 받기 때문이지.

        member1.add(memberRepository.findById(req.getMemberId())
                .orElseThrow(MemberNotFoundException::new));

        RouteProduct requestRouteProduct = new RouteProduct(
                0,
                (String) routeProductType.get(0),
                req.getRequestComment(),
                false,
                false,
                true,
                false,
                member1,
                newRoute

        );

        // 2) 나머지 routeProduct는 돌려서 생성
        List<RouteProduct> restRouteProducts =
                req.getMemberIds().stream().map(
                        i ->
        new RouteProduct(
                req.getMemberIds().indexOf(i)+1, //request이후의 sequence 이므로 1부터 시작
                (String) routeProductType.get(req.getMemberIds().indexOf(i)+1),
                "default",
                false,
                false,
                true,
                false,
                req.getMemberIds().get(req.getMemberIds().indexOf(i)) //memberIds에서는 0부터 시작(request member 포함x)
                        .stream().map(
                        m->
                                memberRepository.findById(m).
                                        orElseThrow(MemberNotFoundException::new)
                ).collect(
                        toList()
                ),
                newRoute
        )
        ).collect(toList());

        willBeSavedRouteProductList.add(requestRouteProduct);
        for(RouteProduct restRouteProduct : restRouteProducts) {
            willBeSavedRouteProductList.add(restRouteProduct);
        }

        return willBeSavedRouteProductList;

    }
}

