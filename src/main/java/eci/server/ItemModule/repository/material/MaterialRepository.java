package eci.server.ItemModule.repository.material;

import eci.server.ItemModule.entity.item.Material;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MaterialRepository extends JpaRepository<Material, Long>, CustomMaterialRepository {
}
