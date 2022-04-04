package eci.server.repository.member;

import eci.server.entity.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

<<<<<<< HEAD
    Optional<Member> findByEmail(String email);
    Optional<Member> findByUsername(String username);

    boolean existsByEmail(String email);
=======
    Optional<Member> findByEmail(String email); // 1
    Optional<Member> findByUsername(String username); // 1

    boolean existsByEmail(String email); // 3
>>>>>>> e143a8c189dadeaf9a9cad53c67ea454e93f5b71
    boolean existsByUsername(String username);
}
