package eci.server.ItemModule.repository.item;

import eci.server.ItemModule.entity.material.ItemMaterial;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemMaterialRepository extends JpaRepository<ItemMaterial, Long> {

}
