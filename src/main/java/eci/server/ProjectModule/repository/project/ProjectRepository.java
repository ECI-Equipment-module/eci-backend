package eci.server.ProjectModule.repository.project;

import eci.server.ItemModule.entity.item.Item;
import eci.server.ProjectModule.entity.project.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository  extends JpaRepository<Project, Long>, CustomProjectRepository {

    Page<Project> findAll(Pageable pageable);

}
