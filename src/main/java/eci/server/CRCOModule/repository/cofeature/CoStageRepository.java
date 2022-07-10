package eci.server.CRCOModule.repository.cofeature;

import eci.server.CRCOModule.entity.cofeatures.CoStage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CoStageRepository  extends JpaRepository<CoStage, Long>, CustomCoStageRepository {

}