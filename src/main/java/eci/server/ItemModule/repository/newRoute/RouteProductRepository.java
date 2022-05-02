package eci.server.ItemModule.repository.newRoute;

import eci.server.ItemModule.entity.newRoute.RouteOrdering;
import eci.server.ItemModule.entity.newRoute.RouteProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RouteProductRepository extends JpaRepository<RouteProduct, Long> {

    List<RouteProduct> findAllByRouteOrdering(RouteOrdering routeOrdering);
}
