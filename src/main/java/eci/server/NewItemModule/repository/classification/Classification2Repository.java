package eci.server.NewItemModule.repository.classification;

import eci.server.NewItemModule.entity.classification.Classification1;
import eci.server.NewItemModule.entity.classification.Classification2;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface Classification2Repository extends JpaRepository<Classification2, Long> {
        List<Classification2> findByClassification1(Classification1 classification1);

//        @Query("select c from Classification2 c join fetch c.classification3List where c.class")
//        List<Classification2> findAllByClassification2();


}
