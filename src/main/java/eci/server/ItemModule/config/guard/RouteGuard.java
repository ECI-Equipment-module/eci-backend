package eci.server.ItemModule.config.guard;


import eci.server.ItemModule.entity.member.RoleType;
import eci.server.ItemModule.entity.route.Route;
import eci.server.ItemModule.repository.route.RouteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class RouteGuard {
    private final AuthHelper authHelper;
    private final RouteRepository RouteRepository;

    public boolean check(Long id) {
        return authHelper.isAuthenticated() && hasAuthority(id);
    }

    private boolean hasAuthority(Long id) {
        return hasAdminRole() || isResourceOwner(id) || isResourceReviewer(id) || isResourceApprover(id);
    }

    private boolean isResourceOwner(Long id) {
        Route Route = RouteRepository.findById(id).orElseThrow(() -> { throw new AccessDeniedException(""); });
        Long memberId = authHelper.extractMemberId();
        return Route.getMember().getId().equals(memberId);
    }

    private boolean isResourceReviewer(Long id) {
        Route Route = RouteRepository.findById(id).orElseThrow(() -> { throw new AccessDeniedException(""); });
        Long memberId = authHelper.extractMemberId();
        return Route.getReviewer().getId().equals(memberId);
    }

    private boolean isResourceApprover(Long id) {
        Route Route = RouteRepository.findById(id).orElseThrow(() -> { throw new AccessDeniedException(""); });
        Long memberId = authHelper.extractMemberId();
        return Route.getApprover().getId().equals(memberId);
    }

    private boolean hasAdminRole() {
        return authHelper.extractMemberRoles().contains(RoleType.ROLE_ADMIN);
    }
}