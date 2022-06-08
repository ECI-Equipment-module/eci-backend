package eci.server.BomModule.repository;

import eci.server.BomModule.entity.Bom;
import eci.server.BomModule.entity.CompareBom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompareBomRepository extends JpaRepository<CompareBom, Long> {
    CompareBom findByBom(Bom bom);
}
