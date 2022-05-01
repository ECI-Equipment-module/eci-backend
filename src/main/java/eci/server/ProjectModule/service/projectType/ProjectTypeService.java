package eci.server.ProjectModule.service.projectType;

import eci.server.ProjectModule.dto.projectLevel.ProjectLevelListDto;
import eci.server.ProjectModule.dto.projectLevel.ProjectLevelReadCondition;
import eci.server.ProjectModule.dto.projectType.ProjectTypeListDto;
import eci.server.ProjectModule.dto.projectType.ProjectTypeReadCondition;
import eci.server.ProjectModule.repository.projectLevel.ProjectLevelRepository;
import eci.server.ProjectModule.repository.projectType.ProjectTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProjectTypeService {

    private final ProjectTypeRepository projectTypeRepository;

    public ProjectTypeListDto readAll(ProjectTypeReadCondition cond) {
        return ProjectTypeListDto.toDto(
                projectTypeRepository.findAllByCondition(cond)
        );
    }

}

