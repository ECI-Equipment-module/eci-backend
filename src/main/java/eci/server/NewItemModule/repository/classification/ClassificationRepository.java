package eci.server.NewItemModule.repository.classification;

import eci.server.NewItemModule.entity.classification.Classification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClassificationRepository extends JpaRepository<Classification, Long> {
}
