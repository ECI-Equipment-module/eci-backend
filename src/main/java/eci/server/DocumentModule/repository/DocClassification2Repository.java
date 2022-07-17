package eci.server.DocumentModule.repository;

import eci.server.DocumentModule.entity.classification.DocClassification1;
import eci.server.DocumentModule.entity.classification.DocClassification2;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DocClassification2Repository extends JpaRepository<DocClassification2, Long> {

    List<DocClassification2> findByDocClassification1(DocClassification1 docClassification1);

}
