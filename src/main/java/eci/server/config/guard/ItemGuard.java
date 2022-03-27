package eci.server.config.guard;

import eci.server.entity.item.Item;
import eci.server.entity.member.RoleType;
import eci.server.repository.item.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
/**
 * 아이템 삭제, 수정은 작성자 및 관리자만 가능
 */
public class ItemGuard {

    private final AuthHelper authHelper;
    private final ItemRepository ItemRepository;

    public boolean check(Long id) {

        return authHelper.isAuthenticated() && hasAuthority(id);

    }

    private boolean hasAuthority(Long id) {
        return hasAdminRole() || isResourceOwner(id);
        // 관리자 권한 검사가 자원의 소유자 검사 선행하도록
        // ||는 A 수행되면 B 수행 안됨
    }

    private boolean isResourceOwner(Long id) {

        Item Item = ItemRepository.findById(id).orElseThrow(
                () -> { throw new AccessDeniedException(""); }
        );
        //해당 아이템이 존재 유무 확인
        Long memberId = authHelper.extractMemberId();
        //요청자의 아이디 확인

        return Item.getMember().getId().equals(memberId);
    }

    private boolean hasAdminRole() {
        System.out.println("lassssssssadminrole");
        return authHelper.extractMemberRoles().contains(RoleType.ROLE_ADMIN);
    }
}