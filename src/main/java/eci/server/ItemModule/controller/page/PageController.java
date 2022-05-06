package eci.server.ItemModule.controller.page;

import eci.server.ItemModule.dto.item.ItemProjectDashboardDto;
import eci.server.ItemModule.dto.item.ItemProjectDto;
import eci.server.ItemModule.dto.item.ItemSimpleDto;
import eci.server.ItemModule.entity.item.Item;
import eci.server.ItemModule.exception.member.MemberNotFoundException;
import eci.server.ItemModule.repository.item.ItemRepository;
import eci.server.ItemModule.repository.member.MemberRepository;
import eci.server.ItemModule.repository.newRoute.RouteOrderingRepository;
import eci.server.ItemModule.repository.newRoute.RouteProductRepository;
import eci.server.ProjectModule.dto.ProjectDashboardDto;
import eci.server.ProjectModule.dto.project.ProjectMemberRequest;
import eci.server.ProjectModule.dto.project.ProjectSimpleDto;
import eci.server.ProjectModule.entity.project.Project;
import eci.server.ProjectModule.entity.projectAttachment.ProjectAttachment;
import eci.server.ProjectModule.exception.ProjectNotFoundException;
import eci.server.ProjectModule.repository.project.ProjectRepository;
import eci.server.ProjectModule.service.ProjectService;
import eci.server.aop.AssignMemberId;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import javax.validation.constraints.Null;
import java.util.stream.Collectors;
@Transactional
@RestController
@RequiredArgsConstructor
public class PageController {

    @Autowired
    ItemRepository itemRepository;

    @CrossOrigin(origins = "https://localhost:3000")
    @GetMapping("/item/page")
    public Page<ItemSimpleDto> paging(@PageableDefault(size=5)
                                      @SortDefault.SortDefaults({
                                              @SortDefault(
                                                      sort = "createdAt",
                                                      direction = Sort.Direction.DESC)
                                      })
                                      Pageable pageRequest) {

        Page<Item> itemList = itemRepository.findAll(pageRequest);

        Page<ItemSimpleDto> pagingList = itemList.map(
                item -> new ItemSimpleDto(

                        item.getId(),
                        item.getName(),
                        item.getType(),
                        item.getItemNumber(),
                        item.getWidth(),
                        item.getHeight(),
                        item.getWeight(),
                        item.getMember().getUsername(),
                        item.getMaterials().get(0).getMaterial().getName(),
                        item.getColor().getColor(),
                        item.getThumbnail().get(0).getImageaddress(),
                        item.getAttachments().stream().map(
                                a -> a.getAttachmentaddress()
                        ).collect(Collectors.toList()),
                        item.getCreatedAt()

                )
        );

        return pagingList;
    }

    /**
     * 프로젝트 모듈에서의 프로젝트 리스트 (내가 만든 프로젝트들 maybe..?)
     */
    @Autowired
    ProjectRepository projectRepository;
    @CrossOrigin(origins = "https://localhost:3000")
    @GetMapping("/project/page")
    public Page<ProjectSimpleDto> pagingProject(@PageableDefault(size=5)
                                                @SortDefault.SortDefaults({
                                                        @SortDefault(
                                                                sort = "createdAt",
                                                                direction = Sort.Direction.DESC)
                                                })
                                                        Pageable pageRequest) {

        Page<Project> projectList = projectRepository.findAll(pageRequest);

        Page<ProjectSimpleDto> pagingList = projectList.map(
                project -> new ProjectSimpleDto(

                        project.getId(),
                        project.getProjectNumber(),
                        project.getName(),
                        project.getCarType(),

                        ItemProjectDto.toDto(project.getItem()),

                        project.getRevision(),
                        project.getStartPeriod(),
                        project.getOverPeriod(),

                        project.getTempsave(),

                        //tag가 개발
                        project.getProjectAttachments().stream().filter(
                                        a -> a.getTag().equals("DEVELOP")
                                ).collect(Collectors.toList())
                                .stream().map(
                                        ProjectAttachment::getAttachmentaddress
                                ).collect(Collectors.toList()),

                        //tag가 디자인
                        project.getProjectAttachments().stream().filter(
                                        a -> a.getTag().equals("DESIGN")
                                ).collect(Collectors.toList())
                                .stream().map(
                                        ProjectAttachment::getAttachmentaddress
                                ).collect(Collectors.toList()),


                        project.getCreatedAt()

                )
        );

        return pagingList;
    }

//    @Autowired
//    @CrossOrigin(origins = "https://localhost:3000")
//    @GetMapping("project/page")
//    @AssignMemberId //작성한 멤버
//    public Page<ProjectDashboardDto> Project(
//            @PageableDefault(size=5)
//            @SortDefault.SortDefaults
//                    (
//                            {
//                                    @SortDefault(
//                                            sort = "createdAt",
//                                            direction = Sort.Direction.DESC
//                                    )
//                            }
//                    )
//                    Pageable pageRequest,
//            ProjectMemberRequest req
//    ) {
//
//
//        Page<ProjectDashboardDto> projectDashboardDtos = projectService.readA(
//                pageRequest,
//                req
//        );
//
//        return projectDashboardDtos;
//    }



}