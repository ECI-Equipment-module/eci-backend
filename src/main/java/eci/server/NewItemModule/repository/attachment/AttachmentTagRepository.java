package eci.server.NewItemModule.repository.attachment;

import eci.server.NewItemModule.entity.attachment.AttachmentTag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AttachmentTagRepository extends JpaRepository<AttachmentTag, Long> {

    AttachmentTag findByName(String name);
    //AttachmentTag findByNameModule(String name, String module);
}
