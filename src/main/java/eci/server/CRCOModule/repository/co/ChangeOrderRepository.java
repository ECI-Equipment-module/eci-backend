package eci.server.CRCOModule.repository.co;

import eci.server.CRCOModule.entity.co.ChangeOrder;
import eci.server.ItemModule.entity.member.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChangeOrderRepository extends JpaRepository<ChangeOrder, Long> {

    Page<ChangeOrder> findAll(Pageable pageable);

    List<ChangeOrder> findByMember(Member member);

    @Query(
            "select i from ChangeOrders " +
                    "i where i IN (:changeOrders)"
    )
    Page<ChangeOrder> findByChangeOrderByIdAscs(@Param("changeOrders")
                                                 List<ChangeOrder> changeOrders, Pageable pageable);

    List<ChangeOrder> findAllByOrderByIdAsc();
    
}
