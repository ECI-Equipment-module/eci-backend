package eci.server.NewItemModule.repository.coatingWay;

import eci.server.NewItemModule.entity.coating.CoatingWay;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CoatingWayRepository extends JpaRepository<CoatingWay, Long>, CustomCoatingWayRepository {
}
