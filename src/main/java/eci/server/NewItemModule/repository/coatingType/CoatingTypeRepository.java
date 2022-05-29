package eci.server.NewItemModule.repository.coatingType;

import eci.server.NewItemModule.entity.coating.CoatingType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CoatingTypeRepository extends JpaRepository<CoatingType, Long>, CustomCoatingTypeRepository {
}
