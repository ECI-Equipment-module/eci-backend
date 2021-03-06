//package eci.server.ItemModule.service.item;
//
//import eci.server.DashBoardModule.dto.itemTodo.ItemTodoResponse;
//import eci.server.DashBoardModule.dto.itemTodo.ItemTodoResponseList;
//import eci.server.ItemModule.exception.member.auth.AuthenticationEntryPointException;
//import eci.server.ItemModule.repository.item.ItemMakerRepository;
//import eci.server.ItemModule.repository.item.ItemMaterialRepository;
//import eci.server.ItemModule.repository.newRoute.RouteOrderingRepository;
//
//import eci.server.NewItemModule.dto.newItem.NewItemPagingDto;
//import eci.server.NewItemModule.dto.newItem.NewItemReadCondition;
//import eci.server.NewItemModule.entity.NewItem;
//import eci.server.NewItemModule.entity.NewItemAttachment;
//import eci.server.NewItemModule.repository.attachment.NewItemAttachmentRepository;
//import eci.server.NewItemModule.repository.item.NewItemRepository;
//import eci.server.ProjectModule.repository.project.ProjectRepository;
//import eci.server.config.guard.AuthHelper;
//import eci.server.ItemModule.dto.item.*;
//import eci.server.ItemModule.dto.manufacture.ReadPartNumberService;
//import eci.server.ItemModule.dto.newRoute.routeOrdering.RouteOrderingDto;
//import eci.server.ItemModule.dto.newRoute.routeProduct.RouteProductDto;
//import eci.server.ItemModule.entity.item.Attachment;
//import eci.server.ItemModule.entity.member.Member;
//import eci.server.ItemModule.entity.newRoute.RouteOrdering;
//import eci.server.ItemModule.entity.newRoute.RoutePreset;
//import eci.server.ItemModule.entity.newRoute.RouteProduct;
//import eci.server.ItemModule.entity.newRoute.RouteProductMember;
//import eci.server.ItemModule.exception.item.AttachmentNotFoundException;
//import eci.server.ItemModule.exception.item.ItemUpdateImpossibleException;
//import eci.server.ItemModule.exception.route.RouteNotFoundException;
//import eci.server.ItemModule.repository.color.ColorRepository;
//import eci.server.ItemModule.repository.manufacture.MakerRepository;
//import eci.server.ItemModule.repository.material.MaterialRepository;
//import eci.server.ItemModule.repository.newRoute.RouteProductRepository;
//import eci.server.ItemModule.service.file.FileService;
//import eci.server.ItemModule.entity.item.Image;
////import eci.server.ItemModule.entity.item.Item;
//import eci.server.ItemModule.exception.item.ItemNotFoundException;
//import eci.server.ItemModule.repository.member.MemberRepository;
//import eci.server.ItemModule.service.file.LocalFileService;
//import lombok.RequiredArgsConstructor;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.util.*;
//import java.util.stream.Collectors;
//import java.util.stream.IntStream;
//
//@Service
//@Transactional(readOnly = true)
//@RequiredArgsConstructor
//public class ItemService {
//
//    private final NewItemRepository itemRepository;
//    private final MemberRepository memberRepository;
//    private final ColorRepository colorRepository;
//    private final MaterialRepository materialRepository;
//    private final MakerRepository manufactureRepository;
//    private final NewItemAttachmentRepository attachmentRepository;
//    private final RouteOrderingRepository routeOrderingRepository;
//    private final RouteProductRepository routeProductRepository;
//    private final ItemMakerRepository itemMakerRepository;
//    private final ItemMaterialRepository itemMaterialRepository;
//
//    private final ProjectRepository projectRepository;
//
//    private final ReadPartNumberService readPartNumber;
//
//    private final FileService fileService;
//    private final LocalFileService localFileService;
//
//    private final AuthHelper authHelper;
//
//    private final RoutePreset routePreset;
//
//    /**
//     * ????????? ????????? ?????? save
//     *
//     * @param req
//     * @return ????????? ????????? ??????
//     */
//
//    @Transactional
//    public NewItemCreateResponse tempCreate(ItemTemporaryCreateRequest req) {
//
//        NewItem item = itemRepository.save(
//                ItemTemporaryCreateRequest.toEntity(
//                        req,
//                        memberRepository,
//                        colorRepository,
//                        materialRepository,
//                        manufactureRepository
//                )
//        );
//
//        uploadImages(item.getThumbnail(), req.getThumbnail());
//
//        uploadAttachments(item.getAttachments(), req.getAttachments());
//
//        return new ItemCreateResponse(item.getId());
//    }
//
//
//    /**
//     * ????????? create
//     *
//     * @param req
//     * @return ????????? ????????? ??????
//     */
//
//    @Transactional
//    public ItemCreateResponse create(ItemCreateRequest req) {
//
//        Item item = itemRepository.save(
//                ItemCreateRequest.toEntity(
//                        req,
//                        memberRepository,
//                        colorRepository,
//                        materialRepository,
//                        manufactureRepository
//                )
//
//        );
//
//        uploadImages(item.getThumbnail(), req.getThumbnail());
//        if (!req.getAttachments()==null || req.getAttachments().size()==0 == 0)) {//TODO : ????????? ????????? ?????? (Attachment ?????? ??????)
//            //attachment??? ????????? ??????
//            uploadAttachments(item.getAttachments(), req.getAttachments());
//        }
//
//        return new ItemCreateResponse(item.getId());
//    }
//
//    /**
//     * ????????? ?????? ?????? File Upload??? ????????? ?????????
//     *
//     * @param images
//     * @param fileImages
//     */
//
//    private void uploadImages(List<Image> images, List<MultipartFile> fileImages) {
//        // ?????? ????????? ????????? ????????? ?????? Multipart ?????????
//        // ???????????? ????????? uniquename??? ??????????????? ?????? ??????????????? ?????????
//        IntStream.range(0, images.size())
//                .forEach(
//                        i -> fileService.upload
//                                (
//                                        fileImages.get(i),
//                                        images.get(i).getUniqueName()
//                                )
//                );
//    }
//
//    private void uploadAttachments(List<Attachment> attachments, List<MultipartFile> filedAttachments) {
//        // ?????? ????????? ????????? ????????? ?????? Multipart ?????????
//        // ????????? ????????? uniquename??? ??????????????? ?????? ??????????????? ?????????
//        IntStream.range(0, attachments.size())
//                .forEach(
//                        i -> fileService.upload
//                                (
//                                        filedAttachments.get(i),
//                                        attachments.get(i).getUniqueName()
//                                )
//                );
//    }
//
//    public byte[] readImg(Long id) {
//        Item targetItem = itemRepository.findById(id).orElseThrow(ItemNotFoundException::new);
//        byte[] image = localFileService.getImage(
//                targetItem.getCreatedAt().toString(),
//                targetItem.getThumbnail().get(0).
//                        getUniqueName()
//        );
//        return image;
//    }
//
//    public ReadItemDto read(Long id) {
//        NewItem targetItem = newItemRepository.findById(id).orElseThrow(ItemNotFoundException::new);
//
//        List<AttachmentDto> attachmentDtoList = Optional.ofNullable(AttachmentDto.toDtoList(
//                                attachmentRepository.findByNewItem(targetItem)
//                        )
//                )
//                .orElseThrow(AttachmentNotFoundException::new);
//
//        //???????????? ?????? ???????????? ????????? ????????? ?????? ?????? ?????????,
//        // ????????? ????????? ????????? ????????? ?????????
//        List<RouteOrderingDto> routeDtoList = Optional.ofNullable(
//                RouteOrderingDto.toDtoList(
//                        routeOrderingRepository.findByNewItemOrderByIdAsc(targetItem),
//                        routeProductRepository,
//                        routeOrderingRepository
//                )
//        ).orElseThrow(RouteNotFoundException::new);
//
//        if (routeDtoList.size() > 0) {
//            //???????????? ?????? routeDto??? ????????? ???
//            ReadItemDto readItemDto = ReadItemDto.toDto(
//                    targetItem,
//                    ItemDto.toDto(targetItem),
////                    routeDtoList,
//                    //?????? ?????????
//                    routeDtoList.get(routeDtoList.size() - 1),
//                    //?????? ???????????? ?????? ????????????????????? ????????? ???,
//                    // ???????????? present ???????????? ???????????? ????????? ????????? ??????
//                    RouteProductDto.toDto(
//                            routeProductRepository.findAllByRouteOrdering(
//                                    routeOrderingRepository.findById(
//                                            routeDtoList.get(routeDtoList.size() - 1).getId()
//                                    ).orElseThrow(RouteNotFoundException::new)
//                            ).get(
//                                    routeDtoList.get(routeDtoList.size() - 1).getPresent()
//                            )
//                    )
//                    ,
////                    routeDtoList.
////                            get(routeDtoList.size() - 1),//?????? ?????????,
//
//                    readPartNumber.readPartnumbers(targetItem),
//                    attachmentDtoList
//
//
//            );
//            return readItemDto;
//        } else {
//            //route ??? ???????????? ?????? ???
//            ReadItemDto readItemDto = ReadItemDto.noRoutetoDto(
//                    itemRepository.findById(id).orElseThrow(ItemNotFoundException::new),
//                    ItemDto.toDto(
//                            itemRepository.findById(id).orElseThrow(ItemNotFoundException::new)
//                    ),
//                    routeDtoList,
//                    readPartNumber.readPartnumbers(targetItem),
//                    attachmentDtoList,
//                    routePreset
//
//            );
//
//            System.out.println(readItemDto.getThumbnail().toString());
//
//            return readItemDto;
//        }
//    }
//
//    @Transactional
//    public void delete(Long id) {
//
//        Item item = itemRepository.findById(id).orElseThrow(ItemNotFoundException::new);
//        deleteImages(item.getThumbnail());
//
//        itemRepository.delete(item);
//    }
//
//
//    private void deleteImages(List<Image> images) {
//        images.
//                stream()
//                .forEach(
//                        i -> fileService.delete(
//                                i.getUniqueName()
//                        )
//                );
//    }
//
//    private void deleteAttachments(List<Attachment> attachments) {
////        attachments.stream().forEach(i -> fileService.delete(i.getUniqueName()));
//        attachments.
//                stream().
//                forEach(
//                        i -> i.setDeleted(true)
//                );
//    }
//
//    @Transactional
//    public ItemUpdateResponse update(Long id, ItemUpdateRequest req) {
//
//        Item item = itemRepository.findById(id).orElseThrow(ItemNotFoundException::new);
//
//        if (item.getTempsave() == false) { //true??? ???????????? ??????, false??? ??? ?????? ??????
//            //??? ?????? ???????????? UPDATE ??????, ???????????? ????????? ??????
//            throw new ItemUpdateImpossibleException();
//        }
//
//        Item.FileUpdatedResult result = item.update(
//                req,
//                colorRepository,
//                memberRepository,
//                materialRepository,
//                manufactureRepository,
//                itemMakerRepository,
//                itemMaterialRepository
//                );
//
//        uploadImages(
//                result.getImageUpdatedResult().getAddedImages(),
//                result.getImageUpdatedResult().getAddedImageFiles()
//        );
//        deleteImages(
//                result.getImageUpdatedResult().getDeletedImages()
//        );
//
//        uploadAttachments(
//                result.getAttachmentUpdatedResult().getAddedAttachments(),
//                result.getAttachmentUpdatedResult().getAddedAttachmentFiles()
//        );
//        deleteAttachments(
//                result.getAttachmentUpdatedResult().getDeletedAttachments()
//        );
//
//        return new ItemUpdateResponse(id);
//    }
//
////    public ItemListDto readAll(ItemReadCondition cond) {
////        return ItemListDto.toDto(
////                itemRepository.findAllByCondition(cond)
////        );
////    }
//
//    /**
//     * ??????
//     *
//     * @param images
//     * @param fileImages
//     */
//    private void uploadFiles(List<Image> images, List<MultipartFile> fileImages) {
//        IntStream.range(0, images.size()).forEach(
//                i -> fileService.upload(
//                        fileImages.get(i), images.get(i).getUniqueName()
//                )
//        );
//    }
//
//
//    public ItemTodoResponseList readTodo() {
//        List<ItemTodoResponse> IN_PROGRESS = new ArrayList<>();
//        List<ItemTodoResponse> NEED_REVISE = new ArrayList<>();
//        List<ItemTodoResponse> REJECTED = new ArrayList<>();
//        List<ItemTodoResponse> WAITING_APPROVAL = new ArrayList<>();
//        //0) ?????? ????????? ??? ??????
//        Member member1 = memberRepository.findById(authHelper.extractMemberId()).orElseThrow(
//                AuthenticationEntryPointException::new);
//
//        //1-1 tempsave??? ) ?????? ???????????? ?????? ??????????????? ????????????
//        List<Item> myItemList = itemRepository.findByMember(member1);
//
//        //1-2) ?????? reviewer_id, approver_id??? ????????? ???????????? ??????
//
//        //1-2-1) ?????? ???????????? ?????????
//        List<Item> allItemList = itemRepository.findAll();
//
//        //1-2-2) ?????? ??????????????? ?????? ????????? ??????
//        List<RouteOrdering> liveRouteList = new ArrayList<>();
//        for (Item i : allItemList) {
//            //???????????? ?????? ?????????
//            List<RouteOrdering> itemsAllRoute =
//                    routeOrderingRepository.findByNewItemOrderByIdAsc(i);
//            //?????? ???????????? ?????? ????????? ???????????? ???????????? ???????????? ??? ????????? ??????????????? ??????????????? ??? ????????? ????????? ????????????
//            if (itemsAllRoute.size() > 0) {
//                liveRouteList.add(
//                        itemsAllRoute.get(itemsAllRoute.size() - 1)
//                );
//            }
//        }
//
//        //1-1-1 ) ?????? ????????? ????????? ??? ??????????????? ????????? ??????
//        List<Item> tempsaveList = new ArrayList<>();
//        for (Item item : myItemList) {
//            if (item.getTempsave() == true) {
//                tempsaveList.add(item);
//            }
//        }
//        //1-2-3) Rejected ????????? ??????
//        // ???????????? ????????? ???, present??? ????????? ???????????????
//        // rejcted=true??? member??? member1??? ???????????? ?????????
//        List<Item> rejectedList = new ArrayList<>();
//        // ???????????? ????????? ???, present??? ????????? ??????????????? type
//        // ??? review??? approve??????, ??? ????????? ????????? ????????? ????????? ???
//        List<Item> waitingList = new ArrayList<>();
//        for (RouteOrdering i : liveRouteList) {
//            List<RouteProduct> routeProductList = routeProductRepository.findAllByRouteOrdering(i);
//            RouteProduct targetRouteProduct = routeProductList.get(i.getPresent());
//
//            if (targetRouteProduct.isRejected()) {//present????????? reject??????
//                for (RouteProductMember routeProductMember : targetRouteProduct.getMembers()) {
//                    //rejected??? product ???????????? ?????? ????????? rejct??? ??????
//                    if (routeProductMember.getMember().getId().equals(member1.getId())) {
//                        rejectedList.add(
//                                i.getItem()
//                        );
//                    }
//                }
//            }
//
//            if (
//                    targetRouteProduct.getType().equals("approve") ||
//                            targetRouteProduct.getType().equals("review")
//            ) {
//                for (RouteProductMember routeProductMember : targetRouteProduct.getMembers()) {
//                    //rejected??? product ???????????? ?????? ????????? rejct??? ??????
//                    if (routeProductMember.getMember().getId().equals(member1.getId())) {
//                        waitingList.add(
//                                i.getItem()
//                        );
//                    }
//                }
//            }
//
//
//        }
//
//        //2-1) tempsave ??????
//        if (tempsaveList.size() > 0) {
//            for (Item i : tempsaveList) {
//
//                ItemTodoResponse itemTodoResponse =
//                        new ItemTodoResponse(i.getId(), i.getName(), i.getType(), i.getItemNumber());
//
//                IN_PROGRESS.add(itemTodoResponse);
//            }
//        }
//        //2-2) rejected ??????
//        if (rejectedList.size() > 0) {
//            for (Item i : rejectedList) {
//
//                ItemTodoResponse itemTodoResponse =
//                        new ItemTodoResponse(i.getId(), i.getName(), i.getType(), i.getItemNumber());
//
//                REJECTED.add(itemTodoResponse);
//            }
//        }
//
//        //2-3 ) need review ??????
//        if (waitingList.size() > 0) {
//            for (Item i : waitingList) {
//
//                ItemTodoResponse itemTodoResponse =
//                        new ItemTodoResponse(i.getId(), i.getName(), i.getType(), i.getItemNumber());
//
//                REJECTED.add(itemTodoResponse);
//            }
//        }
//
//        //3) ?????? response ????????? ???????????? - ItemTodoResponse
//
//        return new ItemTodoResponseList(IN_PROGRESS, NEED_REVISE, REJECTED, WAITING_APPROVAL);
//
//    }
//
//    public List<UnlinkedItemDto> linkNeededItem() {
//
//        //0) ?????? ????????? ??? ??????
//        Member member1 = memberRepository.findById(authHelper.extractMemberId()).orElseThrow(
//                AuthenticationEntryPointException::new
//        );
//
//        //1) ?????? ?????? ?????? ????????? ???????????? ?????????
//        List<RouteProduct> routeProductList = routeProductRepository.findAll().stream().filter(
//                rp -> rp.getSequence().equals(
//                        rp.getRouteOrdering().getPresent()
//                )
//        ).collect(Collectors.toList());
//
//        //2) ????????? ??????????????? ??? ????????? ????????? ????????? & ????????? ??????????????? Item(??????) Link(?????????) ??? ???
//        List<RouteProduct> myRouteProductList = new ArrayList<>();
//
//        for (RouteProduct routeProduct : routeProductList) {
//            for (RouteProductMember routeProductMember : routeProduct.getMembers()) {
//                if (routeProductMember.getMember().getId().equals(member1.getId()) &&
//                    routeProduct.getRoute_name().equals("??????????????? Item(??????) Link(?????????)")) {
//                    myRouteProductList.add(routeProduct);
//                    break;
//                }
//
//            }
//        }
//
//        //3) ???????????? ?????? ?????? ?????? ??????
//        HashSet<UnlinkedItemDto> unlinkedItemList = new HashSet<>();
//
//        for (RouteProduct routeProduct : myRouteProductList){
//            if(projectRepository.findByItem(routeProduct.getRouteOrdering().getItem()).size()==0){
//
//                List<Attachment> attachmentList =
//                                        attachmentRepository.findByItem(
//                                                routeProduct.getRouteOrdering().getItem()
//                                        );
//
//                RouteOrdering routeOrdering = routeOrderingRepository.findByNewItemOrderByIdAsc(
//                        routeProduct.getRouteOrdering().getNewItem()
//                ).get(
//                        routeOrderingRepository.findByNewItemOrderByIdAsc(
//                                routeProduct.getRouteOrdering().getNewItem()
//                        ).size()-1
//                );
//
//                ItemDto itemDto = ItemDto.toDto(
//                        routeProduct.getRouteOrdering().getItem()
//                );
//
//                unlinkedItemList.add(
//                        UnlinkedItemDto.toDto(
//                                itemDto
//                                ,
//                                routeOrdering.getLifecycleStatus()
//                                ,
//                                attachmentList
//
//                        )
//                );
//            }
//        }
//
//        List<UnlinkedItemDto> setToListUnlinked  = new ArrayList<>();
//
//        for(UnlinkedItemDto unlinkedItemDto : unlinkedItemList){
//            setToListUnlinked.add(unlinkedItemDto);
//        }
//
//        return setToListUnlinked;
//
//    }
//
//    public List<ItemProjectCreateDto> linkNeededItemsForProject() {
//
//        //0) ?????? ????????? ??? ??????
//        Member member1 = memberRepository.findById(authHelper.extractMemberId()).orElseThrow(
//                AuthenticationEntryPointException::new
//        );
//
//        //1) ?????? ?????? ?????? ????????? ???????????? ?????????
//        List<RouteProduct> routeProductList = routeProductRepository.findAll().stream().filter(
//                rp -> rp.getSequence().equals(
//                        rp.getRouteOrdering().getPresent()
//                )
//        ).collect(Collectors.toList());
//
//        //2) ????????? ??????????????? ??? ????????? ????????? ????????? & ????????? ??????????????? Item(??????) Link(?????????) ??? ???
//        List<RouteProduct> myRouteProductList = new ArrayList<>();
//
//        for (RouteProduct routeProduct : routeProductList) {
//            for (RouteProductMember routeProductMember : routeProduct.getMembers()) {
//                if (routeProductMember.getMember().getId().equals(member1.getId()) &&
//                        routeProduct.getRoute_name().equals("??????????????? Item(??????) Link(?????????)")) {
//                    myRouteProductList.add(routeProduct);
//                    break;
//                }
//
//            }
//        }
//
//        //3) ???????????? ?????? ?????? ?????? ??????
//
//        HashSet<ItemProjectCreateDto> unlinkedItemList = new HashSet<>();
//
//        for (RouteProduct routeProduct : myRouteProductList){
//            if(projectRepository.findByItem(routeProduct.getRouteOrdering().getItem()).size()==0){
//
//                List<NewItemAttachment> attachmentList =
//                        attachmentRepository.findByNewItem(
//                                routeProduct.getRouteOrdering().getNewItem()
//                        );
//
//                RouteOrdering routeOrdering = routeOrderingRepository.findByNewItemOrderByIdAsc(
//                        routeProduct.getRouteOrdering().getNewItem()
//                ).get(
//                        routeOrderingRepository.findByNewItemOrderByIdAsc(
//                                routeProduct.getRouteOrdering().getNewItem()
//                        ).size()-1
//                );
//
//                NewItemPagingDto itemDto = NewItemPagingDto.toDto(
//                        routeProduct.getRouteOrdering().getNewItem()
//                );
//
//                NewItem targetItem = routeProduct.getRouteOrdering().getNewItem();
//
//                unlinkedItemList.add(
//                        ItemProjectCreateDto.toDto(
//                                targetItem,
//                                routeOrderingRepository
//
//                        )
//                );
//            }
//        }
//
//        List<ItemProjectCreateDto> setToListUnlinked  = new ArrayList<>();
//
//        for(ItemProjectCreateDto unlinkedItemDto : unlinkedItemList){
//            setToListUnlinked.add(unlinkedItemDto);
//        }
//
//        return setToListUnlinked;
//
//    }
//
//
//    public List<ItemProjectCreateDto> linkNeededItemsForProjectPage() {
//
//        //0) ?????? ????????? ??? ??????
//        Member member1 = memberRepository.findById(authHelper.extractMemberId()).orElseThrow(
//                AuthenticationEntryPointException::new
//        );
//
//        //1) ?????? ?????? ?????? ????????? ???????????? ?????????
//        List<RouteProduct> routeProductList = routeProductRepository.findAll().stream().filter(
//                rp -> rp.getSequence().equals(
//                        rp.getRouteOrdering().getPresent()
//                )
//        ).collect(Collectors.toList());
//
//        //2) ????????? ??????????????? ??? ????????? ????????? ????????? & ????????? ??????????????? Item(??????) Link(?????????) ??? ???
//        List<RouteProduct> myRouteProductList = new ArrayList<>();
//
//        for (RouteProduct routeProduct : routeProductList) {
//            for (RouteProductMember routeProductMember : routeProduct.getMembers()) {
//                if (routeProductMember.getMember().getId().equals(member1.getId()) &&
//                        routeProduct.getRoute_name().equals("??????????????? Item(??????) Link(?????????)")) {
//                    myRouteProductList.add(routeProduct);
//                    break;
//                }
//
//            }
//        }
//
//        //3) ???????????? ?????? ?????? ?????? ??????
//
//        List<Item> unlinkedItemList = new ArrayList<>();
//
//        for (RouteProduct routeProduct : myRouteProductList){
//            if(projectRepository.findByItem(routeProduct.getRouteOrdering().getItem()).size()==0){
//
//                List<Attachment> attachmentList =
//                        attachmentRepository.findByItem(
//                                routeProduct.getRouteOrdering().getItem()
//                        );
//
//                RouteOrdering routeOrdering = routeOrderingRepository.findByNewItemOrderByIdAsc(
//                        routeProduct.getRouteOrdering().getNewItem()
//                ).get(
//                        routeOrderingRepository.findByNewItemOrderByIdAsc(
//                                routeProduct.getRouteOrdering().getNewItem()
//                        ).size()-1
//                );
//
//                ItemDto itemDto = ItemDto.toDto(
//                        routeProduct.getRouteOrdering().getItem()
//                );
//
//                Item targetItem = routeProduct.getRouteOrdering().getItem();
//
//                unlinkedItemList.add(
//                        targetItem
//                );
//            }
//        }
//
//        List<ItemProjectCreateDto>  itemProjectCreateDtos =
//                unlinkedItemList.stream().map(
//                        i -> ItemProjectCreateDto.toDto(
//                                i,
//                                routeOrderingRepository
//                        )
//                ).collect(Collectors.toList());
//
//        return itemProjectCreateDtos;
//
//    }
//
//    public ItemProjectCreateDtoList readItemCandidatesAll(NewItemReadCondition cond) {
//        return ItemProjectCreateDtoList.toDto(
//                itemRepository.findAllByCondition(cond)
//        );
//    }
//
//}