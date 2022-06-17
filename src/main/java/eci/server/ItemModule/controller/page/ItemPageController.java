//package eci.server.ItemModule.controller.page;
//
//import eci.server.ItemModule.dto.item.ItemProjectCreateDto;
//import eci.server.ItemModule.dto.item.ItemSimpleDto;
//import eci.server.ItemModule.dto.response.Response;
//import eci.server.ItemModule.entity.item.Attachment;
////import eci.server.ItemModule.entity.item.Item;
//import eci.server.ItemModule.repository.item.ItemRepository;
//import eci.server.ItemModule.service.item.ItemService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageImpl;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.domain.Sort;
//import org.springframework.data.web.PageableDefault;
//import org.springframework.data.web.SortDefault;
//import org.springframework.web.bind.annotation.CrossOrigin;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import javax.transaction.Transactional;
//import java.util.Iterator;
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Transactional
//@RestController
//@RequiredArgsConstructor
//public class ItemPageController {
//
//    @Autowired
//    ItemRepository itemRepository;
//
//
//    @CrossOrigin(origins = "https://naughty-raman-7e7eb1.netlify.app")
//    @GetMapping("/item/page")
//    public Page<ItemSimpleDto> paging(@PageableDefault(size=5)
//                                      @SortDefault.SortDefaults({
//                                              @SortDefault(
//                                                      sort = "createdAt",
//                                                      direction = Sort.Direction.DESC)
//                                      })
//                                              Pageable pageRequest) {
//
//        Page<Item> itemListBefore = itemRepository.findAll(pageRequest);
//
//        List<Item> itemList1 =
//                itemListBefore.stream().filter(
//                        i->i.getTempsave().equals(false)
//                ).collect(Collectors.toList());
//
//        Page<Item> itemList = new PageImpl<>(itemList1);
//
//
//        return itemList.map(
//                item -> new ItemSimpleDto(
//
//                        item.getId(),
//                        item.getName(),
//                        item.getType(),
//                        item.getItemNumber(),
//                        item.getWidth(),
//                        item.getHeight(),
//                        item.getWeight(),
//                        item.getMember().getUsername(),
//                        item.getMaterials().get(0).getMaterial().getName(),
//                        item.getColor().getColor(),
//                        item.getThumbnail().get(0).getImageaddress(),
//                        item.getAttachments().stream().map(
//                                Attachment::getAttachmentaddress
//                        ).collect(Collectors.toList()),
//                        item.getCreatedAt()
//
//                )
//        );
//    }
//
//
//
//    /**
//     * 링크되지 않은 아이템들, 나에게 기다리고 있는 아이템들 05-27 수정
//     *
//     * @return 200 (success)
//     */
//    @Autowired
//    ItemService itemService;
//    @CrossOrigin(origins = "https://naughty-raman-7e7eb1.netlify.app")
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
//
//}