package eci.server.ItemModule.repository.item;

import eci.server.ItemModule.entity.item.Attachment;
import eci.server.ItemModule.entity.item.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AttachmentRepository extends JpaRepository<Attachment, Long> {

    List<Attachment> findByItem(Item item);

}
