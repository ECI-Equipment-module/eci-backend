package eci.server.DocumentModule.repository;

import eci.server.DocumentModule.entity.Document;
import eci.server.DocumentModule.entity.DocumentAttachment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DocumentAttachmentRepository extends JpaRepository<DocumentAttachment, Long> {

    List<DocumentAttachment> findByDocument(Document document);

}
