package eci.server.ProjectModule.repository.project;

import eci.server.ProjectModule.entity.project.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Long>{
}
