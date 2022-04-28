package eci.server.ProjectModule.repository.projectType;

import eci.server.ProjectModule.entity.project.ProjectType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectTypeRepository extends JpaRepository<ProjectType, Long>, CustomProjectTypeRepository {
}


