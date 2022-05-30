package eci.server.NewItemModule.repository.maker;

import eci.server.NewItemModule.entity.NewItem;
import eci.server.NewItemModule.entity.maker.NewItemMaker;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NewItemMakerRepository extends JpaRepository<NewItemMaker, Long>{
    List<NewItemMaker> findByNewItem(NewItem item);
}