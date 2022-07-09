package eci.server.CRCOModule.repository;

import eci.server.CRCOModule.entity.ChangeRequest;
import eci.server.CRCOModule.entity.CrAttachment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CrAttachmentRepository extends JpaRepository<CrAttachment, Long> {

    List<CrAttachment> findByChangeRequest(ChangeRequest changeRequest);
}
