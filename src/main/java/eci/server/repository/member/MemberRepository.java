package eci.server.repository.member;

import eci.server.entity.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmail(String email); // 1
    Optional<Member> findByUsername(String username); // 1

    boolean existsByEmail(String email); // 3

    boolean existsByUsername(String username);
}
