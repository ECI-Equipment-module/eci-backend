package eci.server.ItemModule.repository.manufacture;

import eci.server.ItemModule.entity.manufacture.Manufacture;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ManufactureRepository extends JpaRepository<Manufacture, Long>, CustomManufactureRepository {

}
