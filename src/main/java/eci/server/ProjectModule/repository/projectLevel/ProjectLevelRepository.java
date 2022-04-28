package eci.server.ProjectModule.repository.projectLevel;

import eci.server.ProjectModule.entity.project.ProjectLevel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectLevelRepository extends JpaRepository<ProjectLevel, Long>, CustomProjectLevelRepository {
}
