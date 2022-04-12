package eci.server.ItemModule.repository.member;

import eci.server.ItemModule.entity.member.Role;
import eci.server.ItemModule.entity.member.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByRoleType(RoleType roleType);
}
