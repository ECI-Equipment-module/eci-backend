package eci.server.ItemModule.entity.newRoute;

import eci.server.BomModule.entity.Bom;
import eci.server.DesignModule.entity.design.Design;
import eci.server.ItemModule.dto.newRoute.routeOrdering.RouteOrderingUpdateRequest;
import eci.server.ItemModule.entitycommon.EntityDate;
import eci.server.ItemModule.exception.route.RejectImpossibleException;
import eci.server.ItemModule.exception.route.UpdateImpossibleException;
import eci.server.ItemModule.repository.newRoute.RouteOrderingRepository;
import eci.server.ItemModule.repository.newRoute.RouteProductRepository;
import eci.server.NewItemModule.entity.NewItem;
import eci.server.ProjectModule.entity.project.Project;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.time.format.DecimalStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

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

//    /**
//     * null 가능, 플젝에서 라우트 생성 시 지정
//     */
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "project_id")
//    @OnDelete(action = OnDeleteAction.CASCADE)
//    private Project project;

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

    public void setPresent(Integer present) {
        //초기 값은 1(진행 중인 아이)
        this.present = present;
    }

    public void setProject(Project project) {
        this.project = project;
    }


    public void setDesign(Design design) {
        this.design = design;
    }

    public void setBom(Bom bom) {
        this.bom = bom;
    }

    public RouteOrderingUpdateRequest update(
            RouteOrderingUpdateRequest req,
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
            routeProductList.get(this.present).setComment(req.getComment());

            // 지금 업데이트되는 라우트 프로덕트의 타입이 create 라면
            if(routeProductList.get(this.present).getType().getName().equals("CREATE")){
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
                    this.getProject().setTempsave(false); //라우트 만든 순간 임시저장 다시 거짓으로
                }
            }

        }else{
            //만약 present가 size() 가 됐다면 다 왔다는 거다.
            System.out.println("complete");
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



        return req;
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
