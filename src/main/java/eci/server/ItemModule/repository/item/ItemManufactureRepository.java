package eci.server.ItemModule.repository.item;

import eci.server.ItemModule.entity.item.Item;
import eci.server.ItemModule.entity.manufacture.ItemManufacture;
import eci.server.ItemModule.entity.manufacture.Manufacture;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemManufactureRepository extends JpaRepository<ItemManufacture, Long>{

    ItemManufacture findByItemAndManufacture(Item item, Manufacture manufacture);
    List<ItemManufacture> findByItem(Item item);
    List<ItemManufacture> findByManufacture(Manufacture manufacture);
}
