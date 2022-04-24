package eci.server.ItemModule.dto.newRoute;

import eci.server.ItemModule.entity.item.Item;
import eci.server.ItemModule.entity.item.ItemType;
import eci.server.ItemModule.entity.newRoute.RouteOrdering;
import eci.server.ItemModule.entity.newRoute.RoutePreset;
import eci.server.ItemModule.entity.newRoute.RouteType;
import eci.server.ItemModule.exception.item.ItemNotFoundException;
import eci.server.ItemModule.exception.member.sign.MemberNotFoundException;
import eci.server.ItemModule.repository.item.ItemRepository;
import eci.server.ItemModule.repository.newRoute.RouteTypeRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
public class RouteOrderingCreateRequest {

//    @NotNull(message = "라우트의 타입을 입력해주세요")
//    private Integer type;

    @NotNull(message = "아이템 아이디를 입력해주세요")
    @Positive(message = "올바른 아이템 아이디를 입력해주세요")
    private Long itemId;

    @Null // 첫번째 멤버 아이디는 무조건 itemId 작성자랑 동일 인물
    private Long memberId;

    private ArrayList<ArrayList<Long>> memberIds;

    private String requestComment;

    public static RouteOrdering toEntity(
            RouteOrderingCreateRequest req,
            ItemRepository itemRepository,
            RoutePreset newRouteType,
            RouteTypeRepository routeTypeRepository
            //ItemType itemType
    ){
        Item targetItem = itemRepository.findById(req.itemId).orElseThrow(ItemNotFoundException::new);

        List<String> typeList = new ArrayList<>();

        //아이템 타입에따라서 라우트 타입이 선택된다.
        Integer routeType =  ItemType.valueOf(targetItem.getType()).label();

        RouteType routeType1 = routeTypeRepository.findByName("REVIEW").get(0);
//        System.out.println(routeType1.getName());
//        System.out.println(routeType1.getId());
//        System.out.println("succccccccccccessssssssssssss");
        List routeProduct = List.of((newRouteType.routeName[routeType]));

        System.out.println(routeProduct.size());
        for(Object type : routeProduct){
            typeList.add(type.toString());
            System.out.println(type);
        }


        return new RouteOrdering(
                typeList.toString(),
                itemRepository.findById(req.itemId)
                        .orElseThrow(MemberNotFoundException::new)
        );
    }

}