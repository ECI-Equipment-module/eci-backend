package eci.server.NewItemModule.controller.newItem;

import eci.server.NewItemModule.dto.item.ItemUpdateRequest;
import eci.server.NewItemModule.dto.newItem.NewItemReadCondition;
import eci.server.NewItemModule.dto.newItem.create.NewItemCreateRequest;
import eci.server.NewItemModule.dto.newItem.create.NewItemTemporaryCreateRequest;
import eci.server.NewItemModule.dto.newItem.update.NewItemUpdateRequest;
import eci.server.NewItemModule.service.item.NewItemService;
import eci.server.aop.AssignMemberId;
import eci.server.ItemModule.dto.response.Response;
import eci.server.aop.AssignModifierId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "https://localhost:3000")

public class NewItemController {

    private final NewItemService newItemService;

    /**
     * 아이템 임시저장
     *
     * @param req
     * @return 200 (success)
     */
    @CrossOrigin(origins = "https://localhost:3000")
    @PostMapping("/item/temp")
    @ResponseStatus(HttpStatus.CREATED)
    @AssignMemberId // Aspect : 인증된 사용자 정보로 아이템 작성자 지정 가능
    public Response tempCreate(
            @Valid @ModelAttribute
                    NewItemTemporaryCreateRequest req
    ) {

        return Response.success(
                newItemService.tempCreate(req));
    }

    /**
     * 아이템 생성 (찐 저장)
     *
     * @param req
     * @return 200 (success)
     */
    @CrossOrigin(origins = "https://localhost:3000")
    @PostMapping("/item")
    @ResponseStatus(HttpStatus.CREATED)
    @AssignMemberId // Aspect : 인증된 사용자 정보로 아이템 작성자 지정 가능
    public Response create(
            @Valid @ModelAttribute
                    //Content-Type = multipart/form-data
                    NewItemCreateRequest req
    ) {


        return Response.success(
                newItemService.create(req));
    }

    @CrossOrigin(origins = "https://localhost:3000")
    @GetMapping("/items")

    @ResponseStatus(HttpStatus.OK)
    public Response readAll(@Valid NewItemReadCondition cond) {
        return Response.success(
                newItemService.
                        readAll(cond));
    }

    /**
     * 특정 아이템 조회
     *
     * @param id
     * @return 200 (success)
     */
    @CrossOrigin(origins = "https://localhost:3000")
    @GetMapping(value = "/item/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Response read(
            @PathVariable Long id) {
        return Response.success(
                newItemService.read(id)

        );
    }



    /**
     * 특정 아이템+딸린 썸네일 이미지 삭제
     *
     * @param id
     * @return 200 (success)
     */
    @CrossOrigin(origins = "https://localhost:3000")
    @DeleteMapping("/item/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Response delete(
            @PathVariable Long id) {

        newItemService.delete(id);
        return Response.success();
    }

    /**
     * 특정 아이템 수정
     *
     * @param id
     * @param req
     * @return
     */
    @CrossOrigin(origins = "https://localhost:3000")
    @PutMapping("/item/{id}")
    @ResponseStatus(HttpStatus.OK)
    @AssignModifierId
    public Response update(
            @PathVariable Long id,
            @Valid @ModelAttribute NewItemUpdateRequest req) {


        return Response.success(newItemService.update(id, req));
    }

    /**
     * 아이템 전부 읽어오기긴 한데 페이징 needed
     * @param cond
     * @return
     */
//
//    @CrossOrigin(origins = "https://localhost:3000")
//    @GetMapping("/items")
//    @ResponseStatus(HttpStatus.OK)
//    public Response readAll(@Valid ItemReadCondition cond) {
//        return Response.success(itemService.readAll(cond));
//    }

    /**
     * 특정 사진 조회
     *
     * @return 200 (success)
     */

//    @CrossOrigin(origins = "https://localhost:3000")
//    @GetMapping(value=  "/images/{id}", produces= MediaType.IMAGE_PNG_VALUE)
//    @ResponseStatus(HttpStatus.OK)
//    public byte[] img(
//            @PathVariable Long id) {
//        {
//            byte[] image = itemService.readImg(id);
//            return image;
//
//        }
//    }


//    @CrossOrigin(origins = "https://localhost:3000")
//    @GetMapping("/item-candidates"xxxx)
//    @ResponseStatus(HttpStatus.OK)
//    public Response linkNeededItemForProject(
//            @Valid ItemProjectCreateReadCondition cond
//    ) {
//        return Response.success(
//                itemService.readItemCandidatesAll(cond)
//        );
//    }

}