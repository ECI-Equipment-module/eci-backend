package eci.server.ItemModule.repository.manufacture;

import eci.server.ItemModule.entity.item.Manufacture;
import eci.server.ItemModule.entity.item.Material;
import eci.server.ItemModule.repository.material.CustomMaterialRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ManufactureRepository extends JpaRepository<Manufacture, Long>, CustomManufactureRepository {

}
