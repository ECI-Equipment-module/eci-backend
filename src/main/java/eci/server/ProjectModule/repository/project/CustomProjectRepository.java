package eci.server.ProjectModule.repository.project;

import eci.server.ProjectModule.dto.ProjectReadCondition;
import eci.server.ProjectModule.dto.ProjectReadDto;
import eci.server.ProjectModule.entity.project.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomProjectRepository{
    Page<ProjectReadDto> findAllByCondition(ProjectReadCondition cond);
}
