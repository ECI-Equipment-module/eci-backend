//package eci.server.ItemModule.controller.item;
//
//import eci.server.ItemModule.dto.item.*;
//import eci.server.aop.AssignMemberId;
//import eci.server.ItemModule.dto.response.Response;
//import eci.server.aop.AssignModifierId;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.http.HttpStatus;
//import org.springframework.web.bind.annotation.*;
//
//import javax.validation.Valid;
//
//@RestController
//@RequiredArgsConstructor
//@Slf4j
//@CrossOrigin(origins = "https://localhost:3000")
//
//public class ItemController {
//
//    private final ItemService itemService;
//
//    /**
//     * 아이템 임시저장
//     *
//     * @param req
//     * @return 200 (success)
//     */
//    @PostMapping("/items/temp")
//    @ResponseStatus(HttpStatus.CREATED)
//    @AssignMemberId // Aspect : 인증된 사용자 정보로 아이템 작성자 지정 가능
//    public Response tempCreate(
//            @Valid @ModelAttribute
//                    ItemTemporaryCreateRequest req
//    ) {
//
//        return Response.success(
//
//                itemService.tempCreate(req));
//    }
//    @CrossOrigin(origins = "https://localhost:3000")
//    /**
//     * 아이템 생성 (찐 저장)
//     *
//     * @param req
//     * @return 200 (success)
//     */
//    @PostMapping("/items")
//    @ResponseStatus(HttpStatus.CREATED)
//    @AssignMemberId // Aspect : 인증된 사용자 정보로 아이템 작성자 지정 가능
//    public Response create(
//            @Valid @ModelAttribute
//                    //Content-Type = multipart/form-data
//                    ItemCreateRequest req
//    ) {
//
//
//        return Response.success(
//
//                itemService.create(req));
//    }
//
//    /**
//     * 특정 아이템 조회
//     *
//     * @param id
//     * @return 200 (success)
//     */
//    @CrossOrigin(origins = "https://localhost:3000")
//    @GetMapping(value = "/items/{id}")
//    @ResponseStatus(HttpStatus.OK)
//    public Response read(
//            @PathVariable Long id) {
//        return Response.success(
//                itemService.read(id)
//
//        );
//    }
//
//
//
//    /**
//     * 특정 아이템+딸린 썸네일 이미지 삭제
//     *
//     * @param id
//     * @return 200 (success)
//     */
//    @CrossOrigin(origins = "https://localhost:3000")
//    @DeleteMapping("/items/{id}")
//    @ResponseStatus(HttpStatus.OK)
//    public Response delete(
//            @PathVariable Long id) {
//
//        itemService.delete(id);
//        return Response.success();
//    }
//
//    /**
//     * 특정 아이템 수정
//     *
//     * @param id
//     * @param req
//     * @return
//     */
//    @CrossOrigin(origins = "https://localhost:3000")
//    @PutMapping("/items/{id}")
//    @ResponseStatus(HttpStatus.OK)
//    @AssignModifierId
//    public Response update(
//            @PathVariable Long id,
//            @Valid @ModelAttribute ItemUpdateRequest req) {
//
//
//        return Response.success(itemService.update(id, req));
//    }
//
//    /**
//     * 아이템 전부 읽어오기긴 한데 페이징 needed
//     * @param cond
//     * @return
//     */
////
////    @CrossOrigin(origins = "https://localhost:3000")
////    @GetMapping("/items")
////    @ResponseStatus(HttpStatus.OK)
////    public Response readAll(@Valid ItemReadCondition cond) {
////        return Response.success(itemService.readAll(cond));
////    }
//
//    /**
//     * 특정 사진 조회
//     *
//     * @return 200 (success)
//     */
//
////    @CrossOrigin(origins = "https://localhost:3000")
////    @GetMapping(value=  "/images/{id}", produces= MediaType.IMAGE_PNG_VALUE)
////    @ResponseStatus(HttpStatus.OK)
////    public byte[] img(
////            @PathVariable Long id) {
////        {
////            byte[] image = itemService.readImg(id);
////            return image;
////
////        }
////    }
//
//
//    /**
//     * 링크되지 않은 아이템들, 나에게 기다리고 있는 아이템들
//     *
//     * @return 200 (success)
//     */
//    @CrossOrigin(origins = "https://localhost:3000")
//    @GetMapping("/link-needed")
//    @ResponseStatus(HttpStatus.OK)
//    public Response linkNeeded() {
//        return Response.success(
//                itemService.linkNeededItem()
//        );
//    }
//
////    /**
////     * 링크되지 않은 아이템들, 나에게 기다리고 있는 아이템들
////     *
////     * @return 200 (success)
////     */
////    @CrossOrigin(origins = "https://localhost:3000")
////    @GetMapping("/item-candidates")
////    @ResponseStatus(HttpStatus.OK)
////    public Response linkNeededItemForProject() {
////        return Response.success(
////                itemService.linkNeededItemsForProject()
////        );
////    }
//
//
////    @CrossOrigin(origins = "https://localhost:3000")
////    @GetMapping("/item-candidates")
////    @ResponseStatus(HttpStatus.OK)
////    public Response linkNeededItemForProject(
////            @Valid ItemProjectCreateReadCondition cond
////    ) {
////        return Response.success(
////                itemService.readItemCandidatesAll(cond)
////        );
////    }
//
//}