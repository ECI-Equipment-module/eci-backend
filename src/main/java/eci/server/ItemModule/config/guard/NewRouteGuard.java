package eci.server.ItemModule.config.guard;


import eci.server.ItemModule.entity.member.RoleType;
import eci.server.ItemModule.entity.newRoute.NewRoute;
import eci.server.ItemModule.entity.newRoute.RouteProduct;
import eci.server.ItemModule.exception.route.RouteNotFoundException;
import eci.server.ItemModule.repository.newRoute.NewRouteRepository;
import eci.server.ItemModule.repository.newRoute.RouteProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class NewRouteGuard {
    private final AuthHelper authHelper;
    private final NewRouteRepository newRouteRepository;
    private final RouteProductRepository routeProductRepository;

    public boolean check(Long id) {
        System.out.println("newRouteGuachkeccccccccccccccc");
        System.out.println(newRouteRepository.findById(id));
        System.out.println(routeProductRepository.findAllByNewRoute(newRouteRepository.findById(id).orElseThrow()));
        System.out.println("newRouteGuddddddddachkeccccccccccccccc");
        return authHelper.isAuthenticated() && hasAuthority(id);
    }

    private boolean hasAuthority(Long id) {
        return hasAdminRole() || isResponsible(id);
    }

    private boolean isResponsible(Long id) {


        NewRoute newRoute =
                newRouteRepository.findById(id).orElseThrow(() -> { throw new RouteNotFoundException(); });

        System.out.println("newRouteGuarddddddddddddddd");
        //라우트에 딸린 routeProduct들
        List<RouteProduct> routeProduct =
                routeProductRepository.findAllByNewRoute(newRoute);

        Long memberId = authHelper.extractMemberId();

        //라우트에 딸린 routeProduct 애서 현재 진행 중인 애
        boolean result =
                routeProduct.get(newRoute.getPresent())
                        .getMember().getId().toString().equals(memberId.toString());

        System.out.println(result);
        return result;
    }



    private boolean hasAdminRole() {
        return authHelper.extractMemberRoles().contains(RoleType.ROLE_ADMIN);
    }
}