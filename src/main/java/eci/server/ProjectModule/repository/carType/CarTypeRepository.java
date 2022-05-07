package eci.server.ProjectModule.repository.carType;

import eci.server.ProjectModule.entity.project.CarType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarTypeRepository extends JpaRepository<CarType, Long>, CustomCarTypeRepository {
}
