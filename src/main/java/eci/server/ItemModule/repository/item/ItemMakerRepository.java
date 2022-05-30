package eci.server.ItemModule.repository.item;

import eci.server.ItemModule.entity.item.Item;
import eci.server.ItemModule.entity.manufacture.ItemMaker;
import eci.server.NewItemModule.entity.supplier.Maker;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemMakerRepository extends JpaRepository<ItemMaker, Long>{

    ItemMaker findByItemAndMaker(Item item, Maker maker);
    List<ItemMaker> findByItem(Item item);
    List<ItemMaker> findByMaker(Maker maker);
}
