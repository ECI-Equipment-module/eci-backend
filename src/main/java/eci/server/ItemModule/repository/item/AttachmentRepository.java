package eci.server.ItemModule.repository.item;

import eci.server.ItemModule.entity.item.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttachmentRepository extends JpaRepository<Attachment, Long> {
}
