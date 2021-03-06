package eci.server.ReleaseModule.repository;

import eci.server.ReleaseModule.entity.Releasing;
import eci.server.ReleaseModule.entity.ReleaseAttachment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReleaseAttachmentRepository extends JpaRepository<ReleaseAttachment, Long> {

    List<ReleaseAttachment> findByRelease(Releasing release);

}