package eci.server.ProjectModule.repository.projectAttachmentRepository;

import eci.server.ProjectModule.entity.project.Project;
import eci.server.ProjectModule.entity.projectAttachment.ProjectAttachment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectAttachmentRepository extends JpaRepository<ProjectAttachment, Long> {
}
