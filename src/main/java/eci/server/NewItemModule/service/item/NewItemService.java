package eci.server.NewItemModule.service.item;

import eci.server.DashBoardModule.dto.itemTodo.ItemTodoResponse;
import eci.server.DashBoardModule.dto.itemTodo.ItemTodoResponseList;
import eci.server.ItemModule.dto.color.ColorListDto;
import eci.server.ItemModule.dto.color.ColorReadCondition;
import eci.server.ItemModule.dto.item.*;
import eci.server.ItemModule.dto.manufacture.ReadPartNumberService;
import eci.server.ItemModule.dto.newRoute.routeOrdering.RouteOrderingDto;
import eci.server.ItemModule.dto.newRoute.routeProduct.RouteProductDto;
import eci.server.ItemModule.entity.item.Attachment;
import eci.server.ItemModule.entity.item.Image;
import eci.server.ItemModule.entity.item.Item;
import eci.server.ItemModule.entity.member.Member;
import eci.server.ItemModule.entity.newRoute.RouteOrdering;
import eci.server.ItemModule.entity.newRoute.RoutePreset;
import eci.server.ItemModule.entity.newRoute.RouteProduct;
import eci.server.ItemModule.entity.newRoute.RouteProductMember;
import eci.server.ItemModule.exception.item.AttachmentNotFoundException;
import eci.server.ItemModule.exception.item.ItemNotFoundException;
import eci.server.ItemModule.exception.item.ItemUpdateImpossibleException;
import eci.server.ItemModule.exception.member.auth.AuthenticationEntryPointException;
import eci.server.ItemModule.exception.route.RouteNotFoundException;
import eci.server.ItemModule.repository.color.ColorRepository;
import eci.server.ItemModule.repository.item.*;
import eci.server.ItemModule.repository.manufacture.MakerRepository;
import eci.server.ItemModule.repository.member.MemberRepository;
import eci.server.ItemModule.repository.newRoute.RouteOrderingRepository;
import eci.server.ItemModule.repository.newRoute.RouteProductRepository;
import eci.server.ItemModule.service.file.FileService;
import eci.server.ItemModule.service.file.LocalFileService;
import eci.server.NewItemModule.dto.newItem.NewItemDetailDto;
import eci.server.NewItemModule.dto.newItem.NewItemPagingDtoList;
import eci.server.NewItemModule.dto.newItem.NewItemReadCondition;
import eci.server.NewItemModule.dto.newItem.RetrieveNewItemDetailDto;
import eci.server.NewItemModule.dto.newItem.create.NewItemCreateRequest;
import eci.server.NewItemModule.dto.newItem.create.NewItemCreateResponse;
import eci.server.NewItemModule.dto.newItem.create.NewItemTemporaryCreateRequest;
import eci.server.NewItemModule.dto.newItem.update.NewItemUpdateRequest;
import eci.server.NewItemModule.entity.NewItem;
import eci.server.NewItemModule.entity.NewItemAttachment;
import eci.server.NewItemModule.entity.NewItemImage;
import eci.server.NewItemModule.repository.attachment.NewItemAttachmentRepository;
import eci.server.NewItemModule.repository.classification.Classification1Repository;
import eci.server.NewItemModule.repository.classification.Classification2Repository;
import eci.server.NewItemModule.repository.classification.Classification3Repository;
import eci.server.NewItemModule.repository.coatingType.CoatingTypeRepository;
import eci.server.NewItemModule.repository.coatingWay.CoatingWayRepository;
import eci.server.NewItemModule.repository.item.NewItemRepository;
import eci.server.NewItemModule.repository.maker.NewItemMakerRepository;
import eci.server.NewItemModule.repository.supplier.SupplierRepository;
import eci.server.NewItemModule.service.classification.ClassificationService;
import eci.server.ProjectModule.dto.project.ProjectDto;
import eci.server.ProjectModule.entity.project.Project;
import eci.server.ProjectModule.exception.ProjectNotFoundException;
import eci.server.ProjectModule.repository.carType.CarTypeRepository;
import eci.server.ProjectModule.repository.clientOrg.ClientOrganizationRepository;
import eci.server.ProjectModule.repository.project.ProjectRepository;
import eci.server.config.guard.AuthHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
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
    private final NewItemAttachmentRepository newItemAttachmentRepository;
    private final RouteOrderingRepository routeOrderingRepository;
    private final RouteProductRepository routeProductRepository;
    private final NewItemMakerRepository itemMakerRepository;
    private final ProjectRepository projectRepository;
    private final ReadPartNumberService readPartNumber;
    private final FileService fileService;
    private final LocalFileService localFileService;
    private final ClassificationService classificationService;
    private final AuthHelper authHelper;
    private final RoutePreset routePreset;

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
                        makerRepository
                )
        );

        uploadImages(item.getThumbnail(), req.getThumbnail());

        uploadAttachments(item.getAttachments(), req.getAttachments());

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
                        makerRepository
                )

        );

        uploadImages(item.getThumbnail(), req.getThumbnail());
        if (!(req.getTag().size() == 0)) {//TODO : 나중에 함수로 빼기 (Attachment 유무 판단)
            //attachment가 존재할 땜나
            uploadAttachments(item.getAttachments(), req.getAttachments());
        }

        item.updateReadOnlyWhenSaved(); //저장하면 readonly = true
        return new NewItemCreateResponse(item.getId());
    }

    /**
     * 썸네일 존재 시에 File Upload로 이미지 업로드
     *
     * @param images
     * @param fileImages
     */

    private void uploadImages(List<NewItemImage> images, List<MultipartFile> fileImages) {
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

    private void uploadAttachments(List<NewItemAttachment> attachments, List<MultipartFile> filedAttachments) {
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
                targetItem.getThumbnail().get(0).
                        getUniqueName()
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
                        routeOrderingRepository.findByNewItem(targetItem),
                        routeProductRepository,
                        routeOrderingRepository
                )
        ).orElseThrow(RouteNotFoundException::new);


        if (routeDtoList.size() > 0) {//아이템에 딸린 routeDto가 존재할 때

            return NewItemDetailDto.toDto(
                    targetItem,
                    itemMakerRepository,
                    //최신 라우트에 딸린 라우트프로덕트 리스트 중,
                    // 라우트의 present 인덱스에 해당하는 타입을 데리고 오기
                    routeDtoList.get(routeDtoList.size() - 1),
                    RouteProductDto.toDto(
                            routeProductRepository.findAllByRouteOrdering(
                                    routeOrderingRepository.findById(
                                            routeDtoList.get(routeDtoList.size() - 1).getId()
                                    ).orElseThrow(RouteNotFoundException::new)
                            ).get(
                                    routeDtoList.get(routeDtoList.size() - 1).getPresent()
                            )
                    )

            );

        }
        return NewItemDetailDto.noRoutetoDto(
                        targetItem,
                        itemMakerRepository
                );
    }


    @Transactional
    public void delete(Long id) {

        NewItem item = newItemRepository.findById(id).orElseThrow(ItemNotFoundException::new);
        deleteImages(item.getThumbnail());

        newItemRepository.delete(item);
    }


    private void deleteImages(List<NewItemImage> images) {
        images.
                stream()
                .forEach(
                        i -> fileService.delete(
                                i.getUniqueName()
                        )
                );
    }

    private void deleteAttachments(List<NewItemAttachment> attachments) {
        attachments.
                stream().
                forEach(
                        i -> i.setDeleted(true)
                );
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
            if(projectRepository.findByNewItem(routeProduct.getRouteOrdering().getNewItem()).size()==0){

                RouteOrdering routeOrdering = routeOrderingRepository.findByNewItem(
                        routeProduct.getRouteOrdering().getNewItem()
                ).get(
                        routeOrderingRepository.findByNewItem(
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
                itemMakerRepository,
                itemTypesRepository,
                coatingWayRepository,
                coatingTypeRepository
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


}
