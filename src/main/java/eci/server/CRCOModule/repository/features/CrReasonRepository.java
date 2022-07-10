package eci.server.CRCOModule.repository.features;

import eci.server.CRCOModule.entity.features.CrReason;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CrReasonRepository extends JpaRepository<CrReason, Long>, CustomCrReasonRepository {
}
