//package eci.server.BomModule.repository;
//
//import eci.server.BomModule.entity.PreliminaryBom;
//import eci.server.BomModule.entity.PreliminaryBomCard;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//
//import java.util.List;
//import java.util.Optional;
//
//public interface PreliminaryBomCardRepository extends JpaRepository<PreliminaryBomCard, Long> {
//        @Query("select c from PreliminaryBomCard c " +
//                "left join fetch c.parent " +
//                "where c.id = :id")
//        Optional<PreliminaryBomCard> findWithParentById(Long id);
//
//        @Query("select c from PreliminaryBomCard c " +
//                "left join fetch c.parent " +
//                "where c.preliminaryBom.id = :preliminaryBomId " +
//                "order by c.parent.id asc nulls first, c.id asc")
//        List<PreliminaryBomCard> findAllWithParentByPreliminaryBomIdOrderByParentIdAscNullsFirstPreliminaryBomIdAsc
//                (@Param("preliminaryBomId") Long preliminaryBomId);
//
//        List<PreliminaryBomCard> findByPreliminaryBom(PreliminaryBom preliminaryBom);
//}
//
