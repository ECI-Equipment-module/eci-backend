package eci.server.NewItemModule.repository.item;

import eci.server.NewItemModule.entity.NewItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NewItemRepository extends JpaRepository<NewItem, Long>, CustomNewItemRepository {
}
