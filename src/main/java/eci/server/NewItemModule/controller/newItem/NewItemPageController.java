package eci.server.NewItemModule.controller.newItem;

import eci.server.NewItemModule.dto.newItem.NewItemPagingDto;
import eci.server.NewItemModule.entity.NewItem;
import eci.server.NewItemModule.repository.item.NewItemRepository;
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

import java.util.List;
import java.util.stream.Collectors;

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
                        i-> i.isTempsave()
                ).collect(Collectors.toList());

        Page<NewItem> itemList = new PageImpl<>(itemList1);


        return itemList.map(
                item -> NewItemPagingDto.toDto(item)
        );
    }



//    /**
//     * 링크되지 않은 아이템들, 나에게 기다리고 있는 아이템들 05-27 수정
//     *
//     * @return 200 (success)
//     */
//    @Autowired
//    ItemService itemService;
//    @CrossOrigin(origins = "https://localhost:3000")
//    @GetMapping("/item-candidates")
//    public Page<ItemProjectCreateDto> readItemCandidate(@PageableDefault(size=5)
//                                      @SortDefault.SortDefaults({
//                                              @SortDefault(
//                                                      sort = "createdAt",
//                                                      direction = Sort.Direction.DESC)
//                                      })
//                                              Pageable pageRequest) {
//
//        List<ItemProjectCreateDto> itemListReal =
//                itemService.linkNeededItemsForProjectPage();
//
//        Page<ItemProjectCreateDto> itemList = new PageImpl<>(itemListReal);
//
//        return itemList;
//
//    }

}
