package eci.server.CRCOModule.repository.co;

import eci.server.CRCOModule.entity.CoCoEffect;
import eci.server.CRCOModule.entity.co.ChangeOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CoCoEffectRepository extends JpaRepository<CoCoEffect, Long> {

    List<CoCoEffect> findByChangeOrder(ChangeOrder changeOrder);

}
