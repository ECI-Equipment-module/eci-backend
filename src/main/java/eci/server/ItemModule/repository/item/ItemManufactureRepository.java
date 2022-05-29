package eci.server.ItemModule.repository.item;

import eci.server.ItemModule.entity.item.Item;
import eci.server.ItemModule.entity.manufacture.ItemMaker;
import eci.server.ItemModule.entity.manufacture.Maker;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemManufactureRepository extends JpaRepository<ItemMaker, Long>{

    ItemMaker findByItemAndManufacture(Item item, Maker maker);
    List<ItemMaker> findByItem(Item item);
    List<ItemMaker> findByManufacture(Maker maker);
}
