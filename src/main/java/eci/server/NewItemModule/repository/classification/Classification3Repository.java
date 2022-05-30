package eci.server.NewItemModule.repository.classification;

import eci.server.NewItemModule.entity.classification.Classification2;
import eci.server.NewItemModule.entity.classification.Classification3;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface Classification3Repository extends JpaRepository<Classification3, Long> {

        List<Classification3> findByClassification2(Classification2 classification2);
}
