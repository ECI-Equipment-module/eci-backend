package eci.server.ProjectModule.repository.projectType;

import eci.server.ProjectModule.dto.projectType.ProjectTypeReadCondition;
import eci.server.ProjectModule.dto.projectType.ProjectTypeReadResponse;
import org.springframework.data.domain.Page;

public interface CustomProjectTypeRepository {
    Page<ProjectTypeReadResponse> findAllByCondition(ProjectTypeReadCondition cond);

}

