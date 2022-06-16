package eci.server.NewItemModule.repository.attachment;

//import eci.server.ItemModule.entity.item.Item;
import eci.server.NewItemModule.entity.NewItem;
import eci.server.NewItemModule.entity.NewItemAttachment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NewItemAttachmentRepository extends JpaRepository<NewItemAttachment, Long> {
    List<NewItemAttachment> findByNewItem(NewItem newItem);
}
