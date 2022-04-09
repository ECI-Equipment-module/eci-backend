package eci.server.ItemModule.service.item;


import eci.server.ItemModule.config.guard.AuthHelper;
import eci.server.ItemModule.dto.item.*;
import eci.server.ItemModule.dto.route.RouteDto;
import eci.server.ItemModule.entity.member.Member;
import eci.server.ItemModule.exception.route.RouteNotFoundException;
import eci.server.ItemModule.repository.color.ColorRepository;
import eci.server.ItemModule.repository.route.RouteRepository;
import eci.server.ItemModule.service.file.FileService;
import eci.server.ItemModule.entity.item.Image;
import eci.server.ItemModule.entity.item.Item;
import eci.server.ItemModule.exception.item.ItemNotFoundException;
import eci.server.ItemModule.repository.item.ItemRepository;
import eci.server.ItemModule.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {

    public Logger logger = LoggerFactory.getLogger(ItemService.class);

    private final RouteRepository routeRepository;
    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;
    private final ColorRepository colorRepository;
    private final FileService fileService;
    private final AuthHelper authHelper;

    /**
     * 아이템 create
     * @param req
     * @return 생성된 아이템 번호
     */

    @Transactional
    public ItemCreateResponse create(ItemCreateRequest req) {

        Item item = itemRepository.save(
                ItemCreateRequest.toEntity(
                        req,
                        memberRepository,
                        colorRepository
                )

        );

        uploadImages(item.getThumbnail(), req.getThumbnail());
        return new ItemCreateResponse(item.getId());
    }

    /**
     * 썸네일 존재 시에 File Upload로 이미지 업로드
     * @param images
     * @param fileImages
     */

    private void uploadImages(List<Image> images, List<MultipartFile> fileImages) {
        // 실제 이미지 파일을 가지고 있는 Multipart 파일을
        // 이미지가 가지는 uniquename을 파일명으로 해서 파일저장소 업로드
        IntStream.range(0, images.size())
                .forEach(
                        i -> fileService.upload
                                (
                                fileImages.get(i),
                                images.get(i).getUniqueName()
                        )
                );
    }

    public ReadItemDto read(Long id) {

        //라우트가 없다면 라우트 없음 에러 던지기, 라우트 없으면 읽기도 사실상 불가능
        List <RouteDto> routeDtoList = Optional.ofNullable(RouteDto.toDtoList(
                routeRepository.findAllWithMemberAndParentByItemIdOrderByParentIdAscNullsFirstRouteIdAsc(id)
        )).orElseThrow(RouteNotFoundException::new);

        if (routeDtoList.size()>0) {
            //routeDto가 존재할 때
            ReadItemDto readItemDto = ReadItemDto.toDto(
                    ItemDto.toDto(
                            itemRepository.findById(id).orElseThrow(ItemNotFoundException::new)
                    ),
                    routeDtoList,
                    routeDtoList.get(routeDtoList.size() - 1)//최신 라우트
            );
            return readItemDto;
        } else{
            //route 가 존재하지 않을 시
            ReadItemDto readItemDto = ReadItemDto.toDto(
                    ItemDto.toDto(
                            itemRepository.findById(id).orElseThrow(ItemNotFoundException::new)
                    ),
                    routeDtoList
            );
            return readItemDto;
        }
    }

    public ItemTodoResponseList readTodo() {
        List<ItemTodoResponse> IN_PROGRESS = new ArrayList<>();
        List<ItemTodoResponse> NEED_REVISE = new ArrayList<>();
        List<ItemTodoResponse> REJECTED = new ArrayList<>();
        List<ItemTodoResponse> WAITING_APPROVAL = new ArrayList<>();
        //1) 내가 작성자인 모든 아이템들을 데려오기
        Optional<Member> member1 = memberRepository.findById(authHelper.extractMemberId());

        List<Item> itemList = itemRepository.findByMember(memberRepository.findById(authHelper.extractMemberId()));

        System.out.println(itemList.size());
        //2) 해당 아이템들의 라우트 중 가장 마지막 라우트의 상태 경우 나누고 리스트에 담아주기
        for(Item i : itemList){
            List <RouteDto> routeDtoList = RouteDto.toDtoList(
                    routeRepository.findAllWithMemberAndParentByItemIdOrderByParentIdAscNullsFirstRouteIdAsc(i.getId())
            );

            if(routeDtoList.size()>0) {
                String itemworkflowPhase = routeDtoList.get(routeDtoList.size() - 1).getWorkflowPhase();

                ItemTodoResponse itemTodoResponse =
                        new ItemTodoResponse(i.getId(), i.getName(), i.getType(), i.getItemNumber());

                if (itemworkflowPhase.equals("IN_PROGRESS")){

                    IN_PROGRESS.add(itemTodoResponse);

                }else if(itemworkflowPhase.equals("NEED_REVISE")){

                    NEED_REVISE.add(itemTodoResponse);

                }else if(itemworkflowPhase.equals("REJECTED")){

                    REJECTED.add(itemTodoResponse);

                }else if(itemworkflowPhase.equals("WAITING_APPROVAL")){

                    WAITING_APPROVAL.add(itemTodoResponse);

                }

            }
        }
        //3) 이쁜 response 형태로 담아주기 - ItemTodoResponse

        return new ItemTodoResponseList(IN_PROGRESS, NEED_REVISE, REJECTED, WAITING_APPROVAL);

    }

    @Transactional
    public void delete(Long id) {

        Item item = itemRepository.findById(id).orElseThrow(ItemNotFoundException::new);
        deleteImages(item.getThumbnail());

        itemRepository.delete(item);
    }


    private void deleteImages(List<Image> images) {
        images.stream().forEach(i -> fileService.delete(i.getUniqueName()));
    }

    @Transactional
    public ItemUpdateResponse update(Long id, ItemUpdateRequest req) {

        Item item = itemRepository.findById(id).orElseThrow(ItemNotFoundException::new);
        Item.ImageUpdatedResult result = item.update(req);
        uploadImages(result.getAddedImages(), result.getAddedImageFiles());
        deleteImages(result.getDeletedImages());
        return new ItemUpdateResponse(id);
    }

    public ItemListDto readAll(ItemReadCondition cond) {
        return ItemListDto.toDto(
                itemRepository.findAllByCondition(cond)
        );
    }

    /**
     * 파일
     * @param images
     * @param fileImages
     */
    private void uploadFiles(List<Image> images, List<MultipartFile> fileImages) {
        IntStream.range(0, images.size()).forEach(i -> fileService.upload(fileImages.get(i), images.get(i).getUniqueName()));
    }

}