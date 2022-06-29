package eci.server.ItemModule.service.newRoute;

import eci.server.BomModule.entity.Bom;
import eci.server.BomModule.entity.CompareBom;
import eci.server.BomModule.entity.DevelopmentBom;
import eci.server.BomModule.entity.PreliminaryBom;
import eci.server.BomModule.exception.BomNotFoundException;
import eci.server.BomModule.repository.*;
import eci.server.BomModule.service.BomService;
import eci.server.DesignModule.entity.design.Design;
import eci.server.DesignModule.exception.DesignNotLinkedException;
import eci.server.DesignModule.repository.DesignRepository;
import eci.server.ItemModule.dto.newRoute.routeOrdering.*;
import eci.server.ItemModule.dto.newRoute.routeProduct.RouteProductCreateRequest;
import eci.server.ItemModule.dto.newRoute.routeProduct.RouteProductDto;
import eci.server.ItemModule.entity.item.ItemType;
import eci.server.ItemModule.entity.newRoute.RouteOrdering;
import eci.server.ItemModule.entity.newRoute.RoutePreset;
import eci.server.ItemModule.entity.newRoute.RouteProduct;
import eci.server.ItemModule.exception.member.MemberNotFoundException;
import eci.server.ItemModule.exception.route.RouteNotFoundException;
import eci.server.ItemModule.exception.route.UpdateImpossibleException;
import eci.server.ItemModule.repository.item.ItemTypesRepository;
import eci.server.ItemModule.repository.member.MemberRepository;

import eci.server.ItemModule.repository.newRoute.RouteOrderingRepository;
import eci.server.ItemModule.repository.newRoute.RouteProductRepository;
import eci.server.ItemModule.repository.newRoute.RouteTypeRepository;
import eci.server.NewItemModule.entity.JsonSave;
import eci.server.NewItemModule.entity.NewItem;
import eci.server.NewItemModule.exception.ItemTypeRequiredException;
import eci.server.NewItemModule.repository.TempNewItemParentChildrenRepository;
import eci.server.NewItemModule.repository.item.NewItemRepository;
import eci.server.NewItemModule.service.TempNewItemParentChildService;
import eci.server.ProjectModule.entity.project.Project;
import eci.server.ProjectModule.exception.ProjectNotLinkedException;
import eci.server.ProjectModule.repository.project.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
    private final TempNewItemParentChildService tempNewItemParentChildService;

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

        List<RouteOrdering> newRoutes = routeOrderingRepository.findByNewItem(
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

                "\"cardNumber\":\""+ newItem.getItemNumber() +"\","+
                "\"cardName\": \""+ newItem.getName() +"\","+
                "\"classification\": \"" + newItem.getClassification().getClassification1().getName().toString()
                +"/" + newItem.getClassification().getClassification2().getName().toString()+
                ( newItem.getClassification().getClassification3().getId().equals(99999L)?
                        "":
                        "/" + newItem.getClassification().getClassification3().getName()
                )
                + "\","+
                "\"cardType\": \"" + newItem.getItemTypes().getItemType().name() +"\"," +
                "\"sharing\": \"" + (newItem.isSharing() ?"공용":"전용") +"\"," +
                "\"preliminaryBomId\": "+ preliminaryBom.getId() +"," +
                "\"children\": []" +
                "}"
                ;

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


        newRoute.getNewItem().updateTempsaveWhenMadeRoute();
        //라우트 만들면 임시저장 해제

        //0607 BOM + PRELIMINARY BOM 생성되게 하기
        newRoute.setBom(bom);

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
                routeProductRepository

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

        if (presentRouteProductCandidate.size() == routeOrdering.getPresent()) {
            //만약 present 가 끝까지 닿았으면 현재 complete 된 상황!
            routeOrdering.updateToComplete();
            //throw new UpdateImpossibleException();

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
                if (projectRepository.findByNewItem(routeOrdering.getNewItem()).size() == 0) {
                    throw new ProjectNotLinkedException();
                } else {
                    Project linkedProject =
                            projectRepository.findByNewItem(routeOrdering.getNewItem())
                                    .get(
                                            projectRepository.findByNewItem(routeOrdering.getNewItem()).size() - 1
                                    );
                    //그 프로젝트를 라우트 프로덕트에 set 해주기
                    targetRoutProduct.setProject(linkedProject);
                    //05-12 추가사항 : 이 라우트를 제작해줄 때야 비로소 프로젝트는 temp save = false 가 되는 것
                    linkedProject.finalSaveProject();
                }
            }

            /////////////////////////////////////////////////////////////////////////////////

//            else if (targetRoutProduct.getRoute_name().equals("기구Design생성[설계자]")) {
            // 06-17 리팩토링 : module : DESIGN , name : create
            else if (targetRoutProduct.getType().getModule().equals("DESIGN")
                    && targetRoutProduct.getType().getName().equals("CREATE")) {

                //아이템에 링크된 맨 마지막 (최신) 디자인 데려오기
                if (designRepository.findByNewItem(routeOrdering.getNewItem()).size() == 0) {
                    throw new DesignNotLinkedException();
                } else {
                    Design linkedDesign =
                            designRepository.findByNewItem(routeOrdering.getNewItem())
                                    .get(
                                            designRepository.findByNewItem(routeOrdering.getNewItem()).size() - 1
                                    );
                    //만약 지금 rejected 가 true였다면 , 이제 새로 다시 넣어주는 것이니깐 rejected풀어주기
                    if (targetRoutProduct.isPreRejected()) {
                        targetRoutProduct.setPreRejected(false); //06-01 손댐
                    }
                    //그 프로젝트를 라우트 프로덕트에 set 해주기
                    targetRoutProduct.setDesign(linkedDesign);
                    // 해당 design 의 임시저장을 false
                    //05-12 추가사항 : 이 라우트를 제작해줄 때야 비로소 프로젝트는 temp save = false 가 되는 것
                    linkedDesign.finalSaveDesign();
                }


            }

            else if (targetRoutProduct.getType().getModule().equals("DESIGN")
                    && targetRoutProduct.getType().getName().equals("REVIEW")) {

                //아이템에 링크된 맨 마지막 (최신) 디자인 데려오기
                if (designRepository.findByNewItem(routeOrdering.getNewItem()).size() == 0) {
                    throw new DesignNotLinkedException();
                } else {
                    Design linkedDesign =
                            designRepository.findByNewItem(routeOrdering.getNewItem())
                                    .get(
                                            designRepository.findByNewItem(routeOrdering.getNewItem()).size() - 1
                                    );

                    // 디자인 리뷰 승인 나면 아이템 정보 관계 맺어주기
                    bomService.makeDevBom(linkedDesign.getId());

                    // dev bom 의 temp new item parent children 관계도 맺어주기
                    bomService.makeTempDevBom(

                            linkedDesign.getId(),
                            true
                    );
                }


            }

            //06-27 데브 봄 만들고 라우트 요청하면 temp save = false & 라우트오더링에 봄 등록

            else if (targetRoutProduct.getType().getModule().equals("BOM")
                    && targetRoutProduct.getType().getName().equals("CREATE")) {

                //아이템에 링크된 봄 아이디 건네주기
                if (bomRepository.findByNewItem(routeOrdering.getNewItem()).size() == 0) {
                    throw new BomNotFoundException();
                } else {
                    Bom bom =
                            bomRepository.findByNewItem(routeOrdering.getNewItem())
                                    .get(
                                            bomRepository.findByNewItem(routeOrdering.getNewItem()).size() - 1
                                    );

                    //만약 지금 rejected 가 true였다면 , 이제 새로 다시 넣어주는 것이니깐 rejected풀어주기
                    if (targetRoutProduct.isPreRejected()) {
                        targetRoutProduct.setPreRejected(false);
                    }
                    //그 프로젝트를 라우트 프로덕트에 set 해주기
                    targetRoutProduct.setBom(bom);
                    // 해당 design 의 임시저장을 false
                    //05-12 추가사항 : 이 라우트를 제작해줄 때야 비로소 프로젝트는 temp save = false 가 되는 것
                    DevelopmentBom developmentBom =
                            developmentBomRepository.findByBom(bom);

                    developmentBom.updateTempSaveFalse();
                }
            }

            else if (targetRoutProduct.getType().getModule().equals("BOM")
                    && targetRoutProduct.getType().getName().equals("REVIEW")) {

                //아이템에 링크된 봄 아이디 건네주기
                if (bomRepository.findByNewItem(routeOrdering.getNewItem()).size() == 0) {
                    throw new BomNotFoundException();
                } else {
                    Bom bom =
                            bomRepository.findByNewItem(routeOrdering.getNewItem())
                                    .get(
                                            bomRepository.findByNewItem(routeOrdering.getNewItem()).size() - 1
                                    );

                    // 디자인 리뷰 승인 나면 아이템 정보 관계 맺어주기
                    bomService.makeFinalBom(bom);

                }


            }


            RouteOrderingUpdateRequest newRouteUpdateRequest =
                    routeOrdering
                            .update(
                                    req,
                                    routeProductRepository
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
                    rejectPossibleTypeId = 11L;
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