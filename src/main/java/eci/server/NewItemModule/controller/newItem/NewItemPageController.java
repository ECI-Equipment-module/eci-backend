package eci.server.NewItemModule.controller.newItem;

import eci.server.ItemModule.dto.item.ItemProjectCreateDto;
import eci.server.ItemModule.repository.item.ItemTypesRepository;
import eci.server.ItemModule.repository.newRoute.RouteOrderingRepository;
import eci.server.NewItemModule.dto.newItem.NewItemChildDto;
import eci.server.NewItemModule.dto.newItem.NewItemPagingDto;
import eci.server.NewItemModule.entity.NewItem;
import eci.server.NewItemModule.entity.page.CustomPageImpl;
import eci.server.NewItemModule.repository.item.NewItemRepository;
import eci.server.NewItemModule.service.item.NewItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
@Transactional
@RestController
@RequiredArgsConstructor
public class NewItemPageController {

    @Autowired
    NewItemRepository newItemRepository;

    @Value("${default.image.address}")
    private String defaultImageAddress;

    @CrossOrigin(origins = "https://localhost:3000")
    @GetMapping("/items/page")
    public Page<NewItemPagingDto> paging(@PageableDefault(size=5)
                                         @SortDefault.SortDefaults({
                                                 @SortDefault(
                                                         sort = "createdAt",
                                                         direction = Sort.Direction.DESC)
                                         })
                                                 Pageable pageRequest) {

        //Page<NewItem> itemListBefore = (pageRequest);

        List<NewItem> itemList1 =//06-01 false??? ????????????
                newItemRepository.findAll().stream().filter(
                        i-> (!i.isTempsave())
                ).collect(Collectors.toList());

        Page<NewItem> itemList = newItemRepository.findByNewItems(itemList1, pageRequest);

        return itemList.map(
                item -> NewItemPagingDto.toDto(item, defaultImageAddress)
        );
    }



    /**
     * ???????????? ?????? ????????????, ????????? ???????????? ?????? ???????????? 05-27 ??????
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

        List<NewItem> itemList1 =//06-01 false??? ????????????
                itemListBefore.stream().filter(
                        i-> (!i.isTempsave())
                ).collect(Collectors.toList());

        Page<NewItem> itemList = new PageImpl<>(itemList1);


        return itemList.map(
                item -> NewItemPagingDto.toDto(item, defaultImageAddress)
        );
    }

    /**
     * dev bom ?????? ????????? ?????? ??? (?????? ????????????)
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


        Page<NewItem> concatItemList = newItemRepository.findByNewItems(
                newItemService.readDevBomItems(), pageRequest
        );

        return NewItemChildDto.toAddChildDtoList(concatItemList, newItemService, defaultImageAddress);
    }

    /**
     * compare bom ?????? ????????? ?????? ??? (?????? ????????????)
     * @param pageRequest
     * @return
     */

    @CrossOrigin(origins = "https://localhost:3000")
    @GetMapping("compare/bom/items/page")
    public Page<NewItemChildDto> compareBomItems(@PageableDefault(size=5)
                                             @SortDefault.SortDefaults({
                                                     @SortDefault(
                                                             sort = "createdAt",
                                                             direction = Sort.Direction.DESC)
                                             })
                                                     Pageable pageRequest) {


        Page<NewItem> concatItemList = newItemRepository.findByNewItems(
                newItemService.readCompareBomItems(), pageRequest
        );

        return NewItemChildDto.toAddChildDtoList(concatItemList, newItemService, defaultImageAddress);
    }

    // affectedItem
    @CrossOrigin(origins = "https://localhost:3000")
    @GetMapping("affected/items/page")
    public Page<NewItemChildDto> affectedItems(@PageableDefault(size=5)
                                                 @SortDefault.SortDefaults({
                                                         @SortDefault(
                                                                 sort = "createdAt",
                                                                 direction = Sort.Direction.DESC)
                                                 })
                                                         Pageable pageRequest) {


        Page<NewItem> concatItemList = newItemRepository.findByNewItems(
                newItemService.readAffectedItems(), pageRequest
        );

        return NewItemChildDto.toAddChildDtoList(concatItemList, newItemService, defaultImageAddress);
    }

    @CrossOrigin(origins = "https://localhost:3000")
    @GetMapping("/releaseItemId")
    public Page<NewItemChildDto> releaseChodoItems(@PageableDefault(size=5)
                                           @SortDefault.SortDefaults({
                                                   @SortDefault(
                                                           sort = "createdAt",
                                                           direction = Sort.Direction.DESC)
                                           })
                                                   Pageable pageRequest) {

        Page<NewItem> releaseAvailableItems = newItemRepository.findByNewItems(
                newItemService.releaseItem(), pageRequest
        );

        return  NewItemChildDto.toDtoList(releaseAvailableItems,
                 defaultImageAddress);
    }

}
