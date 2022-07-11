package eci.server.CRCOModule.repository.cr;

import eci.server.CRCOModule.entity.cr.ChangeRequest;
import eci.server.ItemModule.entity.member.Member;
import eci.server.NewItemModule.entity.NewItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChangeRequestRepository extends JpaRepository<ChangeRequest, Long>{

        Page<ChangeRequest> findAll(Pageable pageable);
        List<ChangeRequest> findByMember(Member member);


        @Query(
                "select i from ChangeRequest " +
                        "i where i IN (:ChangeRequests)"
        )
        Page<ChangeRequest> findByChangeRequests
                (@Param("ChangeRequests")
                         List<ChangeRequest> cr, Pageable pageable);


}