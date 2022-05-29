package eci.server.ItemModule.repository.manufacture;

import eci.server.ItemModule.entity.item.Manufacture;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MakerRepository extends JpaRepository<Manufacture, Long>, CustomMakerRepository {

}
