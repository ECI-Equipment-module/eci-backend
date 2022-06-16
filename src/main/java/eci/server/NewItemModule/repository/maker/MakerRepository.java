package eci.server.NewItemModule.repository.maker;

import eci.server.NewItemModule.entity.supplier.Maker;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MakerRepository extends JpaRepository<Maker, Long> {
}
