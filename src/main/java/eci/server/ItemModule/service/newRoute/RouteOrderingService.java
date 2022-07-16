package eci.server.ItemModule.service.newRoute;

import eci.server.BomModule.entity.Bom;
import eci.server.BomModule.entity.CompareBom;
import eci.server.BomModule.entity.DevelopmentBom;
import eci.server.BomModule.entity.PreliminaryBom;
import eci.server.BomModule.exception.BomNotFoundException;
import eci.server.BomModule.repository.*;
import eci.server.BomModule.service.BomService;
import eci.server.CRCOModule.entity.CoNewItem;
import eci.server.CRCOModule.entity.co.ChangeOrder;
import eci.server.CRCOModule.entity.cr.ChangeRequest;
import eci.server.CRCOModule.exception.CoNotFoundException;
import eci.server.CRCOModule.exception.CrNotFoundException;
import eci.server.CRCOModule.repository.co.ChangeOrderRepository;
import eci.server.CRCOModule.repository.co.CoNewItemRepository;
import eci.server.CRCOModule.repository.cr.ChangeRequestRepository;
import eci.server.DesignModule.entity.design.Design;
import eci.server.DesignModule.exception.DesignNotLinkedException;
import eci.server.DesignModule.repository.DesignRepository;
import eci.server.ItemModule.dto.member.MemberDto;
import eci.server.ItemModule.dto.newRoute.routeOrdering.*;
import eci.server.ItemModule.dto.newRoute.routeProduct.RouteProductCreateRequest;
import eci.server.ItemModule.dto.newRoute.routeProduct.RouteProductDto;
import eci.server.ItemModule.entity.item.ItemType;
import eci.server.ItemModule.entity.member.Member;
import eci.server.ItemModule.entity.newRoute.RouteOrdering;
import eci.server.ItemModule.entity.newRoute.RoutePreset;
import eci.server.ItemModule.entity.newRoute.RouteProduct;
import eci.server.ItemModule.exception.item.ItemNotFoundException;
import eci.server.ItemModule.exception.member.MemberNotFoundException;
import eci.server.ItemModule.exception.route.RouteNotFoundException;
import eci.server.ItemModule.exception.route.UpdateImpossibleException;
import eci.server.ItemModule.repository.item.ItemTypesRepository;
import eci.server.ItemModule.repository.member.MemberRepository;

import eci.server.ItemModule.repository.newRoute.RouteOrderingRepository;
import eci.server.ItemModule.repository.newRoute.RouteProductRepository;
import eci.server.ItemModule.repository.newRoute.RouteTypeRepository;
import eci.server.NewItemModule.dto.MembersDto;
import eci.server.NewItemModule.entity.JsonSave;
import eci.server.NewItemModule.entity.NewItem;
import eci.server.NewItemModule.exception.ItemTypeRequiredException;
import eci.server.NewItemModule.repository.item.NewItemRepository;
import eci.server.NewItemModule.service.item.NewItemService;
import eci.server.ProjectModule.entity.project.Project;
import eci.server.ProjectModule.exception.ProjectNotLinkedException;
import eci.server.ProjectModule.repository.project.ProjectRepository;
import eci.server.ReleaseModule.repository.ReleaseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RouteOrderingService {

    private final ProjectRepository projectRepository;
    private final RouteProductRepository routeProductRepository;
    private final MemberRepository memberRepository;
    private final NewItemRepository newItemRepository;
    private final DesignRepository designRepository;
    private final RouteOrderingRepository routeOrderingRepository;
    private final RouteTypeRepository routeTypeRepository;
    private final ItemTypesRepository itemTypesRepository;
    private final BomRepository bomRepository;
    private final PreliminaryBomRepository preliminaryBomRepository;
    private final DevelopmentBomRepository developmentBomRepository;
    private final CompareBomRepository compareBomRepository;
    private final JsonSaveRepository jsonSaveRepository;
    private final ChangeRequestRepository changeRequestRepository;
    private final ChangeOrderRepository changeOrderRepository;
    private final NewItemService newItemService;
    private final CoNewItemRepository coNewItemRepository;
    private final ReleaseRepository releaseRepository;

    @Value("${default.image.address}")
    private String defaultImageAddress;

    private final BomService bomService;

    private final RoutePreset routePreset;

    public RouteOrderingDto read(Long id) {

        RouteRejectPossibleResponse routeRejectPossibleResponse = rejectPossible(id);

        return RouteOrderingDto.toDto(
                routeOrderingRepository.findById(id).orElseThrow(RouteNotFoundException::new),
                routeProductRepository,
                routeOrderingRepository,
                routeRejectPossibleResponse,
                defaultImageAddress
        );

    }

    public List<List<MemberDto>> memberRead(Long routeId) {

        Set<String> names =
        read(routeId).getRouteProductList().stream().map(
                i->i.getName()
        ).collect(Collectors.toSet());

        List<String> nameList = new ArrayList<>(names);
        List<List<MemberDto>> mem = new ArrayList<>();

        int idx = 0;
        while(idx< nameList.size()-1) {
            for (RouteProductDto rp : read(routeId).getRouteProductList()) {
                if (nameList.get(idx).equals(rp.getName())) {
                    mem.add(
                            rp.getMember()
                    );
                    idx+=1;
                }
            }
        }
    return mem;
    }

    public List readRouteByItem(Long id) {

        List<String> typeList = new ArrayList<>();

        //아이템 타입에따라서 라우트 타입이 선택된다.

        // TODO 라벨 아니고 ITEM.ROUTE_TYPE.ID 로 선택해준다
        Integer routeType =  ItemType.valueOf(
                itemTypesRepository.findById(id).orElseThrow(ItemTypeRequiredException::new)
                        .getItemType().name()).label();

        List routeProduct = List.of((routePreset.routeByItemName[routeType]));

        for(Object type : routeProduct){
            typeList.add(type.toString());

        }
        return typeList;

    }

    public List<RouteOrderingDto> readAll(RouteOrderingReadCondition cond) {

        List<RouteOrdering> newRoutes = routeOrderingRepository.findByNewItemOrderByIdAsc(
                newItemRepository.findById(cond.getItemId())
                        .orElseThrow(RouteNotFoundException::new)
        );

        return RouteOrderingDto.toDtoList(
                newRoutes,
                routeProductRepository,
                routeOrderingRepository,
                bomRepository,
                preliminaryBomRepository,
                defaultImageAddress
        );
    }

    @Transactional
    public RouteOrderingCreateResponse createItemRoute(RouteOrderingCreateRequest req) {

        NewItem routeMadeItem = newItemRepository.findById(req.getItemId()).orElseThrow(ItemNotFoundException::new);


        if(routeMadeItem.getReviseTargetId()==null) { //걍 평범 아이템인 경우

            RouteOrdering newRoute = routeOrderingRepository.save(RouteOrderingCreateRequest.toEntity(
                            req,
                            newItemRepository,
                            routePreset,
                            routeTypeRepository
                    )
            );

            List<RouteProduct> routeProductList =
                    RouteProductCreateRequest.toEntityList(
                            req,
                            newRoute,
                            routePreset,
                            memberRepository,
                            routeTypeRepository

                    );

            for (RouteProduct routeProduct : routeProductList) {

                RouteProduct routeProduct1 =
                        routeProductRepository.save(routeProduct);
                System.out.println(routeProduct1.getRoute_name());
                System.out.println(routeProduct1.getMembers().get(0).getMember());
                System.out.println(routeProduct1.getMembers().get(0).getRouteProduct());
            }

            String itemTypeName = (newItemRepository.findById(newRoute.getNewItem().getId()).
                    orElseThrow(ItemNotFoundException::new)
            ).getItemTypes().getItemType().name();

            if (
                    !(
                            itemTypeName.equals("원재료")
                                    ||
                                    itemTypeName.equals("단순외주구매품")
                    )

            ) {
                //봄 생성
                Bom bom = bomRepository.save(
                        new Bom(
                                newRoute.getNewItem(),
                                memberRepository.findById(req.getMemberId()).orElseThrow(MemberNotFoundException::new)
                        )
                );
                //프릴리미너리 봄 생성
                PreliminaryBom preliminaryBom = preliminaryBomRepository.save(
                        new PreliminaryBom(
                                bom
                        )
                );

                NewItem newItem = preliminaryBom.getBom().getNewItem();

                // 06-11 태초에 기본 프릴리머리가 있었다

                String initPreliminary = "{" +

                        "\"cardNumber\":\"" + newItem.getItemNumber() + "\"," +
                        "\"cardName\": \"" + newItem.getName() + "\"," +
                        "\"classification\": \"" + newItem.getClassification().getClassification1().getName().toString()
                        + "/" + newItem.getClassification().getClassification2().getName().toString() +
                        (newItem.getClassification().getClassification3().getId().equals(99999L) ?
                                "" :
                                "/" + newItem.getClassification().getClassification3().getName()
                        )
                        + "\"," +
                        "\"cardType\": \"" + newItem.getItemTypes().getItemType().name() + "\"," +
                        "\"sharing\": \"" + (newItem.isSharing() ? "공용" : "전용") + "\"," +
                        "\"preliminaryBomId\": " + preliminaryBom.getId() + "," +
                        "\"plusPossible\": " + true + "," +
                        "\"children\": []" +
                        "}";

                JsonSave initialJsonSave = jsonSaveRepository.save(
                        new JsonSave(
                                initPreliminary,
                                preliminaryBom
                        )
                );

                DevelopmentBom developmentBom = developmentBomRepository.save(
                        new DevelopmentBom(
                                bom
                        )
                );

                CompareBom compareBom = compareBomRepository.save(
                        new CompareBom(
                                bom
                        )
                );

                newRoute.setBom(bom);
            }
            newRoute.getNewItem().updateTempsaveWhenMadeRoute();
            //라우트 만들면 임시저장 해제

            //0607 BOM + PRELIMINARY BOM 생성되게 하기
            return new RouteOrderingCreateResponse(newRoute.getId());
        }
        else{
            //revise_progress 로 라우트 만드는 것이라면
            //만드는 아이템에 revise target id가 등록돼있으면 걔는 revise new item

            RouteOrdering newRoute = routeOrderingRepository.save(RouteOrderingCreateRequest
                    .toRevisedRouteOrderingEntity( //
                            req,
                            newItemRepository,
                            routePreset,
                            routeTypeRepository
                    )
            );

            List<RouteProduct> routeProductList =
                    RouteProductCreateRequest.toEntityList(
                            req,
                            newRoute,
                            routePreset,
                            memberRepository,
                            routeTypeRepository

                    );

            for (RouteProduct routeProduct : routeProductList) {

                RouteProduct routeProduct1 =
                        routeProductRepository.save(routeProduct);
                System.out.println(routeProduct1.getRoute_name());
                System.out.println(routeProduct1.getMembers().get(0).getMember());
                System.out.println(routeProduct1.getMembers().get(0).getRouteProduct());
            }

            String itemTypeName = (newItemRepository.findById(newRoute.getNewItem().getId()).
                    orElseThrow(ItemNotFoundException::new)
            ).getItemTypes().getItemType().name();

            if (
                    !(
                            itemTypeName.equals("원재료")
                                    ||
                                    itemTypeName.equals("단순외주구매품")
                    )

            ) {
                //봄 생성
                Bom bom = bomRepository.save(
                        new Bom(
                                newRoute.getNewItem(),
                                memberRepository.findById(req.getMemberId()).orElseThrow(MemberNotFoundException::new)
                        )
                );
                //프릴리미너리 봄 생성
                PreliminaryBom preliminaryBom = preliminaryBomRepository.save(
                        new PreliminaryBom(
                                bom
                        )
                );

                NewItem newItem = preliminaryBom.getBom().getNewItem();

                // 06-11 태초에 기본 프릴리머리가 있었다

                String initPreliminary = "{" +

                        "\"cardNumber\":\"" + newItem.getItemNumber() + "\"," +
                        "\"cardName\": \"" + newItem.getName() + "\"," +
                        "\"classification\": \"" + newItem.getClassification().getClassification1().getName().toString()
                        + "/" + newItem.getClassification().getClassification2().getName().toString() +
                        (newItem.getClassification().getClassification3().getId().equals(99999L) ?
                                "" :
                                "/" + newItem.getClassification().getClassification3().getName()
                        )
                        + "\"," +
                        "\"cardType\": \"" + newItem.getItemTypes().getItemType().name() + "\"," +
                        "\"sharing\": \"" + (newItem.isSharing() ? "공용" : "전용") + "\"," +
                        "\"preliminaryBomId\": " + preliminaryBom.getId() + "," +
                        "\"plusPossible\": " + true + "," +
                        "\"children\": []" +
                        "}";

                JsonSave initialJsonSave = jsonSaveRepository.save(
                        new JsonSave(
                                initPreliminary,
                                preliminaryBom
                        )
                );

                DevelopmentBom developmentBom = developmentBomRepository.save(
                        new DevelopmentBom(
                                bom
                        )
                );

                CompareBom compareBom = compareBomRepository.save(
                        new CompareBom(
                                bom
                        )
                );

                newRoute.setBom(bom);
            }




            newRoute.getNewItem().updateTempsaveWhenMadeRoute();
            //라우트 만들면 임시저장 해제



            //0607 BOM + PRELIMINARY BOM 생성되게 하기
            return new RouteOrderingCreateResponse(newRoute.getId());

        }


    }

    @Transactional
    public RouteOrderingCreateResponse createCrRoute(RouteOrderingCreateRequest req) {
        RouteOrdering newRoute = routeOrderingRepository.save(RouteOrderingCreateRequest.toCrEntity(
                        req,
                        routePreset,
                        changeRequestRepository,
                        routeTypeRepository
                )
        );

        List<RouteProduct> routeProductList =
                RouteProductCreateRequest.toCREntityList(
                        req,
                        newRoute,
                        routePreset,
                        memberRepository,
                        routeTypeRepository

                );

        for (RouteProduct routeProduct : routeProductList) {

            RouteProduct routeProduct1 =
                    routeProductRepository.save(routeProduct);
            System.out.println(routeProduct1.getRoute_name());
            System.out.println(routeProduct1.getMembers().get(0).getMember());
            System.out.println(routeProduct1.getMembers().get(0).getRouteProduct());
        }

        newRoute.getChangeRequest().updateTempsaveWhenMadeRoute();
        //라우트 만들면 임시저장 해제

        return new RouteOrderingCreateResponse(newRoute.getId());
    }


    @Transactional
    public RouteOrderingCreateResponse createCoRoute(RouteOrderingCreateRequest req) {

        RouteOrdering newRoute = routeOrderingRepository.save(RouteOrderingCreateRequest.toCoEntity(
                        req,
                        routePreset,
                        changeOrderRepository,
                        routeTypeRepository
                )
        );

        List<RouteProduct> routeProductList =
                RouteProductCreateRequest.toCOEntityList(
                        req,
                        newRoute,
                        routePreset,
                        memberRepository,
                        routeTypeRepository

                );

        for (RouteProduct routeProduct : routeProductList) {

            RouteProduct routeProduct1 =
                    routeProductRepository.save(routeProduct);
            System.out.println(routeProduct1.getRoute_name());
            System.out.println(routeProduct1.getMembers().get(0).getMember());
            System.out.println(routeProduct1.getMembers().get(0).getRouteProduct());
        }

        newRoute.getChangeOrder().updateTempsaveWhenMadeRoute();
        //라우트 만들면 임시저장 해제


        return new RouteOrderingCreateResponse(newRoute.getId());
    }


    @Transactional
    public RouteOrderingCreateResponse createReleaseRoute(RouteOrderingCreateRequest req) {

        RouteOrdering newRoute = routeOrderingRepository
                .save(RouteOrderingCreateRequest.
                toReleaseEntity(
                        req,
                        routePreset,
                        releaseRepository
                )
        );

        List<RouteProduct> routeProductList =
                RouteProductCreateRequest.toRELEASEEntityList(
                        req,
                        newRoute,
                        routePreset,
                        memberRepository,
                        routeTypeRepository

                );

        for (RouteProduct routeProduct : routeProductList) {

            RouteProduct routeProduct1 =
                    routeProductRepository.save(routeProduct);
            System.out.println(routeProduct1.getRoute_name());
            System.out.println(routeProduct1.getMembers().get(0).getMember());
            System.out.println(routeProduct1.getMembers().get(0).getRouteProduct());
        }

        newRoute.getRelease().updateTempsaveWhenMadeRoute();
        //라우트 만들면 임시저장 해제


        return new RouteOrderingCreateResponse(newRoute.getId());
    }

    @Transactional
    public List<RouteProductDto> rejectUpdate(
            Long id,
            String rejectComment,
            Integer rejectedSequence
    ) {

        RouteOrdering routeOrdering = routeOrderingRepository.findById(id).orElseThrow(RouteNotFoundException::new);

        //추가적으로 만들어진 애들 반환
        List<RouteProduct> rejectUpdatedRouteProductList = routeOrdering.rejectUpdate(
                id,
                rejectComment,
                rejectedSequence,
                routeOrderingRepository,
                routeProductRepository,
                developmentBomRepository

        );

        List<RouteProduct> addedProducts = new ArrayList<>();

        // 새로 만들어진 애들 저장
        for (RouteProduct routeProduct : rejectUpdatedRouteProductList) {
            addedProducts.add(routeProductRepository.save(routeProduct));
        }

        // 처음으로 복제된 애는 거부대상 아이의 복제품 => 얘의 set reject=true로 변경
        //06-03 얘는 rejected false로 해놔야 됨, pre-rejected가 true고
        addedProducts.get(0).setRejected(false);

        //0517 : show= False 인 애들 삭제할건데 검사범위는 끝까지 (라우트 프로덕트 기존 길이의)
        // 0519 에러의 원인 : 이 기존 길이가 넘 짧지,,, 새로 생기고 난리 났는데,, => 삭제가 안되는 에러
//        int range = List.of((routePreset.
//                itemRouteName[ItemType.valueOf(
//                        addedProducts.get(0).getRouteOrdering().getItem().getType()).label()]))
//                .size();//addedProducts.get(0).getSequence()+1;


        List<RouteProduct> deletedList =
                //isShow 가 false 인 것은 삭제 처리
                routeProductRepository.findAllByRouteOrdering(routeOrdering)
                        .subList(
                                routeOrdering.getPresent() - 1,
                                routeProductRepository.findAllByRouteOrdering(routeOrdering).size()
                        )
                        .stream().filter(
                                d -> !d.isRoute_show()
                        )
                        .collect(
                                Collectors.toList()
                        );

        //본격 삭제 진행
        for (RouteProduct routeProduct : deletedList) {
            routeProductRepository.delete(routeProduct);
        }

        // 기존 + 새 라우트프로덕트까지 해서 돌려주기
        return RouteProductDto.toProductDtoList(
                routeProductRepository.findAllByRouteOrdering(routeOrdering),
                defaultImageAddress
        );
    }

    @Transactional
    public RouteUpdateResponse approveUpdate(Long id, RouteOrderingUpdateRequest req) {

        RouteOrdering routeOrdering = routeOrderingRepository
                .findById(id)
                .orElseThrow(RouteNotFoundException::new);


        List<RouteProduct> presentRouteProductCandidate = routeProductRepository
                .findAllByRouteOrdering(routeOrdering);

        //현재 진행중인 라우트프로덕트

        if (routeOrdering.getLifecycleStatus().equals("COMPLETE")) {
            throw new UpdateImpossibleException();
        }

        if (presentRouteProductCandidate.size()== routeOrdering.getPresent()) {
            //만약 present 가 끝까지 닿았으면 현재 complete 된 상황!
            routeOrdering.updateToComplete();

            //presentRouteProductCandidate.get(routeOrdering.getPresent()-1).setComment(req.getComment());

            // 라우트 오더링의 revised cnt 가 0보다 컸다면,
            // revise 당한 아이템의 새로 만들어진 아가의 라우트 오더링이므로
            // 무조건 아이템 존재할 수 밖에 없음
//            if(routeOrdering.getRevisedCnt()>0){
//                //지금 승인된 라우트가 revise 로 인해 새로 생긴 아이템이라면
//                routeOrdering.setRevisedCnt(0);
//                //0710 revise 로 생긴 route ordering 이었다면 다시 0으로 복구;
//
//                if(routeOrdering.getNewItem()==null){
//                    //revise 인데도 안 묶여있으면 뭔가 잘못됐어,
//                    throw new ItemNotFoundException();//에러 던지기
//                }
//
//                NewItem targetRevisedItem = newItemRepository.
//                        findById(routeOrdering.getNewItem().getReviseTargetId()).orElseThrow(ItemNotFoundException::new);
//
//                if (targetRevisedItem.isRevise_progress()) {
//                    routeOrdering.getNewItem().setRevise_progress(false);
//                    //0712 아기의 target route 가 revise progress 가 진행 중이라면 라우트 complete 될 때 false 로 갱신
//                }
//
//                if(coNewItemRepository.findByNewItemOrderByCreatedAtAsc(routeOrdering.getNewItem()).size()>0) {
//                    // (1) 지금 revise 완료 된 아이템의 CO 를 검사하기 위해 check co 찾기
//                    ChangeOrder checkCo =
//                            coNewItemRepository.findByNewItemOrderByCreatedAtAsc(routeOrdering.getNewItem()).get(
//                                            coNewItemRepository.findByNewItemOrderByCreatedAtAsc(routeOrdering.getNewItem()).size()-1
//                                    )//가장 최근에 맺어진 co-new item 관계 중 가장 최신 아이의 co를 검사하기
//                                    .getChangeOrder();
//
//                    // (2) check co 의 affected item 리스트
//                    List<CoNewItem> coNewItemsOfChkCo = checkCo.getCoNewItems();
//                    List<NewItem> affectedItemOfChkCo = coNewItemsOfChkCo.stream().map(
//                            i->i.getNewItem()
//                    ).collect(Collectors.toList());
//
//                    // (3) checkCo의 routeOrdering 찾아오기
//                    RouteOrdering routeOrderingOfChkCo =
//                            routeOrderingRepository.findByChangeOrderOrderByIdAsc(checkCo).get(
//                                    routeOrderingRepository.findByChangeOrderOrderByIdAsc(checkCo).size()-1
//                            );
//
//                    // (4) affected item 이 모두 revise 완료된다면 update route
//                    if(newItemService.checkReviseCompleted(affectedItemOfChkCo)){
//                        routeOrderingOfChkCo.CoUpdate(routeProductRepository);
//                    }
//
//                }
//
//                //throw new UpdateImpossibleException();
//                // 0710 : 이 아이템과 엮인 아이들 (CHILDREN , PARENT )들의 REVISION +=1 진행 !
//                // 대상 아이템들은 이미 각각 아이템 리뷰 / 프로젝트 링크할 때 REVISION+1 당함
//                newItemService.revisionUpdateAllChildrenAndParentItem(routeOrdering.getNewItem());
//
//
//
//            }

        } else {
            RouteProduct targetRoutProduct = presentRouteProductCandidate.get(routeOrdering.getPresent());

            // TODO : 함수로 따로 빼기 availableAccept("route_name)
            //route_name에 따른 조건을 각각 설정해서 해당 조건 부합할 때만 accept 가능하게,
            // 아니면 exception 날리게 설정

            //if (targetRoutProduct.getRoute_name().equals("프로젝트와 Item(제품) Link(설계자)")) {

            // 06-17 리팩토링 : module : project , name : create
            if (targetRoutProduct.getType().getModule().equals("PROJECT")
                    && targetRoutProduct.getType().getName().equals("CREATE")) {

                //아이템에 링크된 맨 마지막 (최신) 프로젝트 데려오기
//                if (projectRepository.findByNewItemOrderByIdAsc(routeOrdering.getNewItem()).size() == 0) {
//                    throw new ProjectNotLinkedException();
//                } else {
                    Project linkedProject =
                            projectRepository.findByNewItemOrderByIdAsc(routeOrdering.getNewItem())
                                    .get(
                                            projectRepository.findByNewItemOrderByIdAsc(routeOrdering.getNewItem()).size() - 1
                                    );
                    //그 프로젝트를 라우트 프로덕트에 set 해주기
                    targetRoutProduct.setProject(linkedProject);
                    targetRoutProduct.getRouteOrdering().setProject(linkedProject);
                    //05-12 추가사항 : 이 라우트를 제작해줄 때야 비로소 프로젝트는 temp save = false 가 되는 것
                    linkedProject.finalSaveProject();
                //}
            }

            /////////////////////////////////////////////////////////////////////////////////

//            else if (targetRoutProduct.getRoute_name().equals("기구Design생성[설계자]")) {
            // 06-17 리팩토링 : module : DESIGN , name : create
            else if (targetRoutProduct.getType().getModule().equals("DESIGN")
                    && targetRoutProduct.getType().getName().equals("CREATE")) {

                //아이템에 링크된 맨 마지막 (최신) 디자인 데려오기
                if (designRepository.findByNewItemOrderByIdAsc(routeOrdering.getNewItem()).size() == 0) {
                    throw new DesignNotLinkedException();
                } else {
                    Design linkedDesign =
                            designRepository.findByNewItemOrderByIdAsc(routeOrdering.getNewItem())
                                    .get(
                                            designRepository.findByNewItemOrderByIdAsc(routeOrdering.getNewItem()).size() - 1
                                    );
                    //만약 지금 rejected 가 true였다면 , 이제 새로 다시 넣어주는 것이니깐 rejected풀어주기
                    if (targetRoutProduct.isPreRejected()) {
                        targetRoutProduct.setPreRejected(false); //06-01 손댐
                    }
                    //그 프로젝트를 라우트 프로덕트에 set 해주기
                    targetRoutProduct.setDesign(linkedDesign);
                    targetRoutProduct.getRouteOrdering().setDesign(linkedDesign);
                    // 해당 design 의 임시저장을 false
                    //05-12 추가사항 : 이 라우트를 제작해줄 때야 비로소 프로젝트는 temp save = false 가 되는 것
                    linkedDesign.finalSaveDesign();
                }

//                if (projectRepository.findByNewItemOrderByIdAsc(routeOrdering.getNewItem()).size() == 0) {
//                    throw new ProjectNotLinkedException();
//                } else {
//                    Project linkedProject =
//                            projectRepository.findByNewItemOrderByIdAsc(routeOrdering.getNewItem())
//                                    .get(
//                                            projectRepository.findByNewItemOrderByIdAsc(routeOrdering.getNewItem()).size() - 1
//                                    );
//                    //그 프로젝트를 라우트 프로덕트에 set 해주기
//                    targetRoutProduct.setProject(linkedProject);
//                    //05-12 추가사항 : 이 라우트를 제작해줄 때야 비로소 프로젝트는 temp save = false 가 되는 것
//                    linkedProject.finalSaveProject();
//                }

            }

            else if (targetRoutProduct.getType().getModule().equals("DESIGN")
                    && targetRoutProduct.getType().getName().equals("REVIEW")) {

                //아이템에 링크된 맨 마지막 (최신) 디자인 데려오기
                if (designRepository.findByNewItemOrderByIdAsc(routeOrdering.getNewItem()).size() == 0) {
                    throw new DesignNotLinkedException();
                } else {
                    Design linkedDesign =
                            designRepository.findByNewItemOrderByIdAsc(routeOrdering.getNewItem())
                                    .get(
                                            designRepository.findByNewItemOrderByIdAsc(routeOrdering.getNewItem()).size() - 1
                                    );

                    if (routeOrdering.getNewItem().getItemTypes().getItemType().name().equals("파트제품") ||
                            routeOrdering.getNewItem().getItemTypes().getItemType().name().equals("프로덕트제품")) {
                        // 디자인 리뷰 승인 나면 아이템 정보 관계 맺어주기
                        bomService.makeDevBom(linkedDesign.getId());

                        // dev bom 의 temp new item parent children 관계도 맺어주기
                        bomService.makeTempDevBom(

                                linkedDesign.getId(),
                                true
                        );
                    }
                }


            }

            //06-27 데브 봄 만들고 라우트 요청하면 temp save = false & 라우트오더링에 봄 등록

            else if (targetRoutProduct.getType().getModule().equals("BOM")
                    && targetRoutProduct.getType().getName().equals("CREATE")) {

                //아이템에 링크된 봄 아이디 건네주기
                if (bomRepository.findByNewItemOrderByIdAsc(routeOrdering.getNewItem()).size() == 0) {
                    throw new BomNotFoundException();
                } else {
                    Bom bom =
                            bomRepository.findByNewItemOrderByIdAsc(routeOrdering.getNewItem())
                                    .get(
                                            bomRepository.findByNewItemOrderByIdAsc(routeOrdering.getNewItem()).size() - 1
                                    );

                    //만약 지금 rejected 가 true였다면 , 이제 새로 다시 넣어주는 것이니깐 rejected풀어주기
                    if (targetRoutProduct.isPreRejected()) {
                        targetRoutProduct.setPreRejected(false);
                    }
                    //그 프로젝트를 라우트 프로덕트에 set 해주기
                    targetRoutProduct.setBom(bom);
                    targetRoutProduct.getRouteOrdering().setBom(bom);
                    // 해당 design 의 임시저장을 false
                    //05-12 추가사항 : 이 라우트를 제작해줄 때야 비로소 프로젝트는 temp save = false 가 되는 것
                    DevelopmentBom developmentBom =
                            developmentBomRepository.findByBom(bom);

                    developmentBom.updateTempSaveFalse();
                    developmentBom.updateReadonlyTrue();
                }
            }

            else if (targetRoutProduct.getType().getModule().equals("BOM")
                    && targetRoutProduct.getType().getName().equals("REVIEW")) {

                //아이템에 링크된 봄 아이디 건네주기
                if (bomRepository.findByNewItemOrderByIdAsc(routeOrdering.getNewItem()).size() == 0) {
                    throw new BomNotFoundException();
                } else {
                    Bom bom =
                            bomRepository.findByNewItemOrderByIdAsc(routeOrdering.getNewItem())
                                    .get(
                                            bomRepository.findByNewItemOrderByIdAsc(routeOrdering.getNewItem()).size() - 1
                                    );

                    // 디자인 리뷰 승인 나면 아이템 정보 관계 맺어주기
                    bomService.makeFinalBom(bom);

                }


            }


            else if (targetRoutProduct.getType().getModule().equals("CR")
                    && targetRoutProduct.getType().getName().equals("CREATE")) {

                //아이템에 링크된 봄 아이디 건네주기
                if ((routeOrdering.getChangeRequest()==null)) {
                    throw new CrNotFoundException();
                } else {
                    if (targetRoutProduct.isPreRejected()) {
                        targetRoutProduct.setPreRejected(false); //06-01 손댐
                    }
                    targetRoutProduct.setChangeRequest(routeOrdering.getChangeRequest());

                    //이 라우트를 제작해줄 때야 비로소 temp save = false 가 되는 것
                    routeOrdering.getChangeRequest().updateTempsaveWhenMadeRoute();

                }


            }

            else if (targetRoutProduct.getType().getModule().equals("CO")
                    && targetRoutProduct.getType().getName().equals("REQUEST")) {

                //아이템에 링크된 봄 아이디 건네주기
                if ((routeOrdering.getChangeOrder()==null)) {
                    throw new CoNotFoundException();
                } else {
                    if (targetRoutProduct.isPreRejected()) {
                        targetRoutProduct.setPreRejected(false); //06-01 손댐
                    }
                    targetRoutProduct.setChangeOrder(routeOrdering.getChangeOrder());

                    //이 라우트를 제작해줄 때야 비로소 temp save = false 가 되는 것
                    routeOrdering.getChangeOrder().updateTempsaveWhenMadeRoute();

                }


            }

            else if (targetRoutProduct.getType().getModule().equals("CO")
                    && targetRoutProduct.getType().getName().equals("APPROVE")) {

                //아이템에 링크된 봄 아이디 건네주기
                if ((routeOrdering.getChangeOrder()==null)) {
                    throw new CoNotFoundException();
                } else {
                    ChangeOrder changeOrder=routeOrdering.getChangeOrder();

                    List<NewItem> affectedItems =
                            changeOrder.getCoNewItems().stream().map(
                                    m->m.getNewItem()
                            ).collect(Collectors.toList());

                    newItemService.ReviseItem(affectedItems);

                }


            }

            else if (targetRoutProduct.getType().getModule().equals("CO")
                    && targetRoutProduct.getType().getName().equals("CONFIRM")) {

                // CO 안에 있는 CR 들의 crCompletedByCo() 호출해서 DONE = TRUE 로 바꾸기

                if ((routeOrdering.getChangeOrder()==null)) {
                    throw new CoNotFoundException();
                } else {

                    // (1)  이 co의 cr 들의 done=true
                    ChangeOrder changeOrder=routeOrdering.getChangeOrder();

                    List<ChangeRequest> changeRequests = changeOrder.getChangeRequests();
                    for(ChangeRequest cr : changeRequests){
                        cr.crCompletedByCo();
                    }

                }


            }

            else if (targetRoutProduct.getType().getModule().equals("ITEM")
                    && targetRoutProduct.getType().getName().equals("REVIEW")) {

                // 파트제품, 프로덕트 제품 제외한 다른 애들은 approve route 할 때
                // (1) 아이템이 revise progress 이며
                // (2) 지금 승인하는게 ITEM REVIEW 면 revision+=1
                NewItem chkItem = targetRoutProduct.getRouteOrdering().getNewItem();
                if (chkItem.getReviseTargetId()!=null) {
                    //저게 null 이 아니라면 targetId의 아이템 revision보다 +1값으로
                    // 기존 : chkItem.isRevise_progress())

                    if (!
                            (chkItem.getItemTypes().getItemType().name().equals("파트제품") ||
                                    chkItem.getItemTypes().getItemType().name().equals("프로덕트제품"))
                    ) {// 파트 제품과 프로덕트 제품 아닌 경우에는 item review를 진행할 때 revision update !
                        // 제품은 create 할 때

                        NewItem targetItem = newItemRepository.findById(chkItem.getReviseTargetId()).orElseThrow(
                                ItemNotFoundException::new
                        );

                        //chkItem.updateRevision(targetItem.getRevision()+1);
                        chkItem.updateRevisionAndHeritageReleaseCnt(
                                targetItem.getRevision()+1,
                                targetItem.getReleased());

                        // 제품 아닌 아이들은 이 단계에서 revision 갱신 및 released 를 상속 받기

                    }


                }
            }

            // 0713 : 아이템 생성 승인했는데 REVISE 진행 제품이라면
            // 수정해야 할 PROJECT 가 딸려있음, 이 PROJ 수정 가능하게 MODE 변경
            else if (targetRoutProduct.getType().getModule().equals("ITEM")
                    && targetRoutProduct.getType().getName().equals("CREATE")) {

                // 파트제품, 프로덕트 제품 제외한 다른 애들은 approve route 할 때
                // (1) 아이템이 revise progress 이며
                // (2) 지금 승인하는게 ITEM REVIEW 면 revision+=1
                NewItem chkItem = targetRoutProduct.getRouteOrdering().getNewItem();
                if (chkItem.getReviseTargetId()!=null) {
                    //저게 null 이 아니라면 targetId의 아이템 revision보다 +1값으로
                    // 기존 : chkItem.isRevise_progress())

                    if (
                            (chkItem.getItemTypes().getItemType().name().equals("파트제품") ||
                                    chkItem.getItemTypes().getItemType().name().equals("프로덕트제품"))
                    ) {// 파트 제품과 프로덕트 제품 아닌 경우에는 item review를 진행할 때 revision update !

                        // (1) 제품은 create 할 때 REVISION + 1
                        NewItem targetOldReviseItem = newItemRepository.findById(chkItem.getReviseTargetId()).orElseThrow(
                                ItemNotFoundException::new
                        );

                        chkItem.updateRevision(targetOldReviseItem.getRevision()+1);
                        //targetItem.getRevision() 보다 하나 더 큰 값으로 갱신


                        // (2) 제품은

//
//                        if(projectRepository.findByNewItemOrderByIdAsc(targetOldReviseItem).size()>0) {
//                            Project oldProject = projectRepository.findByNewItemOrderByIdAsc(targetOldReviseItem)
//                                    .get(projectRepository.findByNewItemOrderByIdAsc(targetOldReviseItem).size()-1);
//
//                            oldProject.setReadonly(false);
//                            oldProject.setTempsave(true);
//                        }

                        /////////////

                    }


                }
            }

            RouteOrderingUpdateRequest newRouteUpdateRequest =
                    routeOrdering
                            .update(
                                    req,
                                    routeProductRepository,
                                    newItemRepository,
                                    coNewItemRepository,
                                    routeOrderingRepository,
                                    newItemService
                            );
        }

        return new RouteUpdateResponse(id);

    }

    /**
     * 라우트 오더링 아이디 전달, 거절 가능한 라우트 프로덕트 아이디 돌려주기
     * 0523
     *
     * @param id
     * @return
     */
    @Transactional
    public RouteRejectPossibleResponse rejectPossible(Long id) {

        RouteOrdering routeOrdering = routeOrderingRepository
                .findById(id)
                .orElseThrow(RouteNotFoundException::new);


        //라우트 오더링의 라우트 리스트
        List<RouteProduct> presentRouteProductCandidate = routeProductRepository
                .findAllByRouteOrdering(routeOrdering);

        if (routeOrdering.getPresent() == presentRouteProductCandidate.size()) {
            //SeqAndName tmpSeqAndName = new SeqAndName(null,null);
            List<SeqAndName> tmpList = new ArrayList<>();
            return new RouteRejectPossibleResponse(tmpList);
        }

        //라우트 리스트 처음부터 거절 주체 전까지
        List<RouteProduct> routeProductRejectCandidates =
                presentRouteProductCandidate.subList(0, routeOrdering.getPresent());

        //CASE1 ) 거절 주체가 되는 라우트
        RouteProduct targetRoutProduct = presentRouteProductCandidate.get(routeOrdering.getPresent());

        // 거절 가능애들 담을 아이디 리스트
        //List<Long> rejectPossibleIdList = new ArrayList<>();
        List<SeqAndName> seqAndNameList = new ArrayList<>();


        //CASE1 ) 만약 이전에 있는 것 모두 거절가능하다면
//        for(RouteProduct routeProduct : routeProductRejectCandidates){
//                if()
//            }

        //CASE2 )
        // 리뷰만 거절 가능하므로 리뷰인지 검증 (타입 -4,5,6,12)
        // => 거절 가능타입 검증
        // => DISABLE 아닌지 검증
        // =>
        // 이 reviewRouteArrList 에 추가해줘야 함 (꺼절 가능 라우트 번호)
        if (routePreset.reviewRouteArrList.contains(targetRoutProduct.getType().getId().toString())) {
            //만약 리뷰타입의 라우트라면
            Long rejectPossibleTypeId = null;


            switch (targetRoutProduct.getType().getId().toString()) {

                case "4":         // 아이템 리뷰인 경우
                    rejectPossibleTypeId = 1L;
                    break;
                case "6":            // 플젝 리뷰
                    rejectPossibleTypeId = 9L;
                    break;
                case "5":            // 디자인 리뷰
                    rejectPossibleTypeId = 13L;
                    break;
                case "12":            // 봄 리뷰
                    rejectPossibleTypeId = 14L;
                    break;

                case "16":            // CR신청 APPROVE
                case "17":            // CR REVIEW
                    rejectPossibleTypeId = 15L;
                    break;
                //CR REQUEST
                case "19": //CO 신청 승인 APPROVE
                    rejectPossibleTypeId = 18L; //CO 신청

                    break;
                //release review
                case "23":
                    rejectPossibleTypeId = 22L; //release request

                    break;
                default:        // 모두 해당이 안되는 경우
                    //에러 던지기
                    rejectPossibleTypeId = 0L;
                    break;
            }

            for (RouteProduct routeProduct : routeProductRejectCandidates) {
                System.out.println(routeProduct.getType().getId());
                if (Objects.equals(routeProduct.getType().getId(), rejectPossibleTypeId)
                        && !(routeProduct.isDisabled())) {
                    seqAndNameList.add(
                            new SeqAndName(
                                    routeProduct.getSequence(),
                                    routeProduct.getRoute_name()
                            ));
                }

            }

        }

//        if(rejectPossibleIdList.size()==0){
//            throw new RejectImpossibleException();
//        } // 05-24 : get에 얘를 포함하면서부턴, 리뷰가 아닌 라우트 프로덕트들에게도 이 서비스가 적용 예정
        // => 따라서 이 예외처리 제외시켜줌
        return new RouteRejectPossibleResponse(seqAndNameList);

    }
}