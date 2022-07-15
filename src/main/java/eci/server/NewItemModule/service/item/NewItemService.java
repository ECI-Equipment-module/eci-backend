package eci.server.NewItemModule.service.item;

import eci.server.BomModule.repository.BomRepository;
import eci.server.BomModule.repository.PreliminaryBomRepository;
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
    private final PreliminaryBomRepository preliminaryBomRepository;
    private final NewItemParentChildrenRepository newItemParentChildrenRepository;

    private final NewItemAttachmentRepository newItemAttachmentRepository;
    private final TempNewItemParentChildrenRepository tempNewItemParentChildrenRepository;


    @Value("${default.image.address}")
    private String defaultImageAddress;
//////////////////////////////////////////////////////////////////////////


    /**
     * 아이템 임시로 저장 save
     *
     * @param req
     * @return 생성된 아이템 번호
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
                        attachmentTagRepository
                )
        );

        if(req.getThumbnail()!=null && req.getThumbnail().getSize()>0) {
            uploadImages(item.getThumbnail(), req.getThumbnail());
        }

        if(req.getTag().size()>0) {
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
                        attachmentTagRepository
                )
        );

        item.setReviseTargetId(targetId);

        if( item.getItemTypes().getItemType().name().equals("파트제품") ||
                item.getItemTypes().getItemType().name().equals("프로덕트제품")){
            System.out.println("equals 제품 이면 , 아이템 만들 때 controller에서 바로 item에 targetitme 등록 & 개정 ");

            NewItem targetItem = newItemRepository.findById(targetId).orElseThrow(ItemNotFoundException::new);
            NewItemCreateResponse res2 =  item.updateRevision(targetItem.getRevision()+1);
            item.setRevision(targetItem.getRevision()+1);

        }

        if(req.getThumbnail()!=null && req.getThumbnail().getSize()>0) {
            uploadImages(item.getThumbnail(), req.getThumbnail());
        }

        if(req.getTag().size()>0) {
            uploadAttachments(item.getAttachments(), req.getAttachments());
        }

        return new NewItemCreateResponse(item.getId());
    }



    /**
     * 아이템 create
     *
     * @param req
     * @return 생성된 아이템 번호
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
                        attachmentTagRepository
                )

        );

        if(req.getThumbnail()!=null && req.getThumbnail().getSize()>0) {
            uploadImages(item.getThumbnail(), req.getThumbnail());
            if (!(req.getTag().size() == 0)) {//TODO : 나중에 함수로 빼기 (Attachment 유무 판단)
                //attachment가 존재할 땜나
                uploadAttachments(item.getAttachments(), req.getAttachments());
            }
        } else {
            //TODO 0628 기본 이미지 전달 => NONO 걍 NULL로 저장하고 DTO에서 줄 때만 기본 이미지 주소주면 끝
        }

        item.updateReadOnlyWhenSaved(); //저장하면 readonly = true
        saveTrueAttachment(item); //06-17 찐 저장될 때

        return new NewItemCreateResponse(item.getId());
    }

    //0712

    /**
     * 아이템 create
     *
     * @param req
     * @return 생성된 아이템 번호
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
                        attachmentTagRepository
                )

        );

        item.setReviseTargetId(targetId);

        if( item.getItemTypes().getItemType().name().equals("파트제품") ||
                item.getItemTypes().getItemType().name().equals("프로덕트제품")){
            System.out.println("equals 제품 이면 , 아이템 만들 때 controller에서 바로 item에 targetitme 등록 & 개정 ");

            NewItem targetItem = newItemRepository.findById(targetId).orElseThrow(ItemNotFoundException::new);
            NewItemCreateResponse res2 =  item.updateRevision(targetItem.getRevision()+1);
            item.setRevision(targetItem.getRevision()+1);

        }


        if(req.getThumbnail()!=null && req.getThumbnail().getSize()>0) {
            uploadImages(item.getThumbnail(), req.getThumbnail());
            if (!(req.getTag().size() == 0)) {//TODO : 나중에 함수로 빼기 (Attachment 유무 판단)
                //attachment가 존재할 땜나
                uploadAttachments(item.getAttachments(), req.getAttachments());
            }
        } else {
            //TODO 0628 기본 이미지 전달 => NONO 걍 NULL로 저장하고 DTO에서 줄 때만 기본 이미지 주소주면 끝
        }

        item.updateReadOnlyWhenSaved(); //저장하면 readonly = true
        saveTrueAttachment(item); //06-17 찐 저장될 때

        return new NewItemCreateResponse(item.getId());
    }


    /**
     * 썸네일 존재 시에 File Upload로 이미지 업로드
     *
     * @param images
     * @param fileImages
     */

//    private void uploadImages(List<NewItemImage> images, List<MultipartFile> fileImages) {
//        // 실제 이미지 파일을 가지고 있는 Multipart 파일을
//        // 이미지가 가지는 uniquename을 파일명으로 해서 파일저장소 업로드
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
        // 실제 이미지 파일을 가지고 있는 Multipart 파일을
        // 이미지가 가지는 uniquename을 파일명으로 해서 파일저장소 업로드
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
                        bomRepository,
                        preliminaryBomRepository,
                        defaultImageAddress

                )
        ).orElseThrow(RouteNotFoundException::new);

        Member currentMember = memberRepository.findById(authHelper.extractMemberId()).orElseThrow(
                AuthenticationEntryPointException::new
        );


        if (routeDtoList.size() > 0) {//아이템에 딸린 routeDto가 존재할 때
            RouteOrdering routeOrdering = routeOrderingRepository
                    .findByNewItemOrderByIdAsc(targetItem).get(

                            routeOrderingRepository
                                    .findByNewItemOrderByIdAsc(targetItem)
                                    .size()-1

                    );
            return NewItemDetailDto.toDto(
                    targetItem,
                    routeOrdering,
                    //최신 라우트에 딸린 라우트프로덕트 리스트 중,
                    // 라우트의 present 인덱스에 해당하는 타입을 데리고 오기
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

        if (!item.isTempsave()) { //true면 임시저장 상태, false면 찐 저장 상태
            //찐 저장 상태라면 UPDATE 불가, 임시저장 일때만 가능
            throw new ItemUpdateImpossibleException();
        }

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
                attachmentTagRepository
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

        return new ItemUpdateResponse(id);
    }

    @Transactional
    public NewItemCreateResponse tempEnd(Long id, NewItemUpdateRequest req) {

        NewItem item = newItemRepository.findById(id).orElseThrow(ItemNotFoundException::new);

        if (!item.isTempsave() || item.isReadonly()) {
            //tempsave가 false면 찐 저장 상태
            //찐 저장 상태라면 UPDATE 불가, 임시저장 일때만 가능
            //readonly가 true라면 수정 불가상태
            throw new ItemUpdateImpossibleException();
        }

        // (2) co의 affected item 의 revise_progress = false

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
                attachmentTagRepository
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

        return new NewItemCreateResponse(id);

    }

    public List<NewItemChildDto> readChildAll(Long id) {

        return NewItemChildDto.toDtoList(
                newItemParentChildrenRepository.
                        findAllWithParentByParentId(id),//ByParentIdOrderByParentIdAscNullsFirst(
                newItemParentChildrenRepository

        );

    }

    public List<NewItemParentDto> readParentAll(Long id) {

        return NewItemParentDto.toDtoList(
                newItemParentChildrenRepository.
                        findAllWithChildByChildId(id),//ByParentIdOrderByParentIdAscNullsFirst(
                newItemParentChildrenRepository

        );

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
     * 제품 중 상태가 complete 나 release 이면서
     * 제품 아닌 건 모두다 데려오는 NewItem - dev bom 추가용
     * @return
     */
    public List<NewItem> readDevBomItems() {

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

        return itemListElse;
    }

    private void saveTrueAttachment(NewItem target) {
        newItemAttachmentRepository.findByNewItem(target).
                forEach(
                        i->i.setSave(true)
                );

    }



    /**
     * 제품 중 상태가 complete 나 release 인 애들만 데려오기 - compare bom 용
     * @return
     */
    public List<NewItem> readCompareBomItems() {

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

        return finalProducts;
    }

    /**
     * affected item
     * 상태 complete, release, 아이템 - revise_progress=false 인 아이들만
     *
     */


    /**
     * affected items
     * 제품 중 상태가 complete 나 release 인 애들만 데려오기 - compare bom 용
     * @return
     */
    public List<NewItem> readAffectedItems() {


        List<NewItem> itemListProduct = newItemRepository.findAll();

        //1-2) 상태가 release 나 complete인 것만 최종 제품에 담을 예정
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

        // 최종 COMPLETE/RELEASE 된 아이들 중 지금 REVISE 중인 것이 아닌 것
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
    List affectedItemList = new ArrayList(affectedItems);

        return affectedItemList;
    }

    /**
     * Release 시 선택가능 후보들
     * release, complete 된 애들 중 release 가 0 인 애들 (배포 시 1.0으로 되는 애들)
     * @return
     */
    public List<NewItem> releaseItem(){
        List<NewItem> affItemList = readAffectedItems();
        System.out.println(affItemList + "다 차즈으으ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅁ");
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
     * createDevelopmentCard 에서 쓰일 것

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

                // 디자인 승인 완료 되니깐 ~ 찐 관계 생성
                NewItemParentChildrenId newItemParentChildrenId = new NewItemParentChildrenId(
                        parentNewItem,
                        newItemRepository.findByItemNumber(p.getCardNumber())

                );

                // 06-25 newParentItemId 는

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
     * route 가 co 실행되면 이 처리 수행
     * @param newItems // affected item List
     */
    public void ReviseItem(List<NewItem> newItems){
        System.out.println("item 은 revise_progress=true 돼야 합니다. ");
        for(NewItem newItem : newItems){
            System.out.println(newItem.getId() + "아이템은 revise 돼야 하므로 ~~~ ");
            newItem.setRevise_progress(true);
            // revise 진행 중 알리기
            //newItem.setModifier(changeOrderRequestPerson);
            // 아이템 수정할 수 있게 modifier 로 요청자 설정
            //newItem.setReadonly(false); newItem.setTempsave(true); // 아이템 수정 가능 모드로 변경
        }
    }

    /**
     * revise 완료 여부 , true 면 revise 완료 , false 면 revise 진행 중
     * @param affectedItems //affected item List
     */
    public boolean checkReviseCompleted(List<NewItem> affectedItems){

        int affectedItemSize = affectedItems.size();
        int revisedItemSize = 0;

        for(NewItem newItem : affectedItems){
            System.out.println(newItem.getId());
            if(!newItem.isRevise_progress()){ //revise progress 가 아니라면 (revise 되었다면)

                revisedItemSize+=1;
            }

        }

        return (affectedItemSize==revisedItemSize); //두개 길이가 같으면 완료, 아니라면 진행 중
    }

    /**
     * 라우트 update 된다면 자손과 부모의 revision 을 update 한다 !
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
     * target Id는 url 로 넘어오는 아이
     * @param targetId
     * @param newItemId
     */
    public NewItemCreateResponse registerTargetReviseItem(Long targetId, Long newItemId){

        System.out.println("register target revise item rrrrrrrrrrrrrrr rrrrrr");
        NewItem newItemForRevise = newItemRepository.findById(newItemId).orElseThrow(ItemNotFoundException::new);
        NewItemCreateResponse res1 = newItemForRevise.register_target_revise_item(targetId);
        // newItemForRevise.setReviseTargetId(targetId);
        System.out.println("등ㄹ고됨??????????????????????????????????????????????");
        System.out.println(newItemForRevise.getReviseTargetId());
        System.out.println(newItemForRevise.getId());

        // 그리고 아기(new item) 제품 타입이라면 아이템 등록할 때부터 revision update 진행
        // 아닌 애들은 어디서 updateRevision 하냐면 아이템 리뷰할 때 ! (아이템 리뷰 승인나야 revise o o )
        if(newItemForRevise.getItemTypes().getItemType().name().equals("파트제품") ||
                newItemForRevise.getItemTypes().getItemType().name().equals("프로덕트제품")){
            System.out.println("equals 제품 이면 , 아이템 만들 때 controller에서 바로 item에 targetitme 등록 & 개정 ");

            NewItem targetItem = newItemRepository.findById(targetId).orElseThrow(ItemNotFoundException::new);
            NewItemCreateResponse res2 = newItemForRevise.updateRevision(targetItem.getRevision()+1);
            //newItemForRevise.setRevision(targetItem.getRevision()+1);

        }

        return new NewItemCreateResponse(targetId);
    }

}
