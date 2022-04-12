package eci.server.ItemModule.repository.member;

import eci.server.ItemModule.entity.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

<<<<<<< HEAD
public interface MemberRepository extends JpaRepository<Member, Long> , CustomMemberRepository{
=======
public interface MemberRepository extends JpaRepository<Member, Long> {
>>>>>>> 90002839b992be427ae0f3cbad4476b4f45af2b7

    Optional<Member> findByEmail(String email);
    Optional<Member> findByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);
}
