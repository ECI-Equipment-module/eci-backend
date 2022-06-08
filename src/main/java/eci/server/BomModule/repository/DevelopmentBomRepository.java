package eci.server.BomModule.repository;

import eci.server.BomModule.entity.Bom;
import eci.server.BomModule.entity.DevelopmentBom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DevelopmentBomRepository extends JpaRepository<DevelopmentBom, Long> {
    DevelopmentBom findByBom(Bom bom);
}
