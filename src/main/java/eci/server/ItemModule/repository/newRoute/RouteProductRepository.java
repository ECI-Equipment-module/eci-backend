package eci.server.ItemModule.repository.newRoute;

import eci.server.ItemModule.entity.newRoute.RouteOrdering;
import eci.server.ItemModule.entity.newRoute.RouteProduct;
import eci.server.ProjectModule.entity.project.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RouteProductRepository extends JpaRepository<RouteProduct, Long> {

    List<RouteProduct> findAllByRouteOrdering(RouteOrdering routeOrdering);

    List<RouteProduct> findAllByProject(Project project);
}
