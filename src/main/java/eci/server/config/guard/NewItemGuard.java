package eci.server.config.guard;

import eci.server.ItemModule.entity.item.Item;
import eci.server.ItemModule.entity.member.RoleType;
import eci.server.ItemModule.repository.item.ItemRepository;
import eci.server.NewItemModule.entity.NewItem;
import eci.server.NewItemModule.repository.item.NewItemRepository;
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
public class NewItemGuard {

    private final AuthHelper authHelper;
    private final NewItemRepository newItemRepository;

    public boolean check(Long id) {

        return authHelper.isAuthenticated() && hasAuthority(id);

    }

    private boolean hasAuthority(Long id) {
        return hasAdminRole() || isResourceOwner(id);
        // 관리자 권한 검사가 자원의 소유자 검사 선행하도록
        // ||는 A 수행되면 B 수행 안됨
    }

    private boolean isResourceOwner(Long id) {

        NewItem Item = newItemRepository.findById(id).orElseThrow(
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
}