package eci.server.NewItemModule.repository.attachment;

import eci.server.NewItemModule.entity.attachment.Classification1AttachmentTag;
import eci.server.NewItemModule.entity.classification.Classification1;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface Classification1AttachmentTagRepository extends JpaRepository<Classification1AttachmentTag, Long> {
    List<Classification1AttachmentTag> findByClassification1(Classification1 classification1);
}
