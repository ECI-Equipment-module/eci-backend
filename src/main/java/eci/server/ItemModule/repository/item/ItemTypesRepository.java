package eci.server.ItemModule.repository.item;

import eci.server.ItemModule.entity.item.ItemTypes;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemTypesRepository extends JpaRepository<ItemTypes, Long> {

}
