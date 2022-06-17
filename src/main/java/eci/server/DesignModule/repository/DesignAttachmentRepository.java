package eci.server.DesignModule.repository;

import eci.server.DesignModule.entity.design.Design;
import eci.server.DesignModule.entity.designfile.DesignAttachment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DesignAttachmentRepository extends JpaRepository<DesignAttachment, Long> {
    List<DesignAttachment> findByDesign(Design design);
}
