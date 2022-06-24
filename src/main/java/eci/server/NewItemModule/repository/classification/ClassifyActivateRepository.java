package eci.server.NewItemModule.repository.classification;

import eci.server.NewItemModule.entity.activateAttributeClassification.ClassifyActivate;
import eci.server.NewItemModule.entity.classification.Classification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClassifyActivateRepository extends JpaRepository<ClassifyActivate, Long> {
    List<ClassifyActivate> findByClassification(Classification classification);
}
