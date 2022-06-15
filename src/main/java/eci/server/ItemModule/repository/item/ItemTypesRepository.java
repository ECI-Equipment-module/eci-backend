package eci.server.ItemModule.repository.item;
import eci.server.ItemModule.entity.item.ItemType;
import eci.server.ItemModule.entity.item.ItemTypes;
import eci.server.NewItemModule.entity.classification.Classification;
import eci.server.NewItemModule.entity.classification.Classification1;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemTypesRepository extends JpaRepository<ItemTypes, Long> {

    List<ItemTypes> findByClassification1(Classification1 classification1);

    ItemTypes findByItemType(ItemType itemType);
}
