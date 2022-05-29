package eci.server.ItemModule.service.item;

import eci.server.DashBoardModule.dto.itemTodo.ItemTodoResponse;
import eci.server.DashBoardModule.dto.itemTodo.ItemTodoResponseList;
import eci.server.ItemModule.exception.member.auth.AuthenticationEntryPointException;
import eci.server.ItemModule.repository.item.ItemManufactureRepository;
import eci.server.ItemModule.repository.item.ItemMaterialRepository;
import eci.server.ItemModule.repository.newRoute.RouteOrderingRepository;

import eci.server.ProjectModule.repository.project.ProjectRepository;
import eci.server.config.guard.AuthHelper;
import eci.server.ItemModule.dto.item.*;
import eci.server.ItemModule.dto.manufacture.ReadPartNumberService;
import eci.server.ItemModule.dto.newRoute.routeOrdering.RouteOrderingDto;
import eci.server.ItemModule.dto.newRoute.routeProduct.RouteProductDto;
import eci.server.ItemModule.entity.item.Attachment;
import eci.server.ItemModule.entity.member.Member;
import eci.server.ItemModule.entity.newRoute.RouteOrdering;
import eci.server.ItemModule.entity.newRoute.RoutePreset;
import eci.server.ItemModule.entity.newRoute.RouteProduct;
import eci.server.ItemModule.entity.newRoute.RouteProductMember;
import eci.server.ItemModule.exception.item.AttachmentNotFoundException;
import eci.server.ItemModule.exception.item.ItemUpdateImpossibleException;
import eci.server.ItemModule.exception.route.RouteNotFoundException;
import eci.server.ItemModule.repository.color.ColorRepository;
import eci.server.ItemModule.repository.item.AttachmentRepository;
import eci.server.ItemModule.repository.manufacture.ManufactureRepository;
import eci.server.ItemModule.repository.material.MaterialRepository;
import eci.server.ItemModule.repository.newRoute.RouteProductRepository;
import eci.server.ItemModule.service.file.FileService;
import eci.server.ItemModule.entity.item.Image;
import eci.server.ItemModule.entity.item.Item;
import eci.server.ItemModule.exception.item.ItemNotFoundException;
import eci.server.ItemModule.repository.item.ItemRepository;
import eci.server.ItemModule.repository.member.MemberRepository;
import eci.server.ItemModule.service.file.LocalFileService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {

    public Logger logger = LoggerFactory.getLogger(ItemService.class);

    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;
    private final ColorRepository colorRepository;
    private final MaterialRepository materialRepository;
    private final ManufactureRepository manufactureRepository;
    private final AttachmentRepository attachmentRepository;
    private final RouteOrderingRepository routeOrderingRepository;
    private final RouteProductRepository routeProductRepository;
    private final ItemManufactureRepository itemManufactureRepository;
    private final ItemMaterialRepository itemMaterialRepository;

    private final ProjectRepository projectRepository;

    private final ReadPartNumberService readPartNumber;

    private final FileService fileService;
    private final LocalFileService localFileService;

    private final AuthHelper authHelper;

    private final RoutePreset routePreset;

    /**
     * 아이템 임시로 저장 save
     *
     * @param req
     * @return 생성된 아이템 번호
     */

    @Transactional
    public ItemCreateResponse tempCreate(ItemTemporaryCreateRequest req) {

        Item item = itemRepository.save(
                ItemTemporaryCreateRequest.toEntity(
                        req,
                        memberRepository,
                        colorRepository,
                        materialRepository,
                        manufactureRepository
                )
        );

        uploadImages(item.getThumbnail(), req.getThumbnail());

        uploadAttachments(item.getAttachments(), req.getAttachments());

        return new ItemCreateResponse(item.getId());
    }


    /**
     * 아이템 create
     *
     * @param req
     * @return 생성된 아이템 번호
     */

    @Transactional
    public ItemCreateResponse create(ItemCreateRequest req) {

        Item item = itemRepository.save(
                ItemCreateRequest.toEntity(
                        req,
                        memberRepository,
                        colorRepository,
                        materialRepository,
                        manufactureRepository
                )

        );

        uploadImages(item.getThumbnail(), req.getThumbnail());
        if (!(req.getTag().size() == 0)) {//TODO : 나중에 함수로 빼기 (Attachment 유무 판단)
            //attachment가 존재할 땜나
            uploadAttachments(item.getAttachments(), req.getAttachments());
        }

        return new ItemCreateResponse(item.getId());
    }

    /**
     * 썸네일 존재 시에 File Upload로 이미지 업로드
     *
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

    private void uploadAttachments(List<Attachment> attachments, List<MultipartFile> filedAttachments) {
        // 실제 이미지 파일을 가지고 있는 Multipart 파일을
        // 파일이 가지는 uniquename을 파일명으로 해서 파일저장소 업로드
        IntStream.range(0, attachments.size())
                .forEach(
                        i -> fileService.upload
                                (
                                        filedAttachments.get(i),
                                        attachments.get(i).getUniqueName()
                                )
                );
    }

    public byte[] readImg(Long id) {
        Item targetItem = itemRepository.findById(id).orElseThrow(ItemNotFoundException::new);
        byte[] image = localFileService.getImage(
                targetItem.getCreatedAt().toString(),
                targetItem.getThumbnail().get(0).
                        getUniqueName()
        );
        return image;
    }

    public ReadItemDto read(Long id) {
        Item targetItem = itemRepository.findById(id).orElseThrow(ItemNotFoundException::new);

        List<AttachmentDto> attachmentDtoList = Optional.ofNullable(AttachmentDto.toDtoList(
                                attachmentRepository.findByItem(targetItem)
                        )
                )
                .orElseThrow(AttachmentNotFoundException::new);

        //아이템에 딸린 라우트가 없다면 라우트 없음 에러 던지기,
        // 라우트 없으면 읽기도 사실상 불가능
        List<RouteOrderingDto> routeDtoList = Optional.ofNullable(
                RouteOrderingDto.toDtoList(
                        routeOrderingRepository.findByItem(targetItem),
                        routeProductRepository,
                        routeOrderingRepository
                )
        ).orElseThrow(RouteNotFoundException::new);

        if (routeDtoList.size() > 0) {
            //아이템에 딸린 routeDto가 존재할 때
            ReadItemDto readItemDto = ReadItemDto.toDto(
                    targetItem,
                    ItemDto.toDto(targetItem),
//                    routeDtoList,
                    //최신 라우트
                    routeDtoList.get(routeDtoList.size() - 1),
                    //최신 라우트에 딸린 라우트프로덕트 리스트 중,
                    // 라우트의 present 인덱스에 해당하는 타입을 데리고 오기
                    RouteProductDto.toDto(
                            routeProductRepository.findAllByRouteOrdering(
                                    routeOrderingRepository.findById(
                                            routeDtoList.get(routeDtoList.size() - 1).getId()
                                    ).orElseThrow(RouteNotFoundException::new)
                            ).get(
                                    routeDtoList.get(routeDtoList.size() - 1).getPresent()
                            )
                    )
                    ,
//                    routeDtoList.
//                            get(routeDtoList.size() - 1),//최신 라우트,

                    readPartNumber.readPartnumbers(targetItem),
                    attachmentDtoList


            );
            return readItemDto;
        } else {
            //route 가 존재하지 않을 시
            ReadItemDto readItemDto = ReadItemDto.noRoutetoDto(
                    itemRepository.findById(id).orElseThrow(ItemNotFoundException::new),
                    ItemDto.toDto(
                            itemRepository.findById(id).orElseThrow(ItemNotFoundException::new)
                    ),
                    routeDtoList,
                    readPartNumber.readPartnumbers(targetItem),
                    attachmentDtoList,
                    routePreset

            );

            System.out.println(readItemDto.getThumbnail().toString());

            return readItemDto;
        }
    }

    @Transactional
    public void delete(Long id) {

        Item item = itemRepository.findById(id).orElseThrow(ItemNotFoundException::new);
        deleteImages(item.getThumbnail());

        itemRepository.delete(item);
    }


    private void deleteImages(List<Image> images) {
        images.
                stream()
                .forEach(
                        i -> fileService.delete(
                                i.getUniqueName()
                        )
                );
    }

    private void deleteAttachments(List<Attachment> attachments) {
//        attachments.stream().forEach(i -> fileService.delete(i.getUniqueName()));
        attachments.
                stream().
                forEach(
                        i -> i.setDeleted(true)
                );
    }

    @Transactional
    public ItemUpdateResponse update(Long id, ItemUpdateRequest req) {

        Item item = itemRepository.findById(id).orElseThrow(ItemNotFoundException::new);

        if (item.getTempsave() == false) { //true면 임시저장 상태, false면 찐 저장 상태
            //찐 저장 상태라면 UPDATE 불가, 임시저장 일때만 가능
            throw new ItemUpdateImpossibleException();
        }

        Item.FileUpdatedResult result = item.update(
                req,
                colorRepository,
                memberRepository,
                materialRepository,
                manufactureRepository,
                itemManufactureRepository,
                itemMaterialRepository
                );

        uploadImages(
                result.getImageUpdatedResult().getAddedImages(),
                result.getImageUpdatedResult().getAddedImageFiles()
        );
        deleteImages(
                result.getImageUpdatedResult().getDeletedImages()
        );

        uploadAttachments(
                result.getAttachmentUpdatedResult().getAddedAttachments(),
                result.getAttachmentUpdatedResult().getAddedAttachmentFiles()
        );
        deleteAttachments(
                result.getAttachmentUpdatedResult().getDeletedAttachments()
        );

        return new ItemUpdateResponse(id);
    }

//    public ItemListDto readAll(ItemReadCondition cond) {
//        return ItemListDto.toDto(
//                itemRepository.findAllByCondition(cond)
//        );
//    }

    /**
     * 파일
     *
     * @param images
     * @param fileImages
     */
    private void uploadFiles(List<Image> images, List<MultipartFile> fileImages) {
        IntStream.range(0, images.size()).forEach(
                i -> fileService.upload(
                        fileImages.get(i), images.get(i).getUniqueName()
                )
        );
    }


    public ItemTodoResponseList readTodo() {
        List<ItemTodoResponse> IN_PROGRESS = new ArrayList<>();
        List<ItemTodoResponse> NEED_REVISE = new ArrayList<>();
        List<ItemTodoResponse> REJECTED = new ArrayList<>();
        List<ItemTodoResponse> WAITING_APPROVAL = new ArrayList<>();
        //0) 현재 로그인 된 유저
        Member member1 = memberRepository.findById(authHelper.extractMemberId()).orElseThrow(
                AuthenticationEntryPointException::new);

        //1-1 tempsave용 ) 내가 작성자인 모든 아이템들을 데려오기
        List<Item> myItemList = itemRepository.findByMember(member1);

        //1-2) 내가 reviewer_id, approver_id로 지정된 라우트들 찾기

        //1-2-1) 모든 아이템들 리스트
        List<Item> allItemList = itemRepository.findAll();

        //1-2-2) 모든 아이템들의 최종 라우트 추출
        List<RouteOrdering> liveRouteList = new ArrayList<>();
        for (Item i : allItemList) {
            //아이템의 모든 라우트
            List<RouteOrdering> itemsAllRoute =
                    routeOrderingRepository.findByItem(i);
            //만약 아이템의 모든 라우트 리스트가 하나이상 존재하면 맨 마지막 라우트만이 유효하므로 그 마지막 아이를 데려온다
            if (itemsAllRoute.size() > 0) {
                liveRouteList.add(
                        itemsAllRoute.get(itemsAllRoute.size() - 1)
                );
            }
        }

        //1-1-1 ) 내가 작성한 아이템 중 임시저장된 아이템 담기
        List<Item> tempsaveList = new ArrayList<>();
        for (Item item : myItemList) {
            if (item.getTempsave() == true) {
                tempsaveList.add(item);
            }
        }
        //1-2-3) Rejected 아이템 담기
        // 살아있는 라우트 중, present인 라우트 프로덕트의
        // rejcted=true고 member이 member1의 아이디와 같다면
        List<Item> rejectedList = new ArrayList<>();
        // 살아있는 라우트 중, present인 라우트 프로덕트의 type
        // 이 review나 approve이고, 그 멤버가 나라면 나에게 할당된 것
        List<Item> waitingList = new ArrayList<>();
        for (RouteOrdering i : liveRouteList) {
            List<RouteProduct> routeProductList = routeProductRepository.findAllByRouteOrdering(i);
            RouteProduct targetRouteProduct = routeProductList.get(i.getPresent());

            if (targetRouteProduct.isRejected()) {//present아이가 reject이고
                for (RouteProductMember routeProductMember : targetRouteProduct.getMembers()) {
                    //rejected된 product 작성자에 내가 있다면 rejct에 담기
                    if (routeProductMember.getMember().getId().equals(member1.getId())) {
                        rejectedList.add(
                                i.getItem()
                        );
                    }
                }
            }

            if (
                    targetRouteProduct.getType().equals("approve") ||
                            targetRouteProduct.getType().equals("review")
            ) {
                for (RouteProductMember routeProductMember : targetRouteProduct.getMembers()) {
                    //rejected된 product 작성자에 내가 있다면 rejct에 담기
                    if (routeProductMember.getMember().getId().equals(member1.getId())) {
                        waitingList.add(
                                i.getItem()
                        );
                    }
                }
            }


        }

        //2-1) tempsave 추가
        if (tempsaveList.size() > 0) {
            for (Item i : tempsaveList) {

                ItemTodoResponse itemTodoResponse =
                        new ItemTodoResponse(i.getId(), i.getName(), i.getType(), i.getItemNumber());

                IN_PROGRESS.add(itemTodoResponse);
            }
        }
        //2-2) rejected 추가
        if (rejectedList.size() > 0) {
            for (Item i : rejectedList) {

                ItemTodoResponse itemTodoResponse =
                        new ItemTodoResponse(i.getId(), i.getName(), i.getType(), i.getItemNumber());

                REJECTED.add(itemTodoResponse);
            }
        }

        //2-3 ) need review 추가
        if (waitingList.size() > 0) {
            for (Item i : waitingList) {

                ItemTodoResponse itemTodoResponse =
                        new ItemTodoResponse(i.getId(), i.getName(), i.getType(), i.getItemNumber());

                REJECTED.add(itemTodoResponse);
            }
        }

        //3) 이쁜 response 형태로 담아주기 - ItemTodoResponse

        return new ItemTodoResponseList(IN_PROGRESS, NEED_REVISE, REJECTED, WAITING_APPROVAL);

    }

    public List<UnlinkedItemDto> linkNeededItem() {

        //0) 현재 로그인 된 유저
        Member member1 = memberRepository.findById(authHelper.extractMemberId()).orElseThrow(
                AuthenticationEntryPointException::new
        );

        //1) 현재 진행 중인 라우트 프로덕트 카드들
        List<RouteProduct> routeProductList = routeProductRepository.findAll().stream().filter(
                rp -> rp.getSequence().equals(
                        rp.getRouteOrdering().getPresent()
                )
        ).collect(Collectors.toList());

        //2) 라우트 프로덕트들 중 나에게 할당된 카드들 & 단계가 프로젝트와 Item(제품) Link(설계자) 인 것
        List<RouteProduct> myRouteProductList = new ArrayList<>();

        for (RouteProduct routeProduct : routeProductList) {
            for (RouteProductMember routeProductMember : routeProduct.getMembers()) {
                if (routeProductMember.getMember().getId().equals(member1.getId()) &&
                    routeProduct.getRoute_name().equals("프로젝트와 Item(제품) Link(설계자)")) {
                    myRouteProductList.add(routeProduct);
                    break;
                }

            }
        }

        //3) 프로젝트 링크 안된 애만 담기
        HashSet<UnlinkedItemDto> unlinkedItemList = new HashSet<>();

        for (RouteProduct routeProduct : myRouteProductList){
            if(projectRepository.findByItem(routeProduct.getRouteOrdering().getItem()).size()==0){

                List<Attachment> attachmentList =
                                        attachmentRepository.findByItem(
                                                routeProduct.getRouteOrdering().getItem()
                                        );

                RouteOrdering routeOrdering = routeOrderingRepository.findByItem(
                        routeProduct.getRouteOrdering().getItem()
                ).get(
                        routeOrderingRepository.findByItem(
                                routeProduct.getRouteOrdering().getItem()
                        ).size()-1
                );

                ItemDto itemDto = ItemDto.toDto(
                        routeProduct.getRouteOrdering().getItem()
                );

                unlinkedItemList.add(
                        UnlinkedItemDto.toDto(
                                itemDto
                                ,
                                routeOrdering.getLifecycleStatus()
                                ,
                                attachmentList

                        )
                );
            }
        }

        List<UnlinkedItemDto> setToListUnlinked  = new ArrayList<>();

        for(UnlinkedItemDto unlinkedItemDto : unlinkedItemList){
            setToListUnlinked.add(unlinkedItemDto);
        }

        return setToListUnlinked;

    }

    public List<ItemProjectCreateDto> linkNeededItemsForProject() {

        //0) 현재 로그인 된 유저
        Member member1 = memberRepository.findById(authHelper.extractMemberId()).orElseThrow(
                AuthenticationEntryPointException::new
        );

        //1) 현재 진행 중인 라우트 프로덕트 카드들
        List<RouteProduct> routeProductList = routeProductRepository.findAll().stream().filter(
                rp -> rp.getSequence().equals(
                        rp.getRouteOrdering().getPresent()
                )
        ).collect(Collectors.toList());

        //2) 라우트 프로덕트들 중 나에게 할당된 카드들 & 단계가 프로젝트와 Item(제품) Link(설계자) 인 것
        List<RouteProduct> myRouteProductList = new ArrayList<>();

        for (RouteProduct routeProduct : routeProductList) {
            for (RouteProductMember routeProductMember : routeProduct.getMembers()) {
                if (routeProductMember.getMember().getId().equals(member1.getId()) &&
                        routeProduct.getRoute_name().equals("프로젝트와 Item(제품) Link(설계자)")) {
                    myRouteProductList.add(routeProduct);
                    break;
                }

            }
        }

        //3) 프로젝트 링크 안된 애만 담기

        HashSet<ItemProjectCreateDto> unlinkedItemList = new HashSet<>();

        for (RouteProduct routeProduct : myRouteProductList){
            if(projectRepository.findByItem(routeProduct.getRouteOrdering().getItem()).size()==0){

                List<Attachment> attachmentList =
                        attachmentRepository.findByItem(
                                routeProduct.getRouteOrdering().getItem()
                        );

                RouteOrdering routeOrdering = routeOrderingRepository.findByItem(
                        routeProduct.getRouteOrdering().getItem()
                ).get(
                        routeOrderingRepository.findByItem(
                                routeProduct.getRouteOrdering().getItem()
                        ).size()-1
                );

                ItemDto itemDto = ItemDto.toDto(
                        routeProduct.getRouteOrdering().getItem()
                );

                Item targetItem = routeProduct.getRouteOrdering().getItem();

                unlinkedItemList.add(
                        ItemProjectCreateDto.toDto(
                                targetItem,
                                routeOrderingRepository

                        )
                );
            }
        }

        List<ItemProjectCreateDto> setToListUnlinked  = new ArrayList<>();

        for(ItemProjectCreateDto unlinkedItemDto : unlinkedItemList){
            setToListUnlinked.add(unlinkedItemDto);
        }

        return setToListUnlinked;

    }


    public List<ItemProjectCreateDto> linkNeededItemsForProjectPage() {

        //0) 현재 로그인 된 유저
        Member member1 = memberRepository.findById(authHelper.extractMemberId()).orElseThrow(
                AuthenticationEntryPointException::new
        );

        //1) 현재 진행 중인 라우트 프로덕트 카드들
        List<RouteProduct> routeProductList = routeProductRepository.findAll().stream().filter(
                rp -> rp.getSequence().equals(
                        rp.getRouteOrdering().getPresent()
                )
        ).collect(Collectors.toList());

        //2) 라우트 프로덕트들 중 나에게 할당된 카드들 & 단계가 프로젝트와 Item(제품) Link(설계자) 인 것
        List<RouteProduct> myRouteProductList = new ArrayList<>();

        for (RouteProduct routeProduct : routeProductList) {
            for (RouteProductMember routeProductMember : routeProduct.getMembers()) {
                if (routeProductMember.getMember().getId().equals(member1.getId()) &&
                        routeProduct.getRoute_name().equals("프로젝트와 Item(제품) Link(설계자)")) {
                    myRouteProductList.add(routeProduct);
                    break;
                }

            }
        }

        //3) 프로젝트 링크 안된 애만 담기

        List<Item> unlinkedItemList = new ArrayList<>();

        for (RouteProduct routeProduct : myRouteProductList){
            if(projectRepository.findByItem(routeProduct.getRouteOrdering().getItem()).size()==0){

                List<Attachment> attachmentList =
                        attachmentRepository.findByItem(
                                routeProduct.getRouteOrdering().getItem()
                        );

                RouteOrdering routeOrdering = routeOrderingRepository.findByItem(
                        routeProduct.getRouteOrdering().getItem()
                ).get(
                        routeOrderingRepository.findByItem(
                                routeProduct.getRouteOrdering().getItem()
                        ).size()-1
                );

                ItemDto itemDto = ItemDto.toDto(
                        routeProduct.getRouteOrdering().getItem()
                );

                Item targetItem = routeProduct.getRouteOrdering().getItem();

                unlinkedItemList.add(
                        targetItem
                );
            }
        }

        List<ItemProjectCreateDto>  itemProjectCreateDtos =
                unlinkedItemList.stream().map(
                        i -> ItemProjectCreateDto.toDto(
                                i,
                                routeOrderingRepository
                        )
                ).collect(Collectors.toList());

        return itemProjectCreateDtos;

    }

    public ItemProjectCreateDtoList readItemCandidatesAll(ItemProjectCreateReadCondition cond) {
        return ItemProjectCreateDtoList.toDto(
                itemRepository.findAllByCondition(cond)
        );
    }

}