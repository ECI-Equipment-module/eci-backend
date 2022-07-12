package eci.server.ItemModule.entity.newRoute;

import eci.server.BomModule.entity.Bom;
import eci.server.CRCOModule.entity.CoNewItem;
import eci.server.CRCOModule.entity.co.ChangeOrder;
import eci.server.CRCOModule.entity.cr.ChangeRequest;
import eci.server.CRCOModule.repository.co.CoNewItemRepository;
import eci.server.DesignModule.entity.design.Design;
import eci.server.ItemModule.dto.newRoute.routeOrdering.RouteOrderingUpdateRequest;
import eci.server.ItemModule.entitycommon.EntityDate;
import eci.server.ItemModule.exception.item.ItemNotFoundException;
import eci.server.ItemModule.exception.route.RejectImpossibleException;
import eci.server.ItemModule.exception.route.UpdateImpossibleException;
import eci.server.ItemModule.repository.newRoute.RouteOrderingRepository;
import eci.server.ItemModule.repository.newRoute.RouteProductRepository;
import eci.server.NewItemModule.dto.newItem.NewItemReadCondition;
import eci.server.NewItemModule.entity.NewItem;
import eci.server.NewItemModule.repository.item.NewItemRepository;
import eci.server.NewItemModule.service.item.NewItemService;
import eci.server.ProjectModule.entity.project.Project;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

/**
 * 라우트프로덕트를 리스트로 가짐으로써
 * 순서를 정의해준다.
 */
@Component
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Slf4j
public class RouteOrdering extends EntityDate {

    @Id

     @GeneratedValue(strategy = GenerationType.IDENTITY)
  //@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQUENCE1")
  //@SequenceGenerator(name="SEQUENCE1", sequenceName="SEQUENCE1", allocationSize=1)


    private Long id;

    /**
     * 요청 int로 받아서 지정할 때
     * routePreset.get(int)로 지정
     */
    @Column(nullable = false)
    private String type;

    /**
     * route에 딸린 routeProduct리스트의
     * 맨 마지막 인덱스 값 아이의 passed = 1 이면 complete,
     * passed = 0 이면 development
     * 나중에 갱신진행 시때도 development로
     */
    @Column(nullable = false)
    private String lifecycleStatus;

    /**
     * 처음엔 0, 갱신 요청 들어올 때마다 +1
     */
    @Column(nullable = false)
    private Integer revisedCnt;

    /**
     * 현재 진행중인 routeproduct의 인덱스
     */
    @Column(nullable = false)
    private Integer present;

    /**
     * null 가능, 아이템에서 라우트 생성 시 지정
     */

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "new_item_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private NewItem newItem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "design_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Design design;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bom_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Bom bom;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cr_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private ChangeRequest changeRequest;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "co_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private ChangeOrder changeOrder;



    //아이템 라우트용 생성자
    public RouteOrdering(
            String type,
            NewItem newItem

    ){
        this.type = type;
        this.lifecycleStatus = "WORKING";
        this.revisedCnt = 0;
        this.present = 1;
        this.newItem = newItem;
    }

    //cr
    // 생성자
    public RouteOrdering(
            String type,
            ChangeRequest changeRequest

    ){
        this.type = type;
        this.lifecycleStatus = "WORKING";
        this.revisedCnt = 0;
        this.present = 1;
        this.changeRequest = changeRequest;
    }

    //co
    //아이템 라우트용 생성자
    public RouteOrdering(
            String type,
            ChangeOrder co

    ){
        this.type = type;
        this.lifecycleStatus = "WORKING";
        this.revisedCnt = 0;
        this.present = 1;
        this.changeOrder = co;
    }

    //프로젝트 라우트용 생성자
    public RouteOrdering(
            String type,
            Project project

    ){
        this.type = type;
        this.lifecycleStatus = "WORKING";
        this.revisedCnt = 0;
        this.present = 1;
    }

    //revise 된 아이템 라우트용 생성자
    public RouteOrdering(
            Integer revised_cnt,
            String type,
            NewItem newItem

    ){
        this.type = type;
        this.lifecycleStatus = "WORKING";
        this.revisedCnt = 1; //revise 표시는 revisedCnt > 0
        this.present = 1;
        this.newItem = newItem;
    }


    public void setPresent(Integer present) {
        //초기 값은 1(진행 중인 아이)
        this.present = present;
    }

    public void setRevisedCnt(Integer revisedCnt) {
        this.revisedCnt = revisedCnt;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public void setChangeRequest(ChangeRequest changeRequest) {
        this.changeRequest = changeRequest;
    }

    public void setChangeOrder(ChangeOrder changeOrder) {
        this.changeOrder = changeOrder;
    }

    public void setDesign(Design design) {
        this.design = design;
    }

    public void setBom(Bom bom) {
        this.bom = bom;
    }

    /**
     * 일반 라우트
     * @param req
     * @param routeProductRepository
     * @return
     */
    public RouteOrderingUpdateRequest update(
            RouteOrderingUpdateRequest req,
            RouteProductRepository routeProductRepository,
            NewItemRepository newItemRepository,
            CoNewItemRepository coNewItemRepository,
            RouteOrderingRepository routeOrderingRepository,
            NewItemService newItemService

    ) {
        List<RouteProduct> routeProductList =
                routeProductRepository.findAllByRouteOrdering(this);

        //이미 승인 완료됐을 시에는 더이상 승인이 불가능해 에러 던지기
        if(this.present==routeProductList.size()){
            throw new UpdateImpossibleException();
        }


        /**
         * update에서 받은 코멘트를 현재 진행중인 routeProduct에 set
         */
        //지금 애는 패스
        //내 앞에 완료됐던 애는 pass로 바꿔주기
        routeProductList.get(this.present-1).setPassed(true);

        if(this.present<routeProductList.size()-1) {//05-24 <= => < 로 수정
            //지금 들어온 코멘트는 현재 애 다음에
            routeProductList.get(this.present).setComment(req.getComment());

            // 지금 업데이트되는 라우트 프로덕트의 타입이 create 라면
            if(routeProductList.get(this.present).getType().getName().equals("CREATE")
                    || routeProductList.get(this.present).getType().getName().equals("REQUEST")){
                // 모듈이 아이템
                if(routeProductList.get(this.present).getType().getModule().equals("ITEM")){
                    this.getNewItem().setTempsave(false); //06-18 라우트 만든 순간 임시저장 다시 거짓으로
                }
                // 모듈이 디자인
                else if(routeProductList.get(this.present).getType().getModule().equals("DESIGN")){
                    this.getDesign().setTempsave(false); //라우트 만든 순간 임시저장 다시 거짓으로
                }
                // 모듈이 프로젝트
                else if(routeProductList.get(this.present).getType().getModule().equals("PROJECT")){
                    this.getProject().setTempsave(false); //라우트 만든 순간 임시저장 다시 거짓으로
                }
                else if(routeProductList.get(this.present).getType().getModule().equals("BOM")){
                    this.getBom().setTempsave(false); //라우트 만든 순간 임시저장 다시 거짓으로
                }
                else if(routeProductList.get(this.present).getType().getModule().equals("CR")){
                    this.getChangeRequest().setTempsave(false); //라우트 만든 순간 임시저장 다시 거짓으로
                }
                else if(routeProductList.get(this.present).getType().getModule().equals("CO")
                        && routeProductList.get(this.present).getType().getName().equals("REQUEST")){
                    //얘는 create인 상태에선 ㄴㄴ 오로지 request 상태만 tempsave 여기서 false 돼야함
                    this.getChangeOrder().setTempsave(false); //라우트 만든 순간 임시저장 다시 거짓으로
                }
            }

        }else{
            routeProductList.get(this.present).setComment(req.getComment());
            //만약 present가 size() 가 됐다면 다 왔다는 거다.
            System.out.println("completeeeeeeeeee item route complete!!!! ");
            this.lifecycleStatus = "COMPLETE";

            RouteOrdering routeOrdering = routeProductList.get(this.present).getRouteOrdering();

            if(routeProductList.get(this.present).getRouteOrdering().getRevisedCnt()>0){
                //지금 승인된 라우트가 revise 로 인해 새로 생긴 아이템이라면
                routeOrdering.setRevisedCnt(0);
                //0710 revise 로 생긴 route ordering 이었다면 다시 0으로 복구;

                if(routeOrdering.getNewItem()==null){
                    //revise 인데도 안 묶여있으면 뭔가 잘못됐어,
                    throw new ItemNotFoundException();//에러 던지기
                }

                NewItem targetRevisedItem = newItemRepository.
                        findById(routeOrdering.getNewItem().getReviseTargetId()).orElseThrow(ItemNotFoundException::new);

                if (targetRevisedItem.isRevise_progress()) {
                    routeOrdering.getNewItem().setRevise_progress(false);
                    //0712 아기의 target route 가 revise progress 가 진행 중이라면 라우트 complete 될 때 false 로 갱신
                }

                if(coNewItemRepository.findByNewItemOrderByCreatedAtAsc(routeOrdering.getNewItem()).size()>0) {
                    // (1) 지금 revise 완료 된 아이템의 CO 를 검사하기 위해 check co 찾기
                    System.out.println("(1) 지금 revise 완료 된 아이템의 CO 를 검사하기 위해 check co 찾기");
                    ChangeOrder checkCo =
                            coNewItemRepository.findByNewItemOrderByCreatedAtAsc(routeOrdering.getNewItem()).get(
                                            coNewItemRepository.findByNewItemOrderByCreatedAtAsc(routeOrdering.getNewItem()).size()-1
                                    )//가장 최근에 맺어진 co-new item 관계 중 가장 최신 아이의 co를 검사하기
                                    .getChangeOrder();

                    // (2) check co 의 affected item 리스트
                    System.out.println("(2) check co 의 affected item 리스트");
                    List<CoNewItem> coNewItemsOfChkCo = checkCo.getCoNewItems();
                    List<NewItem> affectedItemOfChkCo = coNewItemsOfChkCo.stream().map(
                            i->i.getNewItem()
                    ).collect(Collectors.toList());

                    // (3) checkCo의 routeOrdering 찾아오기
                    System.out.println("(3) checkCo의 routeOrdering 찾아오기");
                    RouteOrdering routeOrderingOfChkCo =
                            routeOrderingRepository.findByChangeOrder(checkCo).get(
                                    routeOrderingRepository.findByChangeOrder(checkCo).size()-1
                            );

                    // (4) affected item 이 모두 revise 완료된다면 update route
                    System.out.println("(4) affected item 이 모두 revise 완료된다면 update route");
                    if(newItemService.checkReviseCompleted(affectedItemOfChkCo)){
                        routeOrderingOfChkCo.CoUpdate(routeProductRepository);
                    }

                }

                //throw new UpdateImpossibleException();
                // 0710 : 이 아이템과 엮인 아이들 (CHILDREN , PARENT )들의 REVISION +=1 진행 !
                // 대상 아이템들은 이미 각각 아이템 리뷰 / 프로젝트 링크할 때 REVISION+1 당함
                newItemService.revisionUpdateAllChildrenAndParentItem(routeOrdering.getNewItem());



            }


        }

        /**
         * 라우트프로덕트 맨 마지막 인덱스 값 찾기 위한 변수
         */
        Integer lastIndexofRouteProduct =
                routeProductList.size()-1;

        /**
         * 승인, 거부 시 갱신
         * 서비스 단에서 routeProduct 승인 혹은 거절 후
         * 라우트 업데이트 호출해서 present 갱신해줄거야
         */
        //present 를 다음 진행될 애로 갱신해주기
        if(this.present<routeProductList.size()) {
            this.present = this.present + 1;
        }



        return req;
    }

    /**
     * CO 를 위한 UPDATE 라우트
     */
    public void CoUpdate(
            RouteProductRepository routeProductRepository

    ) {
        List<RouteProduct> routeProductList =
                routeProductRepository.findAllByRouteOrdering(this);

        //이미 승인 완료됐을 시에는 더이상 승인이 불가능해 에러 던지기
        if(this.present==routeProductList.size()){
            throw new UpdateImpossibleException();
        }

        /**
         * update에서 받은 코멘트를 현재 진행중인 routeProduct에 set
         */
        //지금 애는 패스
        //내 앞에 완료됐던 애는 pass로 바꿔주기
        routeProductList.get(this.present-1).setPassed(true);


        if(this.present<routeProductList.size()-1) {//05-24 <= => < 로 수정
            //지금 들어온 코멘트는 현재 애 다음에
            routeProductList.get(this.present).setComment("자동 업데이트 완료오 오오오오");

            // 지금 업데이트되는 라우트 프로덕트의 타입은 무조건 co / create , 아니라면 에러
            if(!(routeProductList.get(this.present).getType().getName().equals("CO")
                    && routeProductList.get(this.present).getType().getName().equals("CREATE"))){
                System.out.println("SOMETHING IS WRRRRRRRRRRRROOOOOOOOONG");
                throw new RuntimeException();
            }

        }else{
            //만약 present가 size() 가 됐다면 다 왔다는 거다.
            System.out.println("cooooooooooooooo complete");
            this.lifecycleStatus = "COMPLETE";
        }

        /**
         * 라우트프로덕트 맨 마지막 인덱스 값 찾기 위한 변수
         */
        Integer lastIndexofRouteProduct =
                routeProductList.size()-1;

        /**
         * 승인, 거부 시 갱신
         * 서비스 단에서 routeProduct 승인 혹은 거절 후
         * 라우트 업데이트 호출해서 present 갱신해줄거야
         */
        //present 를 다음 진행될 애로 갱신해주기
        if(this.present<routeProductList.size()) {
            this.present = this.present + 1;
        }

    }


    public void updateToComplete() {
        this.lifecycleStatus="COMPLETE";
    }

    public List<RouteProduct> rejectUpdate(

            Long id,
            String rejectedComment,
            Integer rejectedIndex,
            RouteOrderingRepository routeOrderingRepository,
            RouteProductRepository routeProductRepository

    ) {
        /**
         * 현재 라우트에 딸린 라우트 생산물들
         */
        List<RouteProduct> routeProductList =
                routeProductRepository.findAllByRouteOrdering(this);

        if(rejectedIndex > this.present || routeProductList.get(rejectedIndex).isDisabled()){
            throw new RejectImpossibleException();
        }

        if(this.present<=routeProductList.size()-1) { //05-23 type 1 오류의 원인이었습니다.
            this.present = this.present + 1;
        }

        /**
         * 라우트프로덕트 맨 마지막 인덱스 값 찾기 위한 변수
         */
        Integer lastIndexofRouteProduct =
                routeProductList.size()-1;

        /**
         * 거절 전의 아이 + 거절주체인 차례 아이도 passed = true로 해주기
         * (수행 된거니깐)
         */

        routeProductList.get(this.present - 2).setPassed(true);//05-23 없애고비
        routeProductList.get(this.present - 1).setPassed(true);


        /**
         * 기존 애들 중에서 passed 가 false 인 애들의 show 는 false 로 변경해주기
         */
        for(RouteProduct routeProductshow : routeProductList){
            if(!routeProductshow.isPassed()){
                routeProductshow.setShow(false);
                //아직 지나지 않은 애들은 화면에서 없애주기
            }
            if(routeProductshow.getSequence()>rejectedIndex){
                //거부당한 인덱스보다 큰 애들은 쭉~ 무효화처리 (reject 대상이 되지 못해)
                routeProductshow.setDisabled(true);
            }
        }

        //06-17 : 거부된 라우트 프로덕트의 라우트 타입 검사

        // 1,9, 11, 13 에 따라서 tempSave 랑 readOnly 의 true,false 값 변경
        switch(routeProductList.get(rejectedIndex).getType().getId().toString()) {

            // 1 (아이템)
            case "1":
                this.getNewItem().setTempsave(true);
                this.getNewItem().setReadonly(false);
                break;
            // 9 (플젝)
            case "9":
                this.getProject().setTempsave(true);
                this.getProject().setReadonly(false);
                break;
            // 13 (디자인)
            case "13":
                this.getDesign().setTempsave(true);
                this.getDesign().setReadonly(false);
                break;
            //11 (봄)
            case "11":
                this.getBom().setTempsave(true);
                this.getBom().setReadonly(false);
                break;
            // 15 (cr)
            case "15":
                this.getChangeRequest().setTempsave(true);
                this.getChangeRequest().setReadonly(false);
                // 18 (CO REQUEST)
            case "18":
                this.getChangeOrder().setTempsave(true);
                this.getChangeOrder().setReadonly(false);
        }

        /**
         * 거부됐던 애의 reject 는 true 로 갱신하고 (거부받은자에게 알림용)
         * 거부한 차례 애의 comment엔 거부 코멘트(req에서 받은 것) set
         */
        routeProductList.get(rejectedIndex).setRejected(true);
        routeProductList.get(rejectedIndex).setDisabled(true);
        routeProductList.get(this.present-1).setComment(rejectedComment);
        routeProductList.get(this.present-1).updateRefusal(rejectedIndex); //06-01 : reject 된 seq을 전달
        //routeProductList.get(this.present).setShow(false);
        /**
         * 거부된 라우트 하나 먼저 복제
         *         // 특히 리젝트 된 아이 복제 대상애는 rejected= 1 로 설정해주고,
         *         // comment들 싹 다 초기화해주고
         */

        Integer seq = this.present;
        RouteProduct rejectedRouteProduct =
                new RouteProduct(
                        seq,
                        routeProductList.get(rejectedIndex).getOrigin_seq(),


                        routeProductList.get(rejectedIndex).getRoute_name(),

                        routeProductList.get(rejectedIndex).getType(),
                        "default",
                        false,
                        false,
                        true, //이전에 거절 당해서 만들어진 애라는 뜻
                        true,
                        false,
                        -1, //0527 - 거절당한 것, 거절자는 아니다
                        routeProductList.get(rejectedIndex)
                                .getMembers().stream().map(
                                        RouteProductMember::getMember
                                )
                                .collect(toList()),
                        routeProductList.get(rejectedIndex).getRouteOrdering(),
                        routeProductList.get(rejectedIndex).getProject(),
                        routeProductList.get(rejectedIndex).getDesign()
                );

        /**
         * 라우트의 present 도 이 아이의 인덱스 값으로 변경시켜주자. (이건 서비스에서)
         */

        /**
         * 거부된 라우트 이후부터
         * 쭉~~~ 끝 인덱스까지 슬라이싱 해서
         * rejectedList로 만들기
         * (얘네 복제할거임)
         */
        List <RouteProduct> duplicateRouteProductList =
                routeProductList.subList(
                        rejectedIndex+1,
                        lastIndexofRouteProduct+1
                        //sublist는 하나 0,3이라면 0,1,2 인덱스 복사
                );

        System.out.println("지금 상황에선 거절대상 이후로 복제돼야 하는 갯수");
        System.out.println(duplicateRouteProductList.size());

        AtomicReference<Integer> sequence = new AtomicReference<>(rejectedRouteProduct.getSequence());
        List<RouteProduct> duplicateList = duplicateRouteProductList.stream().map(
                i ->
                        new RouteProduct(
                                //자기 복제대상보다 1이 더 커야해 다들

                                sequence.updateAndGet(v -> v + 1),

                                i.getOrigin_seq(),

                                i.getRoute_name(),

                                i.getType(),
                                "default",
                                false, //passed
                                false, //rejected
                                false, //preRejected 돼서 만들어진 것이 아니니깐
                                true,
                                false,
                                -1,
                                i.getMembers().stream().map(
                                        m -> m.getMember()
                                ).collect(toList()),
                                i.getRouteOrdering()
                        )
        ).collect(
                toList()
        );
        List<RouteProduct> allRouteProductList =
                routeProductRepository.findAllByRouteOrdering(this);

        Integer lastIndex = allRouteProductList.size()-1;
        List<RouteProduct> addedRouteProductList =
                new ArrayList<>();

        //거절된 애 추가 중
        addedRouteProductList.add(rejectedRouteProduct);

        addedRouteProductList.addAll(duplicateList);

        System.out.println("추가적으로 생길 애들 길이");
        System.out.println(addedRouteProductList.size());

        return addedRouteProductList;
    }
}
