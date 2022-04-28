package eci.server.ProjectModule.repository.projectLevel;

import eci.server.ProjectModule.dto.projectLevel.ProjectLevelReadCondition;
import eci.server.ProjectModule.dto.projectLevel.ProjectLevelReadResponse;
import org.springframework.data.domain.Page;

public interface CustomProjectLevelRepository {
    Page<ProjectLevelReadResponse> findAllByCondition(ProjectLevelReadCondition cond);
}