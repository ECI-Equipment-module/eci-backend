package eci.server.ItemModule.dto.newRoute;

import eci.server.ItemModule.entity.newRoute.NewRoute;
import eci.server.ItemModule.entity.newRoute.NewRouteType;
import eci.server.ItemModule.entity.newRoute.RouteProduct;
import eci.server.ItemModule.exception.member.sign.MemberNotFoundException;
import eci.server.ItemModule.exception.route.RouteNotFoundException;
import eci.server.ItemModule.repository.item.ItemRepository;
import eci.server.ItemModule.repository.member.MemberRepository;
import eci.server.ItemModule.repository.newRoute.NewRouteRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Positive;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
/**
 * 두개 기준
 */
public class NewRouteCreateRequest4{

    @NotNull(message = "라우트의 타입을 입력해주세요")
    private Integer type;

    @NotNull(message = "아이템 아이디를 입력해주세요")
    @Positive(message = "올바른 아이템 아이디를 입력해주세요")
    private Long itemId;

    @Null // 첫번째 멤버 아이디는 무조건 itemId 작성자랑 동일 인물
    private Long memberId;

    private Long memberId2;

    private Long memberId3;

    private String requestComment;

    public static NewRoute toEntity(
            NewRouteCreateRequest4 req,
            NewRouteRepository newRouteRepository,
            ItemRepository itemRepository,
            NewRouteType newRouteType,
            MemberRepository memberRepository
    ){
        List<String> typeList = new ArrayList<>();
        List routeProduct = List.of((newRouteType.routeType[req.type]));
        for(Object type : routeProduct){
            typeList.add(type.toString());
        }


        NewRoute newRoute = new NewRoute (
                typeList.toString(),
                itemRepository.findById(req.itemId)
                        .orElseThrow(MemberNotFoundException::new)
        );

//        List routeProduct = List.of(List.of(newRouteType.routeType).get(req.type));
//
//        RouteProduct routeProduct1 = new RouteProduct(
//                        0,
//                        routeProduct.get(0).toString(),
//                        req.getRequestComment(),
//                        false,
//                        false,
//                        true,
//                        memberRepository.findById(req.getMemberId()).orElseThrow(MemberNotFoundException::new),
//                        newRouteRepository.findById(newRoute.getId()).orElseThrow(RouteNotFoundException::new)
//                );
//
//        RouteProduct routeProduct2 = new RouteProduct(
//                1,
//                routeProduct.get(1).toString(),
//                "",
//                false,
//                false,
//                true,
//                memberRepository.findById(req.getMemberId2()).orElseThrow(MemberNotFoundException::new),
//                newRouteRepository.findById(newRoute.getId()).orElseThrow(RouteNotFoundException::new)
//        );
//
//        RouteProduct routeProduct3 = new RouteProduct(
//                2,
//                routeProduct.get(2).toString(),
//                "",
//                false,
//                false,
//                true,
//                memberRepository.findById(req.getMemberId3()).orElseThrow(MemberNotFoundException::new),
//                newRouteRepository.findById(newRoute.getId()).orElseThrow(RouteNotFoundException::new)
//        );

        return newRoute;
    }

}
