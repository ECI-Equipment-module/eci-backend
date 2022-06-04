package eci.server.NewItemModule.repository.attachment;

import eci.server.NewItemModule.entity.attachment.AttachmentTag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttachmentTagRepository extends JpaRepository<AttachmentTag, Long> {
}
