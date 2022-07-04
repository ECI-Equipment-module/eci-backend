package eci.server.config.guard;


import eci.server.ItemModule.entity.member.RoleType;
import eci.server.ItemModule.entity.newRoute.RouteOrdering;
import eci.server.ItemModule.entity.newRoute.RouteProduct;
import eci.server.ItemModule.entity.newRoute.RouteProductMember;
import eci.server.ItemModule.exception.route.RouteNotFoundException;


import eci.server.ItemModule.repository.newRoute.RouteOrderingRepository;

import eci.server.ItemModule.repository.newRoute.RouteProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
@Transactional
public class NewRouteGuard {
    private final AuthHelper authHelper;

    private final RouteOrderingRepository routeOrderingRepository;

    private final RouteProductRepository routeProductRepository;

    public boolean check(Long id) {
        return authHelper.isAuthenticated() && hasAuthority(id);
    }

    private boolean hasAuthority(Long id) {
        return hasAdminRole() || isResponsible(id);
    }

    @Transactional
    private boolean isResponsible(Long id) {
        System.out.println("왜ㅐㅐㅐㅐㅐㅐㅐㅐㅐㅐㅐㅐㅐㅐㅐㅐㅐㅐㅐㅐㅐㅐㅐㅐㅐ");
        System.out.println(id);
        RouteOrdering newRoute =
                routeOrderingRepository.findById(id).orElseThrow(RouteNotFoundException::new);

        //라우트에 딸린 routeProduct들
        List<RouteProduct> routeProduct =
                routeProductRepository.findAllByRouteOrdering(newRoute);


        //현재 로그인 된 유저
        Long memberId = authHelper.extractMemberId();

        Integer targetIdx = newRoute.getPresent();
        //라우트에 딸린 routeProduct 애서 현재 진행 중인 애

        if(targetIdx == routeProduct.size()){
            targetIdx= targetIdx- 1;
            //인덱스 에러가 나서 일단은 이걸로 멤버 검사하고,
            //승인 완료 됐다는 에러를 routeOrdering에서 던지도록 설정
        }
        List<RouteProductMember> members = routeProduct.get(targetIdx).getMembers();

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