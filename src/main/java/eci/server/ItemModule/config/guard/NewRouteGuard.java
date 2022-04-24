package eci.server.ItemModule.config.guard;


import eci.server.ItemModule.entity.member.RoleType;
import eci.server.ItemModule.entity.newRoute.RouteOrdering;
import eci.server.ItemModule.entity.newRoute.RouteProduct;
import eci.server.ItemModule.entity.newRoute.RouteProductMember;
import eci.server.ItemModule.exception.route.RouteNotFoundException;
import eci.server.ItemModule.repository.newRoute.NewRouteRepository;
import eci.server.ItemModule.repository.newRoute.RouteProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
@Transactional
public class NewRouteGuard {
    private final AuthHelper authHelper;
    private final NewRouteRepository newRouteRepository;
    private final RouteProductRepository routeProductRepository;

    public boolean check(Long id) {
        return authHelper.isAuthenticated() && hasAuthority(id);
    }

    private boolean hasAuthority(Long id) {
        return hasAdminRole() || isResponsible(id);
    }

    @Transactional
    private boolean isResponsible(Long id) {

        RouteOrdering newRoute =
                newRouteRepository.findById(id).orElseThrow(() -> { throw new RouteNotFoundException(); });

        //라우트에 딸린 routeProduct들
        List<RouteProduct> routeProduct =
                routeProductRepository.findAllByNewRoute(newRoute);

        //현재 로그인 된 유저
        Long memberId = authHelper.extractMemberId();

        //라우트에 딸린 routeProduct 애서 현재 진행 중인 애
        List<RouteProductMember> members = routeProduct.get(newRoute.getPresent()).getMembers();

        boolean result = false;


        for(RouteProductMember member : members){
            if(member.getMember().getId().toString().equals(memberId.toString())){
                result = true;
            };
        }

        return result;

    }

    private boolean hasAdminRole() {
        return authHelper.extractMemberRoles().contains(RoleType.ROLE_ADMIN);
    }
}