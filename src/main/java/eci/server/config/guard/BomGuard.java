package eci.server.config.guard;

import eci.server.BomModule.repository.BomRepository;
import eci.server.BomModule.repository.PreliminaryBomRepository;
import eci.server.ItemModule.entity.member.RoleType;
import eci.server.ItemModule.entity.newRoute.RouteOrdering;
import eci.server.ItemModule.entity.newRoute.RouteProduct;
import eci.server.ItemModule.entity.newRoute.RouteProductMember;
import eci.server.ItemModule.exception.item.ItemNotFoundException;
import eci.server.ItemModule.repository.newRoute.RouteOrderingRepository;
import eci.server.ItemModule.repository.newRoute.RouteProductRepository;
import eci.server.NewItemModule.entity.NewItem;
import eci.server.NewItemModule.repository.item.NewItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
/**
 * 아이템 삭제, 수정은 작성자 및 관리자만 가능
 */
public class BomGuard {

    private final AuthHelper authHelper;
    private final NewItemRepository ItemRepository;
    private final RouteOrderingRepository routeOrderingRepository;
    private final RouteProductRepository routeProductRepository;
    private final NewItemRepository newItemRepository;
    private final BomRepository bomRepository;

    private final PreliminaryBomRepository preliminaryBomRepository;

    public boolean check(Long id) {

        return authHelper.isAuthenticated() && hasAuthority(id);

    }

    private boolean hasAuthority(Long id) {
        return hasAdminRole() || isResourceOwner(id);
        // 관리자 권한 검사가 자원의 소유자 검사 선행하도록
        // ||는 A 수행되면 B 수행 안됨
    }

    private boolean isResourceOwner(Long id) {

        NewItem Item = ItemRepository.findById(id).orElseThrow(
                () -> { throw new AccessDeniedException(""); }
        );
        //해당 아이템이 존재 유무 확인
        Long memberId = authHelper.extractMemberId();
        //요청자의 아이디 확인

        return Item.getMember().getId().equals(memberId);
    }

    private boolean hasAdminRole() {
        return authHelper.extractMemberRoles().contains(RoleType.ROLE_ADMIN);
    }


    public boolean isBomCreator(Long itemId) { //디자인 생성 라우트 담당자인지
        routeOrderingRepository.findByNewItem(
                newItemRepository.findById(itemId).orElseThrow(ItemNotFoundException::new)
        );

        RouteOrdering routeOrdering =
                routeOrderingRepository.findByNewItem(
                        newItemRepository.findById(itemId).orElseThrow(ItemNotFoundException::new)
                ).get(
                        routeOrderingRepository.findByNewItem(
                                newItemRepository.findById(itemId).orElseThrow(ItemNotFoundException::new)
                        ).size()-1
                );

        List<RouteProduct> routeProductList =
                routeProductRepository.findAllByRouteOrdering(routeOrdering);

        List<RouteProductMember> targetMem = new ArrayList<>();
        boolean result = false;

        for(RouteProduct routeProduct : routeProductList){
            if(routeProduct.getType().getModule().equals("BOM")
                    && routeProduct.getType().getName().equals("CREATE")){
                targetMem = routeProduct.getMembers();
            }
        }

        if(!(targetMem.size()==0)){
            Long memberId = authHelper.extractMemberId();
            for(RouteProductMember routeProductMember:targetMem){
                if(routeProductMember.getMember().getId().equals(memberId)){
                    result = true;
                    break;
                }
            }
        }

        return result;
    }


    public boolean isBomReviewer(Long itemId) { //디자인 리뷰 라우트 담당자인지
        routeOrderingRepository.findByNewItem(
                newItemRepository.findById(itemId).orElseThrow(ItemNotFoundException::new)
        );

        RouteOrdering routeOrdering =
                routeOrderingRepository.findByNewItem(
                        newItemRepository.findById(itemId).orElseThrow(ItemNotFoundException::new)
                ).get(
                        routeOrderingRepository.findByNewItem(
                                newItemRepository.findById(itemId).orElseThrow(ItemNotFoundException::new)
                        ).size()-1
                );

        List<RouteProduct> routeProductList =
                routeProductRepository.findAllByRouteOrdering(routeOrdering);

        List<RouteProductMember> targetMem = new ArrayList<>();
        boolean result = false;

        for(RouteProduct routeProduct : routeProductList){
            if(routeProduct.getType().getModule().equals("BOM")
                    && routeProduct.getType().getName().equals("REVIEW")){
                targetMem = routeProduct.getMembers();
            }
        }

        if(!(targetMem.size()==0)){
            Long memberId = authHelper.extractMemberId();
            for(RouteProductMember routeProductMember:targetMem){
                if(routeProductMember.getMember().getId().equals(memberId)){
                    result = true;
                    break;
                }
            }
        }

        return result;
    }


    public String reviewState(Long itemId){

        String result = null;

        routeOrderingRepository.findByNewItem(
                newItemRepository.findById(itemId).orElseThrow(ItemNotFoundException::new)
        );

        RouteOrdering routeOrdering =
                routeOrderingRepository.findByNewItem(
                        newItemRepository.findById(itemId).orElseThrow(ItemNotFoundException::new)
                ).get(
                        routeOrderingRepository.findByNewItem(
                                newItemRepository.findById(itemId).orElseThrow(ItemNotFoundException::new)
                        ).size()-1
                );

        List<RouteProduct> routeProductList =
                routeProductRepository.findAllByRouteOrdering(routeOrdering);
        if(isBomComplete(routeOrdering)) {
            result = "complete";
            // 봄 리뷰 후
        }

        else if(
                (isBeforeDesignReview(routeOrdering, routeProductList))

        ){
            result = "beforeReview"; //프릴리미너리 가능
        }

        else if(
                isBomCreate(routeOrdering, routeProductList)
            //봄 생성 전단계~봄 리뷰
        ) {
            result = "bomCreate";
        }

        else if(
                isBomReview(routeOrdering, routeProductList)
            // 봄 리뷰
        ) {
            result = "bomReview";
        }


        return result;
    }

    private boolean isBeforeDesignReview(RouteOrdering routeOrdering, List<RouteProduct> routeProductList){

        if(routeOrdering.getPresent()<routeProductList.size()) {

            String module = routeProductList.get(
                    routeOrdering.getPresent()
            ).getType().getModule();

            return (module.equals("ITEM") || module.equals("PROJECT") || module.equals("DESIGN") );
        }

        else { //complete 상태라면
            return false;
        }
    }

    private boolean isBomCreate(RouteOrdering routeOrdering, List<RouteProduct> routeProductList){

        if(routeOrdering.getPresent()<routeProductList.size()) {

            String module = routeProductList.get(
                    routeOrdering.getPresent()
            ).getType().getModule();

            String name = routeProductList.get(
                    routeOrdering.getPresent()
            ).getType().getName();

            return module.equals("BOM")&&name.equals("CREATE");
        }

        else { //complete 상태라면
            return false;
        }

    }

    private boolean isBomReview(RouteOrdering routeOrdering, List<RouteProduct> routeProductList){

        if(routeOrdering.getPresent()<routeProductList.size()) {

            String module = routeProductList.get(
                    routeOrdering.getPresent()
            ).getType().getModule();

            String name = routeProductList.get(
                    routeOrdering.getPresent()
            ).getType().getName();

            return module.equals("BOM")&&name.equals("REVIEW");

        }

        else { //complete 상태라면
            return false;
        }


    }

    public boolean isBomComplete(RouteOrdering routeOrdering){
        return routeOrdering.getLifecycleStatus().equals("COMPLETE");

    }

    public Long editBomId(Long itemId){
        return bomRepository.findByNewItem(
                newItemRepository.findById(itemId).orElseThrow(ItemNotFoundException::new)
        ).get(
                bomRepository.findByNewItem(
                        newItemRepository.findById(itemId).orElseThrow(ItemNotFoundException::new)
                ).size()-1
        ).getId();
        //사이즈가 0보다 크면 edit
    }

}
