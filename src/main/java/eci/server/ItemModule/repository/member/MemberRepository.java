package eci.server.ItemModule.repository.member;

import eci.server.ItemModule.entity.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> , CustomMemberRepository{

    Optional<Member> findByEmail(String email);
    Optional<Member> findByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);
}
