package eci.server.CRCOModule.repository.cofeature;

import eci.server.CRCOModule.entity.cofeatures.ChangedFeature;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChangedFeatureRepository extends JpaRepository<ChangedFeature, Long>, CustomChangedFeatureRepository {

}

