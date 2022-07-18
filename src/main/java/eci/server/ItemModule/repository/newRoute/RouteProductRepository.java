package eci.server.ItemModule.repository.newRoute;

import eci.server.ItemModule.entity.newRoute.RouteOrdering;
import eci.server.ItemModule.entity.newRoute.RouteProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RouteProductRepository extends JpaRepository<RouteProduct, Long> {

    @Query("select c from RouteProduct c " +
            "where c.routeOrdering = :routeOrdering " +
            "order by c.id asc nulls first, c.id asc")
    List<RouteProduct> findAllByRouteOrdering(@Param("routeOrdering")RouteOrdering routeOrdering);


}
