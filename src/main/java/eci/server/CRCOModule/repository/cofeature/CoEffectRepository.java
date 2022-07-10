package eci.server.CRCOModule.repository.cofeature;

import eci.server.CRCOModule.entity.cofeatures.CoEffect;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CoEffectRepository extends JpaRepository<CoEffect, Long>, CustomCoEffectRepository {

}
