package eci.server.ItemModule.dto.newRoute.projectRoute;

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

public class ProjectRouteProductCreateRequest {


    /**
     * project 용
     * @param req
     * @param routeOrdering
     * @param routePreset
     * @param memberRepository
     * @param routeTypeRepository
     * @return
     */
    public static List<RouteProduct> toEntityList(
            ProjectRouteOrderingCreateRequest req,
            RouteOrdering routeOrdering,
            RoutePreset routePreset,
            MemberRepository memberRepository,
            RouteTypeRepository routeTypeRepository
    ) {
//        System.out.println(routeOrdering.getItem().getType());

        //프로젝트의 아이템 타입의해 routeType 결정됨
        Integer routeTypeIdx = ItemType.valueOf(
                routeOrdering.getItem().getType()
        ).label();


        List<RouteProduct> willBeSavedRouteProductList
                = new ArrayList<>();

        List routeProductName = List.of((routePreset.projectRouteName[routeTypeIdx]));
        List routeProductType = List.of((routePreset.projectRouteType[routeTypeIdx]));
        List routeProductTypeModule = List.of((routePreset.projectRouteTypeModule[routeTypeIdx]));

        //만들어져야 하는 객체는 타입 길이에서 complete 뺀 만큼
        Integer neededRouteProductCnt = routeProductType.size()-1;

        //request member 제외해서 일을 빼줌
        if(neededRouteProductCnt-1 < req.getMemberIds().size()) {
            //멤버가 할당되지 않아서 짝이 안맞아
            throw new MemberOverAssignedException();
        }

        if(neededRouteProductCnt-1 > req.getMemberIds().size()) {
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
                false,
                member1,
                routeOrdering

        );

        // 2) 나머지 routeProduct는 돌려서 생성
        List<RouteProduct> restRouteProducts =

                req.getMemberIds().stream().map(
                        i ->
                                new RouteProduct(
                                        req.getMemberIds().indexOf(i)+1, //request이후의 sequence 이므로 1부터 시작

                                        req.getMemberIds().indexOf(i)+1,

                                        (String) routeProductName.get(req.getMemberIds().indexOf(i)+1),

                                        routeTypeRepository.findByName((String) routeProductType.get(req.getMemberIds().indexOf(i)+1)).
                                                stream().filter(
                                                        m-> m.getModule().equals(
                                                                routeProductTypeModule.get(req.getMemberIds().indexOf(i)+1)
                                                        )
                                                ).collect(toList()).get(0),

                                        "default",
                                        false,
                                        false,
                                        true,
                                        false,
                                        false,
                                        req.getMemberIds().get(req.getMemberIds().indexOf(i)) //memberIds에서는 0부터 시작(request member 포함x)
                                                .stream().map(
                                                        m->
                                                                memberRepository.findById(m).
                                                                        orElseThrow(MemberNotFoundException::new)
                                                ).collect(
                                                        toList()
                                                ),
                                        routeOrdering
                                )
                ).collect(toList());

        willBeSavedRouteProductList.add(requestRouteProduct);
        for(RouteProduct restRouteProduct : restRouteProducts) {
            willBeSavedRouteProductList.add(restRouteProduct);
        }

        return willBeSavedRouteProductList;

    }
}
