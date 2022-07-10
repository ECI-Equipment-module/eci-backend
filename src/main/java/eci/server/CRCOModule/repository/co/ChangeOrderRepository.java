package eci.server.CRCOModule.repository.co;

import eci.server.CRCOModule.entity.co.ChangeOrder;
import eci.server.ItemModule.entity.member.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChangeOrderRepository extends JpaRepository<ChangeOrder, Long> {

    Page<ChangeOrder> findAll(Pageable pageable);

    List<ChangeOrder> findByMember(Member member);

}
