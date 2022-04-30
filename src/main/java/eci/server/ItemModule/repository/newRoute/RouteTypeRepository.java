package eci.server.ItemModule.repository.newRoute;

import eci.server.ItemModule.entity.newRoute.RouteType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RouteTypeRepository extends JpaRepository<RouteType, Long> {

    List<RouteType> findByName(String name);

    RouteType findByModuleAndName(String moduleName, String name);

}
