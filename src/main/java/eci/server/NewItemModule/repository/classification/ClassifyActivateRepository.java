package eci.server.NewItemModule.repository.classification;

import eci.server.NewItemModule.entity.activateAttributeClassification.ClassifyActivate;
import eci.server.NewItemModule.entity.activateAttributes.ActivateAttributes;
import eci.server.NewItemModule.entity.classification.Classification;
import eci.server.NewItemModule.entity.classification.Classification1;
import eci.server.NewItemModule.entity.classification.Classification2;
import eci.server.NewItemModule.entity.classification.Classification3;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClassifyActivateRepository extends JpaRepository<ClassifyActivate, Long> {
    List<ClassifyActivate> findByClassification(Classification classification);
}
