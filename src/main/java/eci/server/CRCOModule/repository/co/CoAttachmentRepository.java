package eci.server.CRCOModule.repository.co;

import eci.server.CRCOModule.entity.co.ChangeOrder;
import eci.server.CRCOModule.entity.cofeatures.CoAttachment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CoAttachmentRepository extends JpaRepository<CoAttachment, Long> {

    List<CoAttachment> findByChangeOrderByIdAsc(ChangeOrder changeOrder);
}
