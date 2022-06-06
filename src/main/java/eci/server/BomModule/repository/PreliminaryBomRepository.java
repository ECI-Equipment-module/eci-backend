package eci.server.BomModule.repository;

import eci.server.BomModule.entity.PreliminaryBom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PreliminaryBomRepository extends JpaRepository<PreliminaryBom, Long> {
}
