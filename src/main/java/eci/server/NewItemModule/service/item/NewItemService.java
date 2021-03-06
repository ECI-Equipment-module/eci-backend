package eci.server.NewItemModule.service.item;

import eci.server.BomModule.repository.BomRepository;
import eci.server.DesignModule.dto.DesignContentDto;
import eci.server.DesignModule.repository.DesignRepository;
import eci.server.ItemModule.dto.item.*;
import eci.server.ItemModule.dto.newRoute.routeOrdering.RouteOrderingDto;

import eci.server.ItemModule.entity.item.ItemType;
import eci.server.ItemModule.entity.item.ItemTypes;
import eci.server.ItemModule.entity.member.Member;
import eci.server.ItemModule.entity.newRoute.RouteOrdering;
import eci.server.ItemModule.entity.newRoute.RouteProduct;
import eci.server.ItemModule.entity.newRoute.RouteProductMember;
import eci.server.ItemModule.exception.item.ItemNotFoundException;
import eci.server.ItemModule.exception.item.ItemUpdateImpossibleException;
import eci.server.ItemModule.exception.member.auth.AuthenticationEntryPointException;
import eci.server.ItemModule.exception.route.RouteNotFoundException;
import eci.server.ItemModule.repository.color.ColorRepository;
import eci.server.ItemModule.repository.item.*;

import eci.server.ItemModule.repository.member.MemberRepository;
import eci.server.ItemModule.repository.newRoute.RouteOrderingRepository;
import eci.server.ItemModule.repository.newRoute.RouteProductRepository;
import eci.server.ItemModule.service.file.FileService;
import eci.server.ItemModule.service.file.LocalFileService;
import eci.server.NewItemModule.dto.TempNewItemChildDto;
import eci.server.NewItemModule.dto.newItem.*;
import eci.server.NewItemModule.dto.newItem.create.NewItemCreateRequest;
import eci.server.NewItemModule.dto.newItem.create.NewItemCreateResponse;
import eci.server.NewItemModule.dto.newItem.create.NewItemTemporaryCreateRequest;
import eci.server.NewItemModule.dto.newItem.update.NewItemUpdateRequest;
import eci.server.NewItemModule.entity.*;
import eci.server.NewItemModule.repository.TempNewItemParentChildrenRepository;
import eci.server.NewItemModule.repository.attachment.AttachmentTagRepository;
import eci.server.NewItemModule.repository.attachment.NewItemAttachmentRepository;
import eci.server.NewItemModule.repository.classification.Classification1Repository;
import eci.server.NewItemModule.repository.classification.Classification2Repository;
import eci.server.NewItemModule.repository.classification.Classification3Repository;
import eci.server.NewItemModule.repository.coatingType.CoatingTypeRepository;
import eci.server.NewItemModule.repository.coatingWay.CoatingWayRepository;
import eci.server.NewItemModule.repository.item.NewItemParentChildrenRepository;
import eci.server.NewItemModule.repository.item.NewItemRepository;
import eci.server.NewItemModule.repository.maker.MakerRepository;
import eci.server.NewItemModule.repository.supplier.SupplierRepository;
import eci.server.ProjectModule.repository.carType.CarTypeRepository;
import eci.server.ProjectModule.repository.clientOrg.ClientOrganizationRepository;
import eci.server.ProjectModule.repository.project.ProjectRepository;
import eci.server.config.guard.AuthHelper;
import eci.server.config.guard.DesignGuard;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class NewItemService {

    private final Classification1Repository classification1Repository;
    private final Classification2Repository classification2Repository;
    private final Classification3Repository classification3Repository;
    private final ItemTypesRepository itemTypesRepository;
    private final CarTypeRepository carTypeRepository;
    private final CoatingWayRepository coatingWayRepository;
    private final CoatingTypeRepository coatingTypeRepository;
    private final ClientOrganizationRepository clientOrganizationRepository;
    private final SupplierRepository supplierRepository;
    private final MemberRepository memberRepository;
    private final ColorRepository colorRepository;
    private final MakerRepository makerRepository;
    private final NewItemRepository newItemRepository;
    private final RouteOrderingRepository routeOrderingRepository;
    private final RouteProductRepository routeProductRepository;
    private final ProjectRepository projectRepository;
    private final FileService fileService;
    private final LocalFileService localFileService;
    private final AuthHelper authHelper;
    private final AttachmentTagRepository attachmentTagRepository;
    private final DesignRepository designRepository;
    private final BomRepository bomRepository;
    private final DesignGuard designGuard;
    private final NewItemParentChildrenRepository newItemParentChildrenRepository;

    private final NewItemAttachmentRepository newItemAttachmentRepository;
    private final TempNewItemParentChildrenRepository tempNewItemParentChildrenRepository;


    @Value("${default.image.address}")
    private String defaultImageAddress;
//////////////////////////////////////////////////////////////////////////


    /**
     * ????????? ????????? ?????? save
     *
     * @param req
     * @return ????????? ????????? ??????
     */

    @Transactional
    public NewItemCreateResponse tempCreate(NewItemTemporaryCreateRequest req) {

        NewItem item = newItemRepository.save(
                NewItemTemporaryCreateRequest.toEntity(
                        req,
                        classification1Repository,
                        classification2Repository,
                        classification3Repository,
                        itemTypesRepository,
                        carTypeRepository,
                        coatingWayRepository,
                        coatingTypeRepository,
                        clientOrganizationRepository,
                        supplierRepository,
                        memberRepository,
                        colorRepository,
                        makerRepository,
                        attachmentTagRepository,
                        newItemAttachmentRepository
                )
        );

        if(req.getThumbnail()!=null && req.getThumbnail().getSize()>0) {
            uploadImages(item.getThumbnail(), req.getThumbnail());
        }

        if(!(req.getAttachments()==null || req.getAttachments().size()==0)) {
            uploadAttachments(item.getAttachments(), req.getAttachments());
        }

        return new NewItemCreateResponse(item.getId());
    }



    @Transactional
    public NewItemCreateResponse tempReviseCreate(NewItemTemporaryCreateRequest req, Long targetId) {

        NewItem item = newItemRepository.save(
                NewItemTemporaryCreateRequest.toEntity(
                        req,
                        classification1Repository,
                        classification2Repository,
                        classification3Repository,
                        itemTypesRepository,
                        carTypeRepository,
                        coatingWayRepository,
                        coatingTypeRepository,
                        clientOrganizationRepository,
                        supplierRepository,
                        memberRepository,
                        colorRepository,
                        makerRepository,
                        attachmentTagRepository,
                        newItemAttachmentRepository
                )
        );

        item.setReviseTargetId(targetId);
        item.setReviseTargetNewItem(newItemRepository.findById(targetId).orElseThrow(ItemNotFoundException::new));

        if( item.getItemTypes().getItemType().name().equals("????????????") ||
                item.getItemTypes().getItemType().name().equals("??????????????????")){
            System.out.println("equals ?????? ?????? , ????????? ?????? ??? controller?????? ?????? item??? targetitme ?????? & ?????? ");

            NewItem targetItem = newItemRepository.findById(targetId).orElseThrow(ItemNotFoundException::new);
            NewItemCreateResponse res2 =  item.updateRevisionAndHeritageReleaseCnt
                    (targetItem.getRevision()+1,
                            targetItem.getReleased());

            item.setRevision(targetItem.getRevision()+1);

        }

        if(req.getThumbnail()!=null && req.getThumbnail().getSize()>0) {
            uploadImages(item.getThumbnail(), req.getThumbnail());
        }

        if(!(req.getAttachments()==null || req.getAttachments().size()==0)) {
            uploadAttachments(item.getAttachments(), req.getAttachments());
        }

        return new NewItemCreateResponse(item.getId());
    }



    /**
     * ????????? create
     *
     * @param req
     * @return ????????? ????????? ??????
     */

    @Transactional
    public NewItemCreateResponse create(NewItemCreateRequest req) {

        NewItem item = newItemRepository.save(
                NewItemCreateRequest.toEntity(
                        req,
                        classification1Repository,
                        classification2Repository,
                        classification3Repository,
                        itemTypesRepository,
                        carTypeRepository,
                        coatingWayRepository,
                        coatingTypeRepository,
                        clientOrganizationRepository,
                        supplierRepository,
                        memberRepository,
                        colorRepository,
                        makerRepository,
                        attachmentTagRepository,
                        newItemAttachmentRepository
                )

        );

        if(req.getThumbnail()!=null && req.getThumbnail().getSize()>0) {
            uploadImages(item.getThumbnail(), req.getThumbnail());
            if(!(req.getAttachments()==null || req.getAttachments().size()==0)) {
                //attachment??? ????????? ??????
                uploadAttachments(item.getAttachments(), req.getAttachments());
            }
        } else {
            //TODO 0628 ?????? ????????? ?????? => NONO ??? NULL??? ???????????? DTO?????? ??? ?????? ?????? ????????? ???????????? ???
        }

        item.updateReadOnlyWhenSaved(); //???????????? readonly = true
        saveTrueAttachment(item); //06-17 ??? ????????? ???

        return new NewItemCreateResponse(item.getId());
    }

    //0712

    /**
     * ????????? create
     *
     * @param req
     * @return ????????? ????????? ??????
     */

    @Transactional
    public NewItemCreateResponse reviseCreate(NewItemCreateRequest req, Long targetId) {

        NewItem item = newItemRepository.save(
                NewItemCreateRequest.toEntity(
                        req,
                        classification1Repository,
                        classification2Repository,
                        classification3Repository,
                        itemTypesRepository,
                        carTypeRepository,
                        coatingWayRepository,
                        coatingTypeRepository,
                        clientOrganizationRepository,
                        supplierRepository,
                        memberRepository,
                        colorRepository,
                        makerRepository,
                        attachmentTagRepository,
                        newItemAttachmentRepository
                )

        );

        item.setReviseTargetId(targetId);
        item.setReviseTargetNewItem(newItemRepository.findById(targetId).orElseThrow(ItemNotFoundException::new));

        NewItem targetItem = newItemRepository.findById(targetId).orElseThrow(ItemNotFoundException::new);

        if( item.getItemTypes().getItemType().name().equals("????????????") ||
                item.getItemTypes().getItemType().name().equals("??????????????????")){
            System.out.println("equals ?????? ?????? , ????????? ?????? ??? controller?????? ?????? item??? targetitme ?????? & ?????? ");


            NewItemCreateResponse res2 =  item.updateRevisionAndHeritageReleaseCnt
                    (targetItem.getRevision()+1,
                            targetItem.getReleased());
            item.setRevision(targetItem.getRevision()+1);

        }


        if(req.getThumbnail()!=null && req.getThumbnail().getSize()>0) {
            uploadImages(item.getThumbnail(), req.getThumbnail());

        } else { // 0719 ????????? ???????????? thumbnail ??? ????????? ?????? thumbnail ??? ~
            System.out.println(" ????????? ???????????? thumbnail ??? ????????? ?????? thumbnail ??? ~ ");
            item.setThumbnail(

                    new NewItemImage(
                            targetItem.getThumbnail()==null?
                                    " ":targetItem.getThumbnail().getUniqueName(),
                            targetItem.getThumbnail()==null?
                                    " ":targetItem.getThumbnail().getOriginName(),
                            targetItem.getThumbnail()==null?
                                    defaultImageAddress
                            :targetItem.getThumbnail().getImageaddress(),
                            item
                    )
            );
        }

        if(!(req.getAttachments()==null || req.getAttachments().size()==0)) {
            //attachment??? ????????? ??????
            uploadAttachments(item.getAttachments(), req.getAttachments());
        }

        item.updateReadOnlyWhenSaved(); //???????????? readonly = true
        saveTrueAttachment(item); //06-17 ??? ????????? ???

        return new NewItemCreateResponse(item.getId());
    }


    /**
     * ????????? ?????? ?????? File Upload??? ????????? ?????????
     *
     * @param images
     * @param fileImages
     */

//    private void uploadImages(List<NewItemImage> images, List<MultipartFile> fileImages) {
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

    private void uploadImages(NewItemImage images, MultipartFile fileImages) {
        // ?????? ????????? ????????? ????????? ?????? Multipart ?????????
        // ???????????? ????????? uniquename??? ??????????????? ?????? ??????????????? ?????????
        fileService.upload
                (
                        fileImages,
                        images.getUniqueName()
                );
    }

    private void uploadAttachments(
            List<NewItemAttachment> attachments,
            List<MultipartFile> filedAttachments
    ) {

        List<NewItemAttachment> neededToBeUploaded =
                attachments.stream().filter(
                                documentAttachment -> //duplicate = false ??? ?????? ????????? ??? ??????!
                                        !documentAttachment.isDuplicate()
                        )
                        .collect(Collectors.toList());

        // ?????? ????????? ????????? ????????? ?????? Multipart ?????????
        // ????????? ????????? unique name ??? ??????????????? ?????? ??????????????? ?????????
        IntStream.range(0, neededToBeUploaded.size())
                .forEach(
                        i -> fileService.upload
                                (
                                        filedAttachments.get(i),
                                        neededToBeUploaded.get(i).getUniqueName()
                                )
                );
    }

    public byte[] readImg(Long id) {
        NewItem targetItem = newItemRepository.findById(id).orElseThrow(ItemNotFoundException::new);
        byte[] image = localFileService.getImage(
                targetItem.getCreatedAt().toString(),
                targetItem.getThumbnail().getUniqueName()
        );
        return image;
    }

    public NewItemPagingDtoList readAll(NewItemReadCondition cond) {
        return NewItemPagingDtoList.toDto(
                newItemRepository.findAllByCondition(cond)
        );
    }

    // read one project
    public NewItemDetailDto read(Long id){
        NewItem targetItem = newItemRepository.findById(id).orElseThrow(ItemNotFoundException::new);

        List<RouteOrderingDto> routeDtoList = Optional.ofNullable(
                RouteOrderingDto.toDtoList(
                        routeOrderingRepository.findByNewItemOrderByIdAsc(targetItem),
                        routeProductRepository,
                        routeOrderingRepository,
                        defaultImageAddress

                )
        ).orElseThrow(RouteNotFoundException::new);

        Member currentMember = memberRepository.findById(authHelper.extractMemberId()).orElseThrow(
                AuthenticationEntryPointException::new
        );


        if (routeDtoList.size() > 0) {//???????????? ?????? routeDto??? ????????? ???
            RouteOrdering routeOrdering = routeOrderingRepository
                    .findByNewItemOrderByIdAsc(targetItem).get(
                            routeOrderingRepository
                                    .findByNewItemOrderByIdAsc(targetItem)
                                    .size()-1

                    );
            return NewItemDetailDto.toDto(
                    targetItem,
                    routeOrdering,
                    //?????? ???????????? ?????? ????????????????????? ????????? ???,
                    // ???????????? present ???????????? ???????????? ????????? ????????? ??????
                    routeDtoList.get(routeDtoList.size() - 1),
                    designRepository,
                    bomRepository,
                    routeProductRepository,
                    designGuard,
                    attachmentTagRepository,
                    defaultImageAddress,
                    newItemRepository
            );

        }
        return NewItemDetailDto.noRoutetoDto(
                targetItem,
                routeProductRepository,
                attachmentTagRepository,
                defaultImageAddress,
                newItemRepository
        );
    }


    @Transactional
    public void delete(Long id) {

        NewItem item = newItemRepository.findById(id).orElseThrow(ItemNotFoundException::new);
        deleteImages(item.getThumbnail());

        newItemRepository.delete(item);
    }


    private void deleteImages(NewItemImage images) {
        fileService.delete(
                images.getUniqueName()
        );
    }

    private void deleteAttachments(List<NewItemAttachment> attachments) {
//        attachments.
//                stream().
//                forEach(
//                        i -> fileService.delete(i.getUniqueName())
//                );
        for(NewItemAttachment newItemAttachment:attachments){
            if(!newItemAttachment.isSave()){
                fileService.delete(newItemAttachment.getUniqueName());
            }
        }

    }

    public List<ItemProjectCreateDto> linkNeededItemsForProjectPage() {

        //0) ?????? ????????? ??? ??????
        Member member1 = memberRepository.findById(authHelper.extractMemberId()).orElseThrow(
                AuthenticationEntryPointException::new
        );

        //1) ?????? ?????? ?????? ????????? ???????????? ?????????
        List<RouteProduct> routeProductList = routeProductRepository.findAll().stream().filter(
                rp -> rp.getSequence().equals(
                        rp.getRouteOrdering().getPresent()
                )
        ).collect(Collectors.toList());

        //2) ????????? ??????????????? ??? ????????? ????????? ????????? & ????????? ??????????????? Item(??????) Link(?????????) ??? ???
        List<RouteProduct> myRouteProductList = new ArrayList<>();

        for (RouteProduct routeProduct : routeProductList) {
            for (RouteProductMember routeProductMember : routeProduct.getMembers()) {
                if (routeProductMember.getMember().getId().equals(member1.getId()) &&
                        routeProduct.getRoute_name().equals("??????????????? Item(??????) Link(?????????)")) {
                    myRouteProductList.add(routeProduct);
                    break;
                }

            }
        }

        //3) ???????????? ?????? ?????? ?????? ??????

        List<NewItem> unlinkedItemList = new ArrayList<>();

        for (RouteProduct routeProduct : myRouteProductList){
            if(projectRepository.findByNewItemOrderByIdAsc(routeProduct.getRouteOrdering().getNewItem()).size()==0){

                RouteOrdering routeOrdering = routeOrderingRepository.findByNewItemOrderByIdAsc(
                        routeProduct.getRouteOrdering().getNewItem()
                ).get(
                        routeOrderingRepository.findByNewItemOrderByIdAsc(
                                routeProduct.getRouteOrdering().getNewItem()
                        ).size()-1
                );

                NewItem targetItem = routeProduct.getRouteOrdering().getNewItem();

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



    @Transactional
    public ItemUpdateResponse update(Long id, NewItemUpdateRequest req) {

        NewItem item = newItemRepository.findById(id).orElseThrow(ItemNotFoundException::new);

        if (!item.isTempsave()) { //true??? ???????????? ??????, false??? ??? ?????? ??????
            //??? ?????? ???????????? UPDATE ??????, ???????????? ????????? ??????
            throw new ItemUpdateImpossibleException();
        }

        List<Long> oldTags = produceOldNewTagComment(item, req).getOldTag();
        List<Long> newTags = produceOldNewTagComment(item, req).getNewTag();
        List<String> oldComment = produceOldNewTagComment(item, req).getOldComment();
        List<String> newComment =produceOldNewTagComment(item, req).getNewComment();
        List<NewItemAttachment> targetAttachmentsForTagAndComment
                = produceOldNewTagComment(item, req).getTargetAttachmentsForTagAndComment();

            ///////////////////////////
        NewItem.NewItemFileUpdatedResult result = item.update(
                req,
                colorRepository,
                memberRepository,
                clientOrganizationRepository,
                supplierRepository,
                makerRepository,
                itemTypesRepository,
                coatingWayRepository,
                coatingTypeRepository,
                carTypeRepository,
                attachmentTagRepository,

                classification1Repository,
                classification2Repository,
                classification3Repository,

                oldTags ,//oldTag
                newTags, // new tag
                oldComment, //?????? co
                newComment,// new co

                targetAttachmentsForTagAndComment // old tag, comment ???????????? ?????????
        );


        if(
                result.getImageUpdatedResult()!=null &&
                        result.getImageUpdatedResult().getAddedImages()!=null
        ){

            uploadImages(
                    result.getImageUpdatedResult().getAddedImages(),
                    result.getImageUpdatedResult().getAddedImageFiles()
            );
            deleteImages(
                    result.getImageUpdatedResult().getAddedImages()
            );
        }

        uploadAttachments(
                result.getAttachmentUpdatedResult().getAddedAttachments(),
                result.getAttachmentUpdatedResult().getAddedAttachmentFiles()
        );
        deleteAttachments(
                result.getAttachmentUpdatedResult().getDeletedAttachments()
        );

        //update ??? ?????? routeId??? ????????? ?????????
        Long routeId = -1L;
        if(routeOrderingRepository.findByNewItemOrderByIdAsc(item).size()>0) {
            RouteOrdering routeOrdering = routeOrderingRepository.findByNewItemOrderByIdAsc(item).get
                    (
                            routeOrderingRepository.findByNewItemOrderByIdAsc(item).size()-1
                    );
            routeId = routeOrdering.getId();
        }

        return new ItemUpdateResponse(id, routeId);
    }

    @Transactional
    public ItemUpdateResponse tempEnd(Long id, NewItemUpdateRequest req) {

        NewItem item = newItemRepository.findById(id).orElseThrow(ItemNotFoundException::new);

        if (!item.isTempsave() || item.isReadonly()) {
            //tempsave??? false??? ??? ?????? ??????
            //??? ?????? ???????????? UPDATE ??????, ???????????? ????????? ??????
            //readonly??? true?????? ?????? ????????????
            throw new ItemUpdateImpossibleException();
        }

        // (2) co??? affected item ??? revise_progress = false

//    List<NewItem> affectedItems =
//            changeOrder.getCoNewItems().stream().map(
//                    m->m.getNewItem()
//            ).collect(Collectors.toList());
//
//                    for(NewItem affectedItem : affectedItems){
//        affectedItem.newItem_revise_progress_done_when_co_confirmed();
//    }
//
//                    newItemService.ReviseItem(affectedItems, changeOrder.getModifier());

        List<Long> oldTags = produceOldNewTagComment(item, req).getOldTag();
        List<Long> newTags = produceOldNewTagComment(item, req).getNewTag();
        List<String> oldComment = produceOldNewTagComment(item, req).getOldComment();
        List<String> newComment =produceOldNewTagComment(item, req).getNewComment();
        List<NewItemAttachment> targetAttachmentsForTagAndComment
                = produceOldNewTagComment(item, req).getTargetAttachmentsForTagAndComment();


        NewItem.NewItemFileUpdatedResult result = item.tempEnd(
                req,
                colorRepository,
                memberRepository,
                clientOrganizationRepository,
                supplierRepository,
                makerRepository,
                itemTypesRepository,
                coatingWayRepository,
                coatingTypeRepository,
                carTypeRepository,
                attachmentTagRepository,

                classification1Repository,
                classification2Repository,
                classification3Repository,

                oldTags ,
                newTags,
                oldComment,
                newComment,

                targetAttachmentsForTagAndComment
        );

        if(
                result.getImageUpdatedResult()!=null &&
                        result.getImageUpdatedResult().getAddedImages()!=null
        ){
            uploadImages(
                    result.getImageUpdatedResult().getAddedImages(),
                    result.getImageUpdatedResult().getAddedImageFiles()
            );
            deleteImages(
                    result.getImageUpdatedResult().getAddedImages()
            );
        }


        uploadAttachments(
                result.getAttachmentUpdatedResult().getAddedAttachments(),
                result.getAttachmentUpdatedResult().getAddedAttachmentFiles()
        );
        deleteAttachments(
                result.getAttachmentUpdatedResult().getDeletedAttachments()
        );

        saveTrueAttachment(item);

        Long routeId = -1L;
        if(routeOrderingRepository.findByNewItemOrderByIdAsc(item).size()>0) {
            RouteOrdering routeOrdering = routeOrderingRepository.findByNewItemOrderByIdAsc(item).get
                    (
                            routeOrderingRepository.findByNewItemOrderByIdAsc(item).size()-1
                    );
            routeId = routeOrdering.getId();
        }

        return new ItemUpdateResponse(id, routeId);

    }

    public List<NewItemChildDto> readChildAll(Long id) {

        return NewItemChildDto.toDtoList(
                newItemParentChildrenRepository.
                        findAllWithParentByParentId(id),//ByParentIdOrderByParentIdAscNullsFirst(
                newItemParentChildrenRepository,
                defaultImageAddress

        );

    }

    public List<NewItemParentDto> readParentAll(Long id) {

        return NewItemParentDto.toDtoList(
                newItemParentChildrenRepository.
                        findAllWithChildByChildId(id),//ByParentIdOrderByParentIdAscNullsFirst(
                newItemParentChildrenRepository,
                defaultImageAddress

        );

    }

    public NewItemParentDto topTreeAndItsParents(Long id){
        NewItem newItem = newItemRepository.findById(id).orElseThrow(ItemNotFoundException::new);
        NewItemParentDto result = NewItemParentDto.toTopDto(newItem, defaultImageAddress);
        result.setChildren(readParentAll(id));
        return result;
    }

    public List<TempNewItemChildDto> readDevChildAll(Long id) {

        return TempNewItemChildDto.toDtoList(
                id,
                tempNewItemParentChildrenRepository.
                        findAllWithParentByParentId(id),//ByParentIdOrderByParentIdAscNullsFirst(
                tempNewItemParentChildrenRepository,
                newItemRepository

        );

    }



    /**
     * ?????? ??? ????????? complete ??? release ?????????
     * ?????? ?????? ??? ????????? ???????????? NewItem - dev bom ?????????
     * @return
     */
    public List<NewItem> readDevBomItems() {

        //1) ????????? ??? (????????? complete, release ??? ??????)

        // 1-1 ) ?????? ?????? ????????????
        ItemType itemType1 = ItemType.??????????????????;
        ItemType itemType2 = ItemType.????????????;
        ItemTypes productItemTypes1 = itemTypesRepository.findByItemType(itemType1);
        ItemTypes productItemTypes2 = itemTypesRepository.findByItemType(itemType2);
        List<ItemTypes> itemTypes = new ArrayList<>();
        itemTypes.add(productItemTypes1);
        itemTypes.add(productItemTypes2);

        List<NewItem> itemListProduct = newItemRepository.findByItemTypes(itemTypes);

        //1-2) ????????? release ??? complete??? ?????? ?????? ????????? ?????? ??????
        //0723 plus -> revise ????????? ??????, ?????? ?????? revise item ????????? ????????? ??????.
        List<NewItem> finalProducts = new ArrayList<>();

        for(NewItem newItem : itemListProduct){
            if(
                    routeOrderingRepository.findByNewItemOrderByIdAsc(newItem).size()>0
                            && (routeOrderingRepository.findByNewItemOrderByIdAsc(newItem).get(
                            routeOrderingRepository.findByNewItemOrderByIdAsc(newItem).size()-1
                    ).getLifecycleStatus().equals("COMPLETE") ||
                            (routeOrderingRepository.findByNewItemOrderByIdAsc(newItem).get(
                                    routeOrderingRepository.findByNewItemOrderByIdAsc(newItem).size()-1
                            ).getLifecycleStatus().equals("RELEASE")
                            )
                    )
            ){
                //????????? ?????? ?????? revise ?????? ????????? ?????????.
                if(!newItem.isRevise_progress() ) {
                    if(newItem.getReviseTargetId()!=null){
                        NewItem targetNewItem = newItemRepository.findById(newItem.getReviseTargetId())
                                .orElseThrow(ItemNotFoundException::new);

                        if(!targetNewItem.isRevise_progress()){
                            finalProducts.add(newItem);
                        }
                    }
                    else {
                        finalProducts.add(newItem);
                    }
                }

            }
        }

        // 1-3 ) ?????? ????????? ?????? ????????? revise ?????? ?????????~(?????? revise) ??? ????????????
        List finalRecentReviseItemList = new ArrayList();

        for(NewItem newItem : finalProducts){
            if(newItemRepository.findByReviseTargetNewItem(newItem)==null){
                finalRecentReviseItemList.add(newItem);
            }
        }

        // 2) ?????? ?????? ??? (temp save??? false??? ?????????)

        List<ItemType> itemTypeList = new ArrayList<>();
        itemTypeList.add(ItemType.?????????????????????);
        itemTypeList.add(ItemType.??????);
        itemTypeList.add(ItemType.?????????);
        itemTypeList.add(ItemType.?????????????????????);
        itemTypeList.add(ItemType.?????????);
        itemTypeList.add(ItemType.???????????????);

        List<ItemTypes> elseItemTypes = new ArrayList<>();
        elseItemTypes.add(itemTypesRepository.findByItemType(itemTypeList.get(0)));
        elseItemTypes.add(itemTypesRepository.findByItemType(itemTypeList.get(1)));
        elseItemTypes.add(itemTypesRepository.findByItemType(itemTypeList.get(2)));
        elseItemTypes.add(itemTypesRepository.findByItemType(itemTypeList.get(3)));
        elseItemTypes.add(itemTypesRepository.findByItemType(itemTypeList.get(4)));
        elseItemTypes.add(itemTypesRepository.findByItemType(itemTypeList.get(5)));

        List<NewItem> itemListElse = newItemRepository.findByItemTypes(elseItemTypes);

        //????????? revise ?????? ?????????
        Set<NewItem> semiFinalItemElseItems = new HashSet<>();

        for(NewItem newItem : itemListElse){

            if(!newItem.isRevise_progress() ) {
                if(newItem.getReviseTargetId()!=null){
                    NewItem targetNewItem = newItemRepository.findById(newItem.getReviseTargetId())
                            .orElseThrow(ItemNotFoundException::new);

                    if(!targetNewItem.isRevise_progress()){
                        semiFinalItemElseItems.add(newItem);
                    }
                }
                else {
                    semiFinalItemElseItems.add(newItem);
                }
            }
        }

        // revise ????????? ???????????? ????????? ??? ??????.

        List finalElseItemList = new ArrayList();

        for(NewItem newItem : semiFinalItemElseItems){
            if(newItemRepository.findByReviseTargetNewItem(newItem)==null){
                finalElseItemList.add(newItem);
            }
        }


        //?????? ?????? ????????? ?????????
        finalElseItemList.addAll(finalRecentReviseItemList);
        //????????? ?????? ????????? ?????? ????????? ?????????

        return finalElseItemList;
    }

    private void saveTrueAttachment(NewItem target) {
        newItemAttachmentRepository.findByNewItem(target).
                forEach(
                        i->i.setSave(true)
                );

    }



    /**
     * ?????? ??? ????????? complete ??? release ??? ????????? ???????????? - compare bom ???
     * - ???????????? : compare bom ??? ?????? ???????????? ??? ?????? ???
     * @return
     */
    public List<NewItem> readCompareBomItems() {

        //1) ????????? ??? (????????? complete, release ??? ??????)

        // 1-1 ) ?????? ?????? ????????????
        ItemType itemType1 = ItemType.??????????????????;
        ItemType itemType2 = ItemType.????????????;
        ItemTypes productItemTypes1 = itemTypesRepository.findByItemType(itemType1);
        ItemTypes productItemTypes2 = itemTypesRepository.findByItemType(itemType2);
        List<ItemTypes> itemTypes = new ArrayList<>();
        itemTypes.add(productItemTypes1);
        itemTypes.add(productItemTypes2);

        List<NewItem> itemListProduct = newItemRepository.findByItemTypes(itemTypes);

//        //1-2) ????????? release ??? complete??? ?????? ?????? ????????? ?????? ??????
//        List<NewItem> finalProducts = new ArrayList<>();
//
//        for(NewItem newItem : itemListProduct){
//            if(
//                    routeOrderingRepository.findByNewItemOrderByIdAsc(newItem).size()>0
//                            && (routeOrderingRepository.findByNewItemOrderByIdAsc(newItem).get(
//                            routeOrderingRepository.findByNewItemOrderByIdAsc(newItem).size()-1
//                    ).getLifecycleStatus().equals("COMPLETE") ||
//                            (routeOrderingRepository.findByNewItemOrderByIdAsc(newItem).get(
//                                    routeOrderingRepository.findByNewItemOrderByIdAsc(newItem).size()-1
//                            ).getLifecycleStatus().equals("RELEASE")
//                            )
//                    )
//            ){
//                finalProducts.add(newItem);
//            }
//        }

        //1-2) revise ??? ?????????, ?????? revise ??? ???????????? !
        List<NewItem> finalProducts = new ArrayList<>();

        for(NewItem newItem : itemListProduct){
            if(!newItem.isRevise_progress() ) {
                if(newItem.getReviseTargetId()!=null){
                    NewItem targetNewItem = newItemRepository.findById(newItem.getReviseTargetId())
                            .orElseThrow(ItemNotFoundException::new);

                    if(!targetNewItem.isRevise_progress()){
                        finalProducts.add(newItem);
                    }
                }
                else {
                    finalProducts.add(newItem);
                }
            }
        }

        // 1-3) ??????????????? ?????? revise ??? ?????? , ?????? revise ??? ?????? ????????? ?????? ?????? ?????? ?????????
        //List affectedItemList = new ArrayList(affectedItems);

        List finalReturnItemList = new ArrayList();

        for(NewItem newItem : finalProducts){
            if(newItemRepository.findByReviseTargetNewItem(newItem)==null){
                finalReturnItemList.add(newItem);
            }
        }

        return finalReturnItemList;
    }

    /**
     * affected items
     * ?????? ??? ????????? complete ??? release ??? ????????? ???????????? - compare bom ???
     *  ?????? complete, release, ????????? - revise_progress=false ??? ????????????
     * & ?????? revise ?????? ??? ?????????, ?????? revise ????????? ????????? ?????????
     * @return
     */
    public List<NewItem> readAffectedItems() {


        List<NewItem> itemListProduct = newItemRepository.findAll();

        //1-2) ????????? release ??? complete??? ?????? ?????? ????????? ?????? ??????
        List<NewItem> finalProducts = new ArrayList<>();

        for(NewItem newItem : itemListProduct){
            if(
                    routeOrderingRepository.findByNewItemOrderByIdAsc(newItem).size()>0
                            && (routeOrderingRepository.findByNewItemOrderByIdAsc(newItem).get(
                            routeOrderingRepository.findByNewItemOrderByIdAsc(newItem).size()-1
                    ).getLifecycleStatus().equals("COMPLETE") ||
                            (routeOrderingRepository.findByNewItemOrderByIdAsc(newItem).get(
                                    routeOrderingRepository.findByNewItemOrderByIdAsc(newItem).size()-1
                            ).getLifecycleStatus().equals("RELEASE")
                            )
                    )
            ){
                finalProducts.add(newItem);
            }
        }

        Set<NewItem> affectedItems = new HashSet<>();

        // ?????? COMPLETE/RELEASE ??? ????????? ??? ?????? REVISE ?????? ?????? ?????? ???
        for(NewItem newItem : finalProducts){

            if(!newItem.isRevise_progress() ) {
                if(newItem.getReviseTargetId()!=null){
                    NewItem targetNewItem = newItemRepository.findById(newItem.getReviseTargetId())
                            .orElseThrow(ItemNotFoundException::new);

                    if(!targetNewItem.isRevise_progress()){
                        affectedItems.add(newItem);
                    }
                }
                else {
                    affectedItems.add(newItem);
                }
            }
        }

        // ??????????????? ?????? revise ??? ?????? , ?????? revise ??? ?????? ????????? ?????? ?????? ?????? ?????????
    //List affectedItemList = new ArrayList(affectedItems);

        List finalAffectedItemList = new ArrayList();

        for(NewItem newItem : affectedItems){
            if(newItemRepository.findByReviseTargetNewItem(newItem)==null){
                finalAffectedItemList.add(newItem);
            }
        }

        return finalAffectedItemList;
    }

    /**
     * Release ??? ???????????? ?????????
     * release, complete ??? ?????? ??? release ??? 0 ??? ?????? (?????? ??? 1.0?????? ?????? ??????)
     * @return
     */
    public List<NewItem> releaseItem(){
        List<NewItem> affItemList = readAffectedItems();
        List<NewItem> releaseItemList = new ArrayList<>();

        for(NewItem newItem : affItemList){
            if (newItem.getReleased()==0){
                releaseItemList.add(newItem);
            }
        }
//        List<NewItem> releaseItem = affItemList.stream().filter(
//                i->i.getRelease()==0
//        ).collect(Collectors.toList());

        return releaseItemList;

    }
    /**
     * createDevelopmentCard ?????? ?????? ???

     */
    public void recursiveChildrenMaking(
            NewItem parentNewItem,
            List<DesignContentDto> childrenList,
            NewItemParentChildrenRepository newItemParentChildrenRepository,
            NewItemRepository newItemRepository
    ) {

        if (childrenList!=null && childrenList.size() > 0) {
            for (DesignContentDto p : childrenList) {

                NewItem children =
                        newItemRepository.findByItemNumber(p.getCardNumber());

                // ????????? ?????? ?????? ????????? ~ ??? ?????? ??????
                NewItemParentChildrenId newItemParentChildrenId = new NewItemParentChildrenId(
                        parentNewItem,
                        newItemRepository.findByItemNumber(p.getCardNumber())

                );

                // 06-25 newParentItemId ???

                Long newId = Long.parseLong((parentNewItem.getId().toString()+
                        children.getId().toString()));

                newItemParentChildrenRepository.save(
                        new NewItemParentChildren(
                                newId,
                                parentNewItem,
                                children
                        )
                );


                if (
                        childrenList.get(
                                childrenList.indexOf(p)
                        )!=null
                                &&
                                childrenList.get(
                                        childrenList.indexOf(p)
                                ).getChildren()!=null
                                &&
                                childrenList.get(
                                        childrenList.indexOf(p)
                                ).getChildren().size() > 0
                ) {

                    recursiveChildrenMaking(
                            newItemRepository.findByItemNumber(p.getCardNumber()),
                            childrenList.get(
                                    childrenList.indexOf(p)
                            ).getChildren(),
                            newItemParentChildrenRepository,
                            newItemRepository
                    );
                }

            }
        }
    }

    /**
     * route ??? co ???????????? ??? ?????? ??????
     * @param newItems // affected item List
     */
    public void ReviseItem(List<NewItem> newItems){
        System.out.println("item ??? revise_progress=true ?????? ?????????. ");
        for(NewItem newItem : newItems){
            System.out.println(newItem.getId() + "???????????? revise ?????? ????????? ~~~ ");
            newItem.setRevise_progress(true);
            // revise ?????? ??? ?????????
            //newItem.setModifier(changeOrderRequestPerson);
            // ????????? ????????? ??? ?????? modifier ??? ????????? ??????
            //newItem.setReadonly(false); newItem.setTempsave(true); // ????????? ?????? ?????? ????????? ??????
        }
    }

    /**
     * revise ?????? ?????? , true ??? revise ?????? , false ??? revise ?????? ???
     * @param affectedItems //affected item List
     */
    public boolean checkReviseCompleted(List<NewItem> affectedItems){

        int affectedItemSize = affectedItems.size();
        int revisedItemSize = 0;

        for(NewItem newItem : affectedItems){
            System.out.println(newItem.getId());
            if(!newItem.isRevise_progress()){ //revise progress ??? ???????????? (revise ????????????)

                revisedItemSize+=1;
            }

        }

        return (affectedItemSize==revisedItemSize); //?????? ????????? ????????? ??????, ???????????? ?????? ???
    }

    /**
     * ????????? update ????????? ????????? ????????? revision ??? update ?????? !
     */
    public void revisionUpdateAllChildrenAndParentItem(NewItem newItem){

        List<NewItemParentChildren> newItemParentChildren =
                newItemParentChildrenRepository.findAllWithParentByParentId(newItem.getId());

        newItemParentChildren.stream().forEach(
                newItemParentChildren1 -> newItemParentChildren1.getChildren().updateRevision()
        );

        newItem.getParent().stream().forEach(
                newItemParentChildren2 -> newItemParentChildren2.getParent().updateRevision()

        );

    }

    /**
     * target Id??? url ??? ???????????? ??????
     * @param targetId
     * @param newItemId
     */
    public NewItemCreateResponse registerTargetReviseItem(Long targetId, Long newItemId){

        // 1. ?????? ?????? (???????????? ?????????) - target_id ??? revise_id ??? ?????? ?????? ????????? ?????? ??????
        //(1)

        System.out.println("register target revise item rrrrrrrrrrrrrrr rrrrrr");

        NewItem newItemForRevise = newItemRepository.findById(newItemId).orElseThrow(ItemNotFoundException::new);
        // (2) ?????? ????????? ????????? ?????????
        NewItem targetNewItem  = newItemRepository.findById(targetId).orElseThrow(ItemNotFoundException::new);
        // 2. ????????? ???????????? ???????????? ?????? ?????? -> ?????? ???????????? ????????????????????????????????? ITEM_REVIEW ????????? ??? ?????? ?????? ??????
        // ????????? ??????(new item) ?????? ??????????????? ????????? ????????? ????????? revision update ??????
        // ?????? ????????? ????????? updateRevision ????????? ????????? ????????? ??? ! (????????? ?????? ???????????? revise o o )
        if(newItemForRevise.getItemTypes().getItemType().name().equals("????????????") ||
                newItemForRevise.getItemTypes().getItemType().name().equals("??????????????????")){
            System.out.println("equals ?????? ?????? , ????????? ?????? ??? controller?????? ?????? item??? targetitme ?????? & ?????? ");

            NewItem targetItem = newItemRepository.findById(targetId).orElseThrow(ItemNotFoundException::new);
            NewItemCreateResponse res3 = newItemForRevise.updateRevisionAndHeritageReleaseCnt(
                    targetItem.getRevision()+1,
                    targetItem.getReleased());
            //newItemForRevise.setRevision(targetItem.getRevision()+1);

        }

        return new NewItemCreateResponse(targetId);
    }

    public void registerReviseIdPlease(NewItem newItem, NewItem willBeRegistered){
        //?????? ??? ??????;;
        System.out.println("?????? ??? ?????? ,,,, ");
        newItem.saving_target_revise_item(willBeRegistered);
        newItem.setReviseTargetNewItem(willBeRegistered);
    }
    ///////////////////////////////////
    @Getter
    @AllArgsConstructor
    public static class OldNewTagCommentUpdatedResult {
        private List<Long> oldTag;
        private List<Long> newTag;
        private List<String> oldComment;
        private List<String> newComment;

        private List<NewItemAttachment> targetAttachmentsForTagAndComment;
    }

    public OldNewTagCommentUpdatedResult produceOldNewTagComment(
            NewItem item, //update ????????? ?????????
            NewItemUpdateRequest req
    ) {
        List<Long> oldDocTag = new ArrayList<>();
        List<Long> newDocTag = new ArrayList<>();
        List<String> oldDocComment = new ArrayList<>();
        List<String> newDocComment = new ArrayList<>();

        List<NewItemAttachment> newItemAttachments
                = item.getAttachments();

        List<NewItemAttachment> targetAttachmentsForTagAndComment = new ArrayList<>();

        // OLD TAG, NEW TAG, COMMENT OLD NEW GENERATE
        if (newItemAttachments.size() > 0) {
            // ?????? ?????? ?????? ??????(?????? ??????, ????????? ????????? ???) == ?????? deleted ????????? - deleted=true ??? ??????

            // ????????? ??? ?????????
            List<NewItemAttachment> oldAttachments = item.getAttachments();
            for (NewItemAttachment attachment : oldAttachments) {

                if (
                        (!attachment.isDeleted())
                                // (1) ????????? ?????? : DELETED = FALSE (??????, ????????? ?????? ???????????? ?????? ??????????????? ??????)
                                 ) {
                    if(req.getDeletedAttachments() != null){ // (1-1) ?????? ???????????? delete ??? ???????????? ???????????????
                        if(!(req.getDeletedAttachments().contains(attachment.getId()))){
                            // ??? delete ?????? ??? attachment ????????? ???????????? ?????? ?????? ??????
                            targetAttachmentsForTagAndComment.add(attachment);
                        }
                    }
                    else{ //??? ????????? delete ????????? ????????? ??? ???????????? ??????
                        targetAttachmentsForTagAndComment.add(attachment);
                    }

                }

            }

            int standardIdx = targetAttachmentsForTagAndComment.size();

            oldDocTag.addAll(req.getAddedTag().subList(0, standardIdx));
            newDocTag.addAll(req.getAddedTag().subList(standardIdx, req.getAddedTag().size()));

            oldDocComment.addAll(req.getAddedAttachmentComment().subList(0, standardIdx));
            newDocComment.addAll(req.getAddedAttachmentComment().subList(standardIdx, req.getAddedTag().size()));

        }
        return new OldNewTagCommentUpdatedResult(

                oldDocTag,
                newDocTag,
                oldDocComment,
                newDocComment,

                targetAttachmentsForTagAndComment
        );
    }
}
