package eci.server.ItemModule.dto.newRoute.routeProduct;

import eci.server.ItemModule.dto.newRoute.routeOrdering.RouteOrderingCreateRequest;
import eci.server.ItemModule.dto.newRoute.routeOrdering.SeqAndName;
import eci.server.ItemModule.entity.item.ItemType;
import eci.server.ItemModule.entity.member.Member;
import eci.server.ItemModule.entity.newRoute.RouteOrdering;
import eci.server.ItemModule.entity.newRoute.RoutePreset;
import eci.server.ItemModule.entity.newRoute.RouteProduct;
import eci.server.ItemModule.exception.member.MemberNotFoundException;
import eci.server.ItemModule.exception.member.MemberOverAssignedException;
import eci.server.ItemModule.exception.route.MemberNotAssignedException;
import eci.server.ItemModule.repository.member.MemberRepository;
import eci.server.ItemModule.repository.newRoute.RouteTypeRepository;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class RouteProductCreateRequest {

    //item -> toEntityList
    public static List<RouteProduct> toEntityList(
            RouteOrderingCreateRequest req,
            RouteOrdering routeOrdering,
            RoutePreset routePreset,
            MemberRepository memberRepository,
            RouteTypeRepository routeTypeRepository

    ) {
        //아이템 타입의해 routeType 결정됨
        Integer routeTypeIdx = ItemType.valueOf(
                routeOrdering.getNewItem().getItemTypes().getItemType().toString()
                            ).label();


        List<RouteProduct> willBeSavedRouteProductList
                = new ArrayList<>();

        List<String> routeProductName = List.of((routePreset.itemRouteName[routeTypeIdx]));
        List<String> routeProductType = List.of((routePreset.itemRouteType[routeTypeIdx]));
        List<String> routeProductTypeModule = List.of((routePreset.itemRouteTypeModule[routeTypeIdx]));

        //만들어져야 하는 객체는 타입 길이에서 complete 뺀 만큼
        Integer neededRouteProductCnt = routeProductType.size()-1;

        //request member 제외해서 일을 빼줌
//06-01 자가결재 여기서 에러
                if (req.getMemberIds()!=null && neededRouteProductCnt - 1 < req.getMemberIds().size()) {
                    //멤버가 할당되지 않아서 짝이 안맞아
                    throw new MemberOverAssignedException();
                }

                if (neededRouteProductCnt - 1 > req.getMemberIds().size()) {
                    //멤버가 할당되지 않아서 짝이 안맞아
                    throw new MemberNotAssignedException();
                }

        // 1) request RouteProduct는 별도 생성 (코멘트 및 멤버 지정 이슈)

        //request하는 사람은 한 명인데, 리스트로 만드는 이유는 routeProduct에선 member을 List로 받기 때문이지
        List<Member> member1 = new ArrayList<>();

        member1.add(memberRepository.findById(req.getMemberId())
                .orElseThrow(MemberNotFoundException::new));


        RouteProduct requestRouteProduct = new RouteProduct(
                0,

                0,

                (String) routeProductName.get(0),

                //우선 NAME 으로 찾아와서
                routeTypeRepository.findByName((String) routeProductType.get(0)).
                        stream().filter(
                                i-> i.getModule().equals(
                                        //그것이 지정된 모듈이랑 SAME 한 라우트 타입 최종 고르기
                                        routeProductTypeModule.get(0)
                                )
                        ).collect(toList()).get(0),
                req.getRequestComment(),
                false,
                false,
                true,
                false,
                -1,
                member1,
                routeOrdering

        );

        int seq = 0;

        // 2) 나머지 routeProduct는 돌려서 생성

        List<RouteProduct> restRouteProducts = new ArrayList<>();

        Integer index = 0;

        for(List list : req.getMemberIds()){


            Integer finalIndex = index;
            RouteProduct routeProduct = new RouteProduct(
                    index+1,
                    index+1,
                    (String) routeProductName.get(index+1),

                    routeTypeRepository.findByName((String) routeProductType.get(index+1)).
                            stream().filter(
                                    m-> m.getModule().equals(
                                            routeProductTypeModule.get(finalIndex+1)
                                    )
                            ).collect(toList()).get(0),


                    "default",
                    false,
                    false,
                    true,
                    false,
                    -1,
                    req.getMemberIds().get(index) //memberIds에서는 0부터 시작(request member 포함x)
                            .stream().map(
                                    m->
                                            memberRepository.findById(m).
                                                    orElseThrow(MemberNotFoundException::new)
                            ).collect(
                                    toList()
                            ),
                    routeOrdering

            );

            restRouteProducts.add(routeProduct);

            index+=1;
        }

        willBeSavedRouteProductList.add(requestRouteProduct);

        willBeSavedRouteProductList.addAll(restRouteProducts);

        return willBeSavedRouteProductList;

    }


}
