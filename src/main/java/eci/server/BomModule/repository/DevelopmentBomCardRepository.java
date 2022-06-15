package eci.server.BomModule.repository;

import eci.server.BomModule.entity.DevelopmentBom;
import eci.server.BomModule.entity.DevelopmentBomCard;
import eci.server.BomModule.entity.PreliminaryBom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DevelopmentBomCardRepository extends JpaRepository<DevelopmentBomCard, Long> {
        @Query("select c from DevelopmentBomCard c " +
                "left join fetch c.parent " +
                "where c.id = :id")
        Optional<DevelopmentBomCard> findWithParentById(Long id);

        @Query("select c from DevelopmentBomCard c " +
                "left join fetch c.parent " +
                "where c.developmentBom.id = :developmentBomId " +
                "order by c.parent.id asc nulls first, c.id asc")
        List<DevelopmentBomCard> findAllWithParentByDevelopmentBomIdOrderByParentIdAscNullsFirstPreliminaryBomIdAsc
                (@Param("developmentBomId") Long developmentBomId);

        List<DevelopmentBomCard> findByDevelopmentBom(DevelopmentBom developmentBom);
}

