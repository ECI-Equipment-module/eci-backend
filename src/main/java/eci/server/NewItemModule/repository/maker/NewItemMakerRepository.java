package eci.server.NewItemModule.repository.maker;

import eci.server.NewItemModule.entity.NewItem;
import eci.server.NewItemModule.entity.maker.NewItemMaker;
import eci.server.NewItemModule.entity.supplier.Maker;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NewItemMakerRepository extends JpaRepository<NewItemMaker, Long>{
    List<NewItemMaker> findByNewItem(NewItem item);
    List<NewItemMaker> findByMaker(Maker maker);
}