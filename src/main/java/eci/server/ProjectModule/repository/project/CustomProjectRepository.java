package eci.server.ProjectModule.repository.project;

import eci.server.ProjectModule.dto.project.ProjectReadCondition;
import eci.server.ProjectModule.dto.project.ProjectReadDto;
import org.springframework.data.domain.Page;

public interface CustomProjectRepository{
    //이 컨디션 성립안되는 것으로 추정
    Page<ProjectReadDto> findAllByCondition(ProjectReadCondition cond);

//    ProjectReadCondition 에 들어가있는 memberId
//    @Null
//    private Long memberId;
}
