package eci.server.CRCOModule.repository.co;

import eci.server.CRCOModule.entity.co.ChangeOrder;
import eci.server.CRCOModule.entity.CoNewItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CoNewItemRepository extends JpaRepository<CoNewItem, Long> {

    List<CoNewItem> findByChangeOrder(ChangeOrder changeOrder);


}