package eci.server.BomModule.repository;

import eci.server.BomModule.entity.Bom;
import eci.server.BomModule.entity.PreliminaryBom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PreliminaryBomRepository extends JpaRepository<PreliminaryBom, Long> {

    PreliminaryBom findByBom(Bom bom);

}
