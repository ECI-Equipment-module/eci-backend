package eci.server.ReleaseModule.repository;

import eci.server.ReleaseModule.entity.Release;
import eci.server.ReleaseModule.entity.ReleaseAttachment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReleaseAttachmentRepository extends JpaRepository<ReleaseAttachment, Long> {

    List<ReleaseAttachment> findByRelease(Release release);

}