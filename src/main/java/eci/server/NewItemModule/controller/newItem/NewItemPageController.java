package eci.server.NewItemModule.controller.newItem;

import eci.server.ItemModule.dto.item.ItemProjectCreateDto;
import eci.server.ItemModule.entity.item.ItemType;
import eci.server.ItemModule.entity.item.ItemTypes;
import eci.server.ItemModule.repository.item.ItemTypesRepository;
import eci.server.ItemModule.repository.newRoute.RouteOrderingRepository;
import eci.server.NewItemModule.dto.newItem.NewItemChildDto;
import eci.server.NewItemModule.dto.newItem.NewItemPagingDto;
import eci.server.NewItemModule.entity.NewItem;
import eci.server.NewItemModule.entity.NewItemParentChildren;
import eci.server.NewItemModule.entity.page.CustomPageImpl;
import eci.server.NewItemModule.repository.item.NewItemParentChildrenRepository;
import eci.server.NewItemModule.repository.item.NewItemRepository;
import eci.server.NewItemModule.service.item.NewItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Transactional
@RestController
@RequiredArgsConstructor
public class NewItemPageController {

    @Autowired
    NewItemRepository newItemRepository;
    @CrossOrigin(origins = "https://localhost:3000")
    @GetMapping("/items/page")
    public Page<NewItemPagingDto> paging(@PageableDefault(size=5)
                                      @SortDefault.SortDefaults({
                                              @SortDefault(
                                                      sort = "createdAt",
                                                      direction = Sort.Direction.DESC)
                                      })
                                                 Pageable pageRequest) {

        Page<NewItem> itemListBefore = newItemRepository.findAll(pageRequest);

        List<NewItem> itemList1 =//06-01 false로 변경하기
                itemListBefore.stream().filter(
                        i-> (!i.isTempsave())
                ).collect(Collectors.toList());

        Page<NewItem> itemList = new PageImpl<>(itemList1);


        return itemList.map(
                item -> NewItemPagingDto.toDto(item)
        );
    }



    /**
     * 링크되지 않은 아이템들, 나에게 기다리고 있는 아이템들 05-27 수정
     *
     * @return 200 (success)
     */
    @Autowired
    NewItemService itemService;
    @CrossOrigin(origins = "https://localhost:3000")
    @GetMapping("/item-candidates")
    public Page<ItemProjectCreateDto> readItemCandidate(@PageableDefault(size=5)
                                      @SortDefault.SortDefaults({
                                              @SortDefault(
                                                      sort = "createdAt",
                                                      direction = Sort.Direction.DESC)
                                      })
                                              Pageable pageRequest) {

        List<ItemProjectCreateDto> itemListReal =
                itemService.linkNeededItemsForProjectPage();

        List<String> indexes = new ArrayList<>();
        indexes.add("itemNumber");
        indexes.add("itemName");

        Page<ItemProjectCreateDto> itemList = new CustomPageImpl<>(itemListReal, indexes);

        return itemList;

    }

    @CrossOrigin(origins = "https://localhost:3000")
    @GetMapping("bom/items/page")
    public Page<NewItemPagingDto> bomItems(@PageableDefault(size=5)
                                         @SortDefault.SortDefaults({
                                                 @SortDefault(
                                                         sort = "createdAt",
                                                         direction = Sort.Direction.DESC)
                                         })
                                                 Pageable pageRequest) {

        Page<NewItem> itemListBefore = newItemRepository.findAll(pageRequest);

        List<NewItem> itemList1 =//06-01 false로 변경하기
                itemListBefore.stream().filter(
                        i-> (!i.isTempsave())
                ).collect(Collectors.toList());

        Page<NewItem> itemList = new PageImpl<>(itemList1);


        return itemList.map(
                item -> NewItemPagingDto.toDto(item)
        );
    }

    /**
     * dev bom 에서 아이템 찾을 때 (봄도 데려오기)
     * @param pageRequest
     * @return
     */
    @Autowired
    private final ItemTypesRepository itemTypesRepository;
    private final RouteOrderingRepository routeOrderingRepository;
    private final NewItemService newItemService;
    @CrossOrigin(origins = "https://localhost:3000")
    @GetMapping("dev/bom/items/page")
    public Page<NewItemChildDto> devBomItems(@PageableDefault(size=5)
                                           @SortDefault.SortDefaults({
                                                   @SortDefault(
                                                           sort = "createdAt",
                                                           direction = Sort.Direction.DESC)
                                           })
                                                   Pageable pageRequest) {


        //1) 제품인 것 (상태가 complete, release 인 것만)

        // 1-1 ) 제품 타입 데려오기
        ItemType itemType1 = ItemType.프로덕트제품;
        ItemType itemType2 = ItemType.파트제품;
        ItemTypes productItemTypes1 = itemTypesRepository.findByItemType(itemType1);
        ItemTypes productItemTypes2 = itemTypesRepository.findByItemType(itemType2);
        List<ItemTypes> itemTypes = new ArrayList<>();
        itemTypes.add(productItemTypes1);
        itemTypes.add(productItemTypes2);

        List<NewItem> itemListProduct = newItemRepository.findByItemTypes(itemTypes);

        //1-2) 상태가 release 나 complete인 것만 최종 제품에 담을 예정
        List<NewItem> finalProducts = new ArrayList<>();

        for(NewItem newItem : itemListProduct){
            System.out.println("여기이ㅣㅣㅣㅣㅣㅣㅣㅣㅣㅣㅣㅣㅣㅣㅣㅣㅣㅣㅣㅣㅣ");
            System.out.println(routeOrderingRepository.findById(1399L));
            if(
                    routeOrderingRepository.findByNewItem(newItem).size()>0
            && (routeOrderingRepository.findByNewItem(newItem).get(
                    routeOrderingRepository.findByNewItem(newItem).size()-1
            ).getLifecycleStatus().equals("COMPLETE") ||
                    (routeOrderingRepository.findByNewItem(newItem).get(
                            routeOrderingRepository.findByNewItem(newItem).size()-1
                    ).getLifecycleStatus().equals("RELEASE")
            )
            )
            ){
                finalProducts.add(newItem);
            }
        }

        // 2) 제품 아닌 것 (temp save만 false면 다된다)

        List<ItemType> itemTypeList = new ArrayList<>();
        itemTypeList.add(ItemType.단순외주구매품);
        itemTypeList.add(ItemType.기타);
        itemTypeList.add(ItemType.부자재);
        itemTypeList.add(ItemType.시방외주구매품);
        itemTypeList.add(ItemType.원재료);
        itemTypeList.add(ItemType.사내가공품);

        List<ItemTypes> elseItemTypes = new ArrayList<>();
        elseItemTypes.add(itemTypesRepository.findByItemType(itemTypeList.get(0)));
        elseItemTypes.add(itemTypesRepository.findByItemType(itemTypeList.get(1)));
        elseItemTypes.add(itemTypesRepository.findByItemType(itemTypeList.get(2)));
        elseItemTypes.add(itemTypesRepository.findByItemType(itemTypeList.get(3)));
        elseItemTypes.add(itemTypesRepository.findByItemType(itemTypeList.get(4)));
        elseItemTypes.add(itemTypesRepository.findByItemType(itemTypeList.get(5)));

        List<NewItem> itemListElse = newItemRepository.findByItemTypes(elseItemTypes);
        //제품 이외 아이템 더하고
        itemListElse.addAll(finalProducts);
        //여기에 상태 완료된 제품 아이템 더하기

        Page<NewItem> concatItemList = new PageImpl<>(itemListElse);

        Page<NewItemChildDto> finalList =
                NewItemChildDto.toAddChildDtoList(concatItemList, newItemService);

        return finalList;
    }

}

