package eci.server.ItemModule.repository.item;

import eci.server.ItemModule.entity.item.Item;
import eci.server.ItemModule.entity.item.ItemManufacture;
import eci.server.ItemModule.entity.item.Manufacture;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ItemManufactureRepository extends JpaRepository<ItemManufacture, Long>{

    ItemManufacture findByItemAndManufacture(Optional<Item> item, Optional<Manufacture> manufacture);
    List<ItemManufacture> findByItem(Item item);
}
