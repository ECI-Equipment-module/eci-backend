package eci.server.ProjectModule.repository.project;

import eci.server.ItemModule.entity.member.Member;
import eci.server.ProjectModule.dto.ProjectReadCondition;
import eci.server.ProjectModule.dto.ProjectReadDto;
import org.springframework.data.domain.Page;


public interface CustomProjectRepository{
    Page<ProjectReadDto> findAllByConditionAndMember(ProjectReadCondition cond, Member member);
//    ProjectReadCondition 에 들어가있는 memberId
//    @Null
//    private Long memberId;
}
