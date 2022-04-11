package eci.server.ItemModule.repository.item;

import eci.server.ItemModule.dto.item.AttachmentDto;
import eci.server.ItemModule.entity.item.Attachment;
import eci.server.ItemModule.entity.item.Item;
import eci.server.ItemModule.entity.route.Route;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AttachmentRepository extends JpaRepository<Attachment, Long> {

    List<Attachment> findByItem(Item item);

}
