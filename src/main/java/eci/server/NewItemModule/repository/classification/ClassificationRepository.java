package eci.server.NewItemModule.repository.classification;

import eci.server.NewItemModule.entity.classification.Classification;
import eci.server.NewItemModule.entity.classification.Classification1;
import eci.server.NewItemModule.entity.classification.Classification2;
import eci.server.NewItemModule.entity.classification.Classification3;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClassificationRepository extends JpaRepository<Classification, Long> {
    Classification findByClassification1AndClassification2AndClassification3(
            Classification1 classification1,
            Classification2 classification2,
            Classification3 classification3
    );
}
