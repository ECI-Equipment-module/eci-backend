package eci.server.CRCOModule.repository.features;

import eci.server.CRCOModule.entity.features.CrSource;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CrSourceRepository extends JpaRepository<CrSource, Long>, CustomCrSourceRepository {

}
