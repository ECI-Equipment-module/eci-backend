package eci.server.ItemModule.repository.manufacture;

import eci.server.ItemModule.entity.manufacture.Maker;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MakerRepository extends JpaRepository<Maker, Long>, CustomMakerRepository {

}
