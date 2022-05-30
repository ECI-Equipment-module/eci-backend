package eci.server.NewItemModule.repository.classification;

import eci.server.NewItemModule.entity.classification.Classification1;
import eci.server.NewItemModule.entity.classification.Classification2;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface Classification2Repository extends JpaRepository<Classification2, Long> {
        List<Classification2> findByClassification1(Classification1 classification1);
}
