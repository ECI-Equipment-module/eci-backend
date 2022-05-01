package eci.server.ProjectModule.service.projectLevel;

import eci.server.ProjectModule.dto.produceOrg.ProduceOrganizationListDto;
import eci.server.ProjectModule.dto.produceOrg.ProduceOrganizationReadCondition;
import eci.server.ProjectModule.dto.projectLevel.ProjectLevelListDto;
import eci.server.ProjectModule.dto.projectLevel.ProjectLevelReadCondition;
import eci.server.ProjectModule.repository.produceOrg.ProduceOrganizationRepository;
import eci.server.ProjectModule.repository.projectLevel.ProjectLevelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProjectLevelService {

    private final ProjectLevelRepository projectLevelRepository;

    public ProjectLevelListDto readAll(ProjectLevelReadCondition cond) {
        return ProjectLevelListDto.toDto(
                projectLevelRepository.findAllByCondition(cond)
        );
    }

}
