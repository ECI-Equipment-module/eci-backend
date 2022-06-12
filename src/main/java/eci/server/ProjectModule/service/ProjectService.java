package eci.server.ProjectModule.service;

import eci.server.BomModule.repository.BomRepository;
import eci.server.BomModule.repository.PreliminaryBomRepository;
import eci.server.DashBoardModule.dto.myProject.ProjectDashboardDto;
import eci.server.ItemModule.dto.item.ItemProjectDashboardDto;

import eci.server.ItemModule.entity.newRoute.RouteOrdering;

import eci.server.ItemModule.exception.member.MemberNotFoundException;

import eci.server.ItemModule.repository.member.MemberRepository;
import eci.server.ItemModule.repository.newRoute.RouteOrderingRepository;
import eci.server.ItemModule.repository.newRoute.RouteProductRepository;
import eci.server.ItemModule.service.file.FileService;

import eci.server.NewItemModule.repository.attachment.AttachmentTagRepository;
import eci.server.NewItemModule.repository.item.NewItemRepository;
import eci.server.ProjectModule.dto.carType.CarTypeDto;
import eci.server.ProjectModule.dto.clientOrg.ClientOrganizationDto;
import eci.server.ProjectModule.dto.produceOrg.ProduceOrganizationDto;
import eci.server.ProjectModule.dto.project.*;
import eci.server.ProjectModule.entity.project.Project;
import eci.server.ProjectModule.entity.projectAttachment.ProjectAttachment;
import eci.server.ProjectModule.exception.ProjectNotFoundException;
import eci.server.ProjectModule.exception.ProjectUpdateImpossibleException;

import eci.server.ProjectModule.repository.carType.CarTypeRepository;
import eci.server.ProjectModule.repository.clientOrg.ClientOrganizationRepository;
import eci.server.ProjectModule.repository.produceOrg.ProduceOrganizationRepository;
import eci.server.ProjectModule.repository.project.ProjectRepository;
import eci.server.ProjectModule.repository.projectLevel.ProjectLevelRepository;
import eci.server.ProjectModule.repository.projectType.ProjectTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProjectService {

    private final MemberRepository memberRepository;
    private final NewItemRepository newItemRepository;
    private final ProjectTypeRepository projectTypeRepository;
    private final ProjectRepository projectRepository;
    private final ProjectLevelRepository projectLevelRepository;
    private final ProduceOrganizationRepository produceOrganizationRepository;
    private final ClientOrganizationRepository clientOrganizationRepository;
    private final FileService fileService;
    private final RouteOrderingRepository routeOrderingRepository;
    private final RouteProductRepository routeProductRepository;
    private final CarTypeRepository carTypeRepository;
    private final AttachmentTagRepository attachmentTagRepository;
    private final BomRepository bomRepository;
    private final PreliminaryBomRepository preliminaryBomRepository;

//    public ProjectListDto readDashboardAll(ProjectReadCondition cond) {
//        return ProjectListDto.toDto(
//                projectRepository.findAllByCondition(cond)
//        );
//    }

    public ProjectListDto readDashboardAll(ProjectReadCondition cond) {
        return ProjectListDto.toDto(
                projectRepository.findAllByCondition(cond)
        );
    }


    @Transactional
    public ProjectTempCreateUpdateResponse tempCreate(ProjectTemporaryCreateRequest req) {

        Project project = projectRepository.save(
                ProjectTemporaryCreateRequest.toEntity(
                        req,
                        memberRepository,
                        newItemRepository,
                        projectTypeRepository,
                        projectLevelRepository,
                        produceOrganizationRepository,
                        clientOrganizationRepository,
                        carTypeRepository,
                        attachmentTagRepository
                )
        );
        if(!(req.getTag().size()==0)) {
            uploadAttachments(project.getProjectAttachments(), req.getAttachments());
        }

        return new ProjectTempCreateUpdateResponse(project.getId());
    }


    @Transactional
    public ProjectCreateUpdateResponse create(ProjectCreateRequest req) {

        Project project = projectRepository.save(
                ProjectCreateRequest.toEntity(
                        req,
                        memberRepository,
                        newItemRepository,
                        projectTypeRepository,
                        projectLevelRepository,
                        produceOrganizationRepository,
                        clientOrganizationRepository,
                        carTypeRepository,
                        attachmentTagRepository
                )
        );
        if(!(req.getTag().size()==0)) {
            uploadAttachments(project.getProjectAttachments(), req.getAttachments());
        }
        List<RouteOrdering> routeOrdering = routeOrderingRepository.findByNewItem(project.getNewItem());
        //프로젝트에 딸린 라우트
        Long routeId = routeOrderingRepository.findByNewItem(project.getNewItem()).get(routeOrdering.size()-1).getId();

        return new ProjectCreateUpdateResponse(project.getId(), routeId);
    }

    private void uploadAttachments(List<ProjectAttachment> attachments, List<MultipartFile> filedAttachments) {
        // 실제 이미지 파일을 가지고 있는 Multipart 파일을
        // 파일이 가지는 unique name 을 파일명으로 해서 파일저장소 업로드
        IntStream.range(0, attachments.size())
                .forEach(
                        i -> fileService.upload
                                (
                                        filedAttachments.get(i),
                                        attachments.get(i).getUniqueName()
                                )
                );
    }

    private void deleteAttachments(List<ProjectAttachment> attachments) {
//        attachments.stream().forEach(i -> fileService.delete(i.getUniqueName()));
        attachments.
                forEach(
                        i -> i.setDeleted(true)
                );
        attachments.
                forEach(
                        i -> i.setModifiedAt(LocalDateTime.now())
                );
    }

    @Transactional

    public ProjectTempCreateUpdateResponse update(Long id, ProjectUpdateRequest req) {

        Project project =  projectRepository.findById(id).orElseThrow(ProjectNotFoundException::new);

        if (!project.getTempsave()){

            //true면 임시저장 상태, false면 찐 저장 상태
            //찐 저장 상태라면 UPDATE 불가, 임시저장 일때만 가능

            throw new ProjectUpdateImpossibleException();
        }

        Project.FileUpdatedResult result = project.update(
                req,
                newItemRepository,
                projectTypeRepository,
                projectLevelRepository,
                produceOrganizationRepository,
                clientOrganizationRepository,
                carTypeRepository,
                memberRepository
        );


        uploadAttachments(
                result.getAttachmentUpdatedResult().getAddedAttachments(),
                result.getAttachmentUpdatedResult().getAddedAttachmentFiles()
        );
        deleteAttachments(
                result.getAttachmentUpdatedResult().getDeletedAttachments()
        );
        return new ProjectTempCreateUpdateResponse(id);

    }


    // read one project
    public ProjectDto read(Long id){
        Project targetProject = projectRepository.findById(id).orElseThrow(ProjectNotFoundException::new);
        return ProjectDto.toDto(
                targetProject,
                routeOrderingRepository,
                routeProductRepository,
                bomRepository,
                preliminaryBomRepository,
                attachmentTagRepository
                );
    }



    //로젝트 리스트에서 찾아노는 경우
    public ProjectListDto readAll
            (
                    ProjectReadCondition cond,
                    ProjectMemberRequest req
            ){
        return ProjectListDto.toDto(
                projectRepository.findAllByCondition(
                        cond
                )
        );
    }

    public Page<ProjectDashboardDto> readPageAll(

            Pageable pageRequest,
            ProjectMemberRequest req

    ){

        Page<Project> projectListBefore = projectRepository.findAll(pageRequest);//에러

        List<Project> projectListList =
                projectListBefore.stream().filter(
                        i->i.getTempsave().equals(false)
                ).collect(Collectors.toList());

        List<Project> workingProjectListList =
                projectListList.stream().filter(
                        i->i.getLifecycle().equals("WORKING") //COMPLETE인 애들은 따로
                        ).collect(Collectors.toList());

        List<Project> completeProjectListList =
                projectListList.stream().filter(
                        i->i.getLifecycle().equals("COMPLETE") //COMPLETE인 애들은 따로
                ).collect(Collectors.toList());

        Page<Project> workingProjectList = new PageImpl<>(workingProjectListList);
        Page<Project> completeProjectList = new PageImpl<>(completeProjectListList);

        Page<ProjectDashboardDto> workingPagingList = workingProjectList.map(
                project -> new ProjectDashboardDto(

                        project.getId(),
                        project.getProjectNumber(),
                        project.getName(),

                        project.getProjectType().getName(),
                        project.getProjectLevel().getName(),

                        ProduceOrganizationDto.toDto(project.getProduceOrganization()).getName(),
                        ClientOrganizationDto.toDto(project.getClientOrganization()).getName(),
                        CarTypeDto.toDto(project.getCarType()),

                        project.getClientItemNumber(),

                        ItemProjectDashboardDto.toDto(project.getNewItem()),

                        project.getTempsave(),


                        //현재 phase의 이름
                        //0604 에러 : complete는 인덱스 길이가 10일 때 present 도 10 (인덱스에러 발생)
                        //=> 그래서 complete라면 route_name 에 route_item_complete 반환
                        routeOrderingRepository.findByNewItem(
                                project.getNewItem()
                        ).get(
                                (
                                        routeOrderingRepository.findByNewItem(
                                                project.getNewItem()
                                        ).size()-1
                                )
                        ).getLifecycleStatus().equals("COMPLETE")?
                        "ITEM_COMPLETE" :
                        routeProductRepository.findAllByRouteOrdering(
                                        routeOrderingRepository.findByNewItem(
                                                project.getNewItem()
                                        ).get(
                                                (
                                                        (routeOrderingRepository.findByNewItem(
                                                        project.getNewItem()
                                                ).size())-1 //아이템의 라우트 오더링 중에서 최신 아이
                                                )
                                        )
                                ).get(
                                (
                                        routeOrderingRepository.findByNewItem(
                                                project.getNewItem()
                                        ).get(
                                                routeOrderingRepository.findByNewItem(
                                                        project.getNewItem()
                                                ).size()-1
                                        )
                                        ).getPresent() //라우트 오더링 중에서 현재 진행중인 라우트프로덕트
                                )
                                .getRoute_name(),

                        routeOrderingRepository.findByNewItem(
                                project.getNewItem()
                        ).get(
                                (
                                routeOrderingRepository.findByNewItem(
                                        project.getNewItem()
                                ).size()-1
                                )
                        ).getLifecycleStatus(),//라우트 오더링 중에서 현재 진행중인 라우트프로덕트

                        project.getCreatedAt()
                )
        );


        Page<ProjectDashboardDto> completePagingList = completeProjectList.map(
                project -> new ProjectDashboardDto(

                        project.getId(),
                        project.getProjectNumber(),
                        project.getName(),

                        project.getProjectType().getName(),
                        project.getProjectLevel().getName(),

                        ProduceOrganizationDto.toDto(project.getProduceOrganization()).getName(),
                        ClientOrganizationDto.toDto(project.getClientOrganization()).getName(),
                        CarTypeDto.toDto(project.getCarType()),

                        project.getClientItemNumber(),

                        ItemProjectDashboardDto.toDto(project.getNewItem()),

//                        project.getStartPeriod(),
//                        project.getOverPeriod(),

                        project.getTempsave(),


                        "complete",

                        routeOrderingRepository.findByNewItem(
                                project.getNewItem()
                        ).get(
                                routeOrderingRepository.findByNewItem(
                                        project.getNewItem()
                                ).size()-1
                        ).getLifecycleStatus(),//라우트 오더링 중에서 현재 진행중인 라우트프로덕트
                        //현재 phase의 sequence가
//
//                        (double) routeOrderingRepository.findByItem(
//                                                project.getItem()
//                                        ).get(
//                                                routeOrderingRepository.findByItem(
//                                                        project.getItem()
//                                                ).size()-1
//                                        ).getPresent() //라우트 오더링 중에서 현재 진행중인 라우트프로덕트
//                                /
//                                routeProductRepository.findAllByRouteOrdering(
//                                routeOrderingRepository.findByItem(
//                                        project.getItem()
//                                ).get(
//                                        routeOrderingRepository.findByItem(
//                                                project.getItem()
//                                        ).size()-1 //아이템의 라우트 오더링 중에서 최신 아이
//                                )
//                        ).size(),

                        project.getCreatedAt()
                )
        );



        return workingPagingList;

    }
//    //로젝트 리스트에서 찾아노는 경우
//    public Page<ProjectSimpleDto> readPageAll
//    (
//            Pageable pageRequest,
//            ProjectMemberRequest req
//    ){
//        Page<Project> projectListBefore = projectRepository.findAll(pageRequest);//에러
//
//        List<Project> projectListList =
//                projectListBefore.stream().filter(
//                        i->i.getTempsave().equals(false)
//                ).collect(Collectors.toList());
//
//        Page<Project> projectList = new PageImpl<>(projectListList);
//
//        Page<ProjectSimpleDto> pagingList = projectList.map(
//                project -> new ProjectSimpleDto(
//
//                        project.getId(),
//                        project.getProjectNumber(),
//                        project.getName(),
//                        CarTypeDto.toDto(project.getCarType()),
//
//                        ItemProjectDto.toDto(project.getItem()),
//
//                        project.getRevision(),
//                        project.getProtoStartPeriod(),
//                        project.getProtoOverPeriod(),
//
//                        project.getTempsave(),
//
//                        //tag가 개발
//                        project.getProjectAttachments().stream().filter(
//                                        a -> a.getTag().equals("DEVELOP")
//                                ).collect(Collectors.toList())
//                                .stream().map(
//                                        ProjectAttachment::getAttachmentaddress
//                                ).collect(Collectors.toList()),
//
//                        //tag가 디자인
//                        project.getProjectAttachments().stream().filter(
//                                        a -> a.getTag().equals("DESIGN")
//                                ).collect(Collectors.toList())
//                                .stream().map(
//                                        ProjectAttachment::getAttachmentaddress
//                                ).collect(Collectors.toList()),
//
//
//                        project.getCreatedAt(),
//
//                        //project.getLifecycle(), //프로젝트의 라이프사이클
//
//                        routeOrderingRepository.findByItem(project.getItem()).get(
//                                routeOrderingRepository.findByItem(project.getItem()).size()-1
//                        ).getLifecycleStatus(),
//                        //아이템의 라이프 사이클
//
//                        req.getMemberId().equals(project.getMember().getId())
//                        //현재 로그인 된 플젝 작성멤버랑 같으면 readonly==true
//
//                )
//        );
//        return pagingList;
//    }

    public Page<ProjectDashboardDto> readDashboard(

            Pageable pageRequest,
            ProjectMemberRequest req

    ){

        Page<Project> projectList = projectRepository.
                findByMember(
                        memberRepository.findById(req.getMemberId())
                                .orElseThrow(MemberNotFoundException::new)
                        ,
                        pageRequest
                );

        //이 프로젝트의 아이템의 맨 마지막 라우트 오더링의 라우트 프로덕트들 중 현재 라우트 오더링의 sequence에 해당하는 값


        Page<ProjectDashboardDto> pagingList = projectList.map(
                project -> new ProjectDashboardDto(

                        project.getId(),
                        project.getProjectNumber(),
                        project.getName(),

                        project.getProjectType().getName(),
                        project.getProjectLevel().getName(),

                        ProduceOrganizationDto.toDto(project.getProduceOrganization()).getName(),
                        ClientOrganizationDto.toDto(project.getClientOrganization()).getName(),

                        CarTypeDto.toDto(project.getCarType()),
                        project.getClientItemNumber(),

                        ItemProjectDashboardDto.toDto(project.getNewItem()),

                        project.getTempsave(),


                        //현재 phase의 이름

                        routeOrderingRepository.findByNewItem(
                                        project.getNewItem()
                                ).get(
                                        routeOrderingRepository.findByNewItem(
                                                project.getNewItem()
                                        ).size()-1
                                )//아이템의 라우트 오더링 중에서 최신 아이
                                .getPresent()
                ==                         routeProductRepository.findAllByRouteOrdering(
                                routeOrderingRepository.findByNewItem(
                                        project.getNewItem()
                                ).get(
                                        routeOrderingRepository.findByNewItem(
                                                project.getNewItem()
                                        ).size()-1
                                )//아이템의 라우트 오더링 중에서 최신 라우트오더링
                        ).size()
                                ?"PROCESS COMPLETE":
                        routeProductRepository.findAllByRouteOrdering(
                                        routeOrderingRepository.findByNewItem(
                                                project.getNewItem()
                                        ).get(
                                                routeOrderingRepository.findByNewItem(
                                                        project.getNewItem()
                                                ).size()-1
                                        )//아이템의 라우트 오더링 중에서 최신 라우트오더링
                                ).get(
                                        routeOrderingRepository.findByNewItem(
                                                        project.getNewItem()
                                                ).get(
                                                        routeOrderingRepository.findByNewItem(
                                                                project.getNewItem()
                                                        ).size()-1
                                                )//아이템의 라우트 오더링 중에서 최신 아이
                                       .getPresent() //라우트 오더링 중에서 현재 진행중인 라우트프로덕트
                                )
                                .getRoute_name(),

                        routeOrderingRepository.findByNewItem(
                                                project.getNewItem()
                                        ).get(
                                                routeOrderingRepository.findByNewItem(
                                                        project.getNewItem()
                                                ).size()-1
                                        ).getLifecycleStatus(),//라우트 오더링 중에서 현재 진행중인 라우트프로덕트
                        //현재 phase의 sequence가
//
//                        (double) routeOrderingRepository.findByItem(
//                                                project.getItem()
//                                        ).get(
//                                                routeOrderingRepository.findByItem(
//                                                        project.getItem()
//                                                ).size()-1
//                                        ).getPresent() //라우트 오더링 중에서 현재 진행중인 라우트프로덕트
//                                /
//                                routeProductRepository.findAllByRouteOrdering(
//                                routeOrderingRepository.findByItem(
//                                        project.getItem()
//                                ).get(
//                                        routeOrderingRepository.findByItem(
//                                                project.getItem()
//                                        ).size()-1 //아이템의 라우트 오더링 중에서 최신 아이
//                                )
//                        ).size(),

                        project.getCreatedAt()
                )
        );

        return pagingList;

    }

     //delete one project

    @Transactional
    public void delete(Long id) {
        Project project = projectRepository.findById(id).orElseThrow(ProjectNotFoundException::new);
        deleteProjectAttachments(project.getProjectAttachments());
        projectRepository.delete(project);
    }

    private void deleteProjectAttachments(List<ProjectAttachment> projectAttachments) {
        projectAttachments.forEach(i -> fileService.delete(i.getUniqueName()));
    }



}
