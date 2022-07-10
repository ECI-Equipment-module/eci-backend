package eci.server.CRCOModule.repository.cr;

import eci.server.CRCOModule.entity.cr.ChangeRequest;
import eci.server.ItemModule.entity.member.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChangeRequestRepository extends JpaRepository<ChangeRequest, Long>{

        Page<ChangeRequest> findAll(Pageable pageable);
        List<ChangeRequest> findByMember(Member member);

}