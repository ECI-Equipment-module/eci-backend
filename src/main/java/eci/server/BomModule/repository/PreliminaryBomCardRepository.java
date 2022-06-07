package eci.server.BomModule.repository;

import eci.server.BomModule.entity.PreliminaryBomCard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PreliminaryBomCardRepository extends JpaRepository<PreliminaryBomCard, Long> {
}
