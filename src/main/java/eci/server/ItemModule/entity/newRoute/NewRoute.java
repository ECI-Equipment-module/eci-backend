package eci.server.ItemModule.entity.newRoute;

import eci.server.ItemModule.dto.newRoute.NewRouteUpdateRequest;
import eci.server.ItemModule.dto.newRoute.RouteProductDto;
import eci.server.ItemModule.entity.item.Item;
import eci.server.ItemModule.entitycommon.EntityDate;
import eci.server.ItemModule.exception.route.RouteNotFoundException;
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

import static java.util.stream.Collectors.toList;

/**
 * 라우트프로덕트를 리스트로 가짐으로써
 * 순서를 정의해준다.
 */
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Slf4j
public class NewRoute extends EntityDate {

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

    public NewRoute(
            String type,
            Item item

    ){
        this.type = type;
        this.lifecycleStatus = "Development";
        this.revisedCnt = 0;
        this.present = 0;
        this.item = item;
    }

    public void setPresent(Integer present) {
        this.present = present;
    }

    public NewRouteUpdateRequest update(
            Long id,
            NewRouteUpdateRequest req,
            NewRouteRepository newRouteRepository,
            RouteProductRepository routeProductRepository

    ) {

        List<RouteProduct> routeProductList =
                routeProductRepository.findAllByNewRoute(this);

        /**
         * update에서 받은 코멘트를 현재 진행중인 routeProduct에 set
         */
        //지금 애는 패스
        //내 앞에 완료됐던 애는 pass로 바꿔주기
        routeProductList.get(this.present).setPassed(true);


        if(this.present<routeProductList.size()-1) {
            //지금 들어온 코멘트는 현재 애 다음에
            routeProductList.get(this.present + 1).setComment(req.getComment());
        }else{
            //만약 present가 size()-1과 같아진다면 다 왔다는 거다.
            System.out.println("complete");
            this.lifecycleStatus = "Release";
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
        //present를 이제 지금 승인해준 애로 갱신
        this.present = req.getPresent();

        return req;
    }

    public List<RouteProduct> rejectUpdate(
            Long id,
            String rejectedComment,
            Integer rejectedIndex,
            NewRouteRepository newRouteRepository,
            RouteProductRepository routeProductRepository


    ) {
//        NewRoute newRoute =
//                newRouteRepository
//                        .findById(id)
//                        .orElseThrow(RouteNotFoundException::new);

        /**
         * 현재 라우트에 딸린 라우트 생산물들
         */
        List<RouteProduct> routeProductList =
                routeProductRepository.findAllByNewRoute(this);

        /**
         * 라우트프로덕트 맨 마지막 인덱스 값 찾기 위한 변수
         */
        Integer lastIndexofRouteProduct =
                routeProductList.size()-1;

        /**
         * 거절 전의 아이 + 거절주체인 차례 아이도 passed = true로 해주기
         * (수행 된거니깐)
         */
        routeProductList.get(this.present).setPassed(true);
        routeProductList.get(this.present+1).setPassed(true);

        /**
         * 라우트의 present는 복제된 아이의 sequence 전으로 해주기
         */

        /**
         * 기존 애들 중에서 passed가 false인 애들의 show는 false로 변경해주기
         */
        for(RouteProduct routeProductshow : routeProductList){
            System.out.println("기존 라우트프로덕트들 eeeeeeeeeeeeeee");
            if(!routeProductshow.isPassed()){
                routeProductshow.setShow(false);//지나지 않은 애들은 화면에서 없애주기
                System.out.println(routeProductshow.getId());
            }
        }

        /**
         * 거부됐던 애의 reject 는 true 로 갱신하고
         * 거부한 차례 애의 comment엔 거부 코멘트(req에서 받은 것) set
         */
        routeProductList.get(rejectedIndex).setRejected(true);
        routeProductList.get(this.present+1).setComment(rejectedComment);
        /**
         * 거부된 라우트 하나 먼저 복제
         *
         *         // reject 시점의 인덱스부터 끝까지인덱스까지 생성
         *         // (모두 라우트는 이 라우트를 바라보도록
         *         // 특히 리젝트 된 아이 복제 대상애는 rejected= 1 로 설정해주고,
         *         // 라우트의 present 도 이 아이의 인덱스 값으로 변경시켜주자
         *         // comment들 싹 다 초기화해주고
         */
        System.out.println("새로 투입될 인덱스 이건 3이어야 한다");
        System.out.println(routeProductRepository.findAllByNewRoute(this).size());
        RouteProduct rejectedRouteProduct =
                new RouteProduct(
                        //sequence에 1 더해줘서 새로 생긴애부터 진행
                        routeProductRepository.findAllByNewRoute(this).size(),
                routeProductList.get(rejectedIndex).getType(),
                "default",
                false, //passed
                false,
                true,
                routeProductList.get(rejectedIndex).getMember(),
                routeProductList.get(rejectedIndex).getNewRoute()

                );
//
//        NewRoute targetRoute = newRouteRepository.findById(id).orElseThrow(RouteNotFoundException::new);
//        targetRoute.setPresent(rejectedRouteProduct.getSequence());
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

        System.out.println("지금 상황에선 거절대상 이후로 복제돼야 하는게 한개");
        System.out.println(duplicateRouteProductList.size());

        Integer sequence = rejectedRouteProduct.getSequence();
        List<RouteProduct> duplicateList = duplicateRouteProductList.stream().map(
                i -> new RouteProduct(
                        //자기 복제대상보다 1이 더 커야해 다들
                        routeProductRepository.findAllByNewRoute(this).size(),
                        i.getType(),
                        "default",
                        false, //passed
                        false, //rejected
                        true,
                        i.getMember(),
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

        addedRouteProductList.add(rejectedRouteProduct);

        for(RouteProduct routeProduct : duplicateList){
            System.out.println("거절된 애 이후 애들 추가 중");
            System.out.println(routeProduct.getType());
            addedRouteProductList.add(routeProduct);
        }

        System.out.println("추가적으로 생길 애들 길이야(두개여야해요 시발)");
        System.out.println(addedRouteProductList.size());

        return addedRouteProductList;
    }
}
