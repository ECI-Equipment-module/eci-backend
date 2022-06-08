package eci.server.BomModule.repository;

import eci.server.BomModule.entity.PreliminaryBom;
import eci.server.NewItemModule.entity.JsonSave;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JsonSaveRepository extends JpaRepository<JsonSave, Long> {

    List<JsonSave> findByPreliminaryBomId(Long preliminaryBomId);
}
