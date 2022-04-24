package eci.server.ItemModule.entity.newRoute;

import eci.server.ItemModule.dto.newRoute.RouteOrderingUpdateRequest;
import eci.server.ItemModule.entity.item.Item;
import eci.server.ItemModule.entitycommon.EntityDate;
import eci.server.ItemModule.exception.item.ItemUpdateImpossibleException;
import eci.server.ItemModule.exception.route.RejectImpossibleException;
import eci.server.ItemModule.exception.route.UpdateImpossibleException;
import eci.server.ItemModule.repository.newRoute.RouteProductRepository;
import eci.server.ItemModule.repository.newRoute.NewRouteRepository;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static java.util.stream.Collectors.toList;

/**
 * 라우트프로덕트를 리스트로 가짐으로써
 * 순서를 정의해준다.
 */
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Slf4j
public class RouteOrdering extends EntityDate {

    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="SEQUENCE1")
    @SequenceGenerator(name="SEQUENCE1", sequenceName="SEQUENCE1", allocationSize=1)
    private Long id;

    /**
     * 요청 int로 받아서 지정할 때
     * NewRouteType.get(int)로 지정
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Item item;

    public RouteOrdering(
            String type,
            Item item

    ){
        this.type = type;
        this.lifecycleStatus = "WORKING";
        this.revisedCnt = 0;
        this.present = 1;
        this.item = item;
    }

    public void setPresent(Integer present) {
        //초기 값은 1(진행 중인 아이)
        this.present = present;
    }

    public RouteOrderingUpdateRequest update(
            RouteOrderingUpdateRequest req,
            RouteProductRepository routeProductRepository

    ) {
        List<RouteProduct> routeProductList =
                routeProductRepository.findAllByNewRoute(this);

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


        if(this.present<=routeProductList.size()-1) {
            //지금 들어온 코멘트는 현재 애 다음에
            routeProductList.get(this.present).setComment(req.getComment());
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

    public List<RouteProduct> rejectUpdate(

            Long id,
            String rejectedComment,
            Integer rejectedIndex,
            NewRouteRepository newRouteRepository,
            RouteProductRepository routeProductRepository

    ) {
      /**
         * 현재 라우트에 딸린 라우트 생산물들
         */
        List<RouteProduct> routeProductList =
                routeProductRepository.findAllByNewRoute(this);

        if(rejectedIndex > this.present || routeProductList.get(rejectedIndex).isDisabled()){
            throw new RejectImpossibleException();
        }

        if(this.present<routeProductList.size()-1) {
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
        routeProductList.get(this.present-1).setPassed(true);
        routeProductList.get(this.present).setPassed(true);

        /**
         * 기존 애들 중에서 passed가 false인 애들의 show는 false로 변경해주기
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

        /**
         * 거부됐던 애의 reject 는 true 로 갱신하고 (거부받은자에게 알림용)
         * 거부한 차례 애의 comment엔 거부 코멘트(req에서 받은 것) set
         */
        routeProductList.get(rejectedIndex).setRejected(true);
        routeProductList.get(rejectedIndex).setDisabled(true);
        routeProductList.get(this.present).setComment(rejectedComment);
        /**
         * 거부된 라우트 하나 먼저 복제
         *         // 특히 리젝트 된 아이 복제 대상애는 rejected= 1 로 설정해주고,
         *         // comment들 싹 다 초기화해주고
         */

        Integer seq = this.present+1;
        RouteProduct rejectedRouteProduct =
                new RouteProduct(
                    seq,
                    routeProductList.get(rejectedIndex).getOrigin_seq(),

                    routeProductList.get(rejectedIndex).getName(),
                    routeProductList.get(rejectedIndex).getType(),
                    "default",
                    false,
                    false,
                    true,
                    false,
                    routeProductList.get(rejectedIndex)
                            .getMembers().stream().map(
                            m -> m.getMember()
                    )
                            .collect(toList()),
                    routeProductList.get(rejectedIndex).getNewRoute()
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

                        i.getName(),
                        //routeProductRepository.findAllByNewRoute(this).size(),
                        i.getType(),
                        "default",
                        false, //passed
                        false, //rejected
                        true,
                        false,
                        i.getMembers().stream().map(
                                m -> m.getMember()
                        ).collect(toList()),
                        i.getNewRoute()
                )
        ).collect(
                toList()
        );
        List<RouteProduct> allRouteProductList =
                routeProductRepository.findAllByNewRoute(this);

        Integer lastIndex = allRouteProductList.size()-1;
        List<RouteProduct> addedRouteProductList =
                new ArrayList<>();

        //거절된 애 추가 중
        addedRouteProductList.add(rejectedRouteProduct);

        for(RouteProduct routeProduct : duplicateList){
            addedRouteProductList.add(routeProduct);
        }

        System.out.println("추가적으로 생길 애들 길이");
        System.out.println(addedRouteProductList.size());

        return addedRouteProductList;
    }
}
