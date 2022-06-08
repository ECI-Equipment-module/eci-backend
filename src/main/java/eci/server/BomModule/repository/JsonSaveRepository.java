package eci.server.BomModule.repository;

import eci.server.NewItemModule.entity.JsonSave;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JsonSaveRepository extends JpaRepository<JsonSave, Long> {
}
