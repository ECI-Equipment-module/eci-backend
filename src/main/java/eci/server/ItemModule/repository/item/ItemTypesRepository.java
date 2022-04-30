package eci.server.ItemModule.repository.item;

import eci.server.ItemModule.entity.item.ItemType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemTypesRepository extends JpaRepository<ItemType, Long> {

}
