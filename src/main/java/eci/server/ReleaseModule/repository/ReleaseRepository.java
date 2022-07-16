package eci.server.ReleaseModule.repository;

import eci.server.ItemModule.entity.member.Member;
import eci.server.ReleaseModule.entity.Releasing;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReleaseRepository extends JpaRepository<Releasing, Long> {

    List<Releasing> findByMember(Member member);

}


