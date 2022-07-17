package eci.server.DocumentModule.repository;

import eci.server.DocumentModule.entity.classification.DocClassification2;
import eci.server.DocumentModule.entity.classification.DocTag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DocTagRepository  extends JpaRepository<DocTag, Long> {
    List<DocTag> findByDocClassification2(DocClassification2 docClassification2);
}
