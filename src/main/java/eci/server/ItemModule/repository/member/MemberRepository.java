package eci.server.ItemModule.repository.member;

import eci.server.ItemModule.entity.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> , CustomMemberRepository{

    Optional<Member> findByEmail(String email);
    Optional<Member> findByUsername(String username);

    //Optional<Member> findById(Long id);//member_id 라고 되어있어서 이런 식으로 명명함

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);
}
