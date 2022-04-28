package eci.server.ItemModule.repository.newRoute;

import eci.server.ItemModule.entity.newRoute.ItemRouteType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RouteTypeRepository extends JpaRepository<ItemRouteType, Long> {

    List<ItemRouteType> findByName(String name);

    ItemRouteType findByModuleAndName(String moduleName, String name);

}
