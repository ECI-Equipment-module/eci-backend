package eci.server.ProjectModule.service;

import eci.server.BomModule.repository.BomRepository;
import eci.server.BomModule.repository.PreliminaryBomRepository;
import eci.server.DashBoardModule.dto.myProject.ProjectDashboardDto;
import eci.server.DesignModule.dto.DesignCreateUpdateResponse;
import eci.server.ItemModule.dto.item.ItemProjectDashboardDto;
import eci.server.ItemModule.entity.newRoute.RouteOrdering;
import eci.server.ItemModule.exception.item.ItemNotFoundException;
import eci.server.ItemModule.exception.member.MemberNotFoundException;

import eci.server.ItemModule.exception.route.RouteNotFoundException;
import eci.server.ItemModule.repository.member.MemberRepository;
import eci.server.ItemModule.repository.newRoute.RouteOrderingRepository;
import eci.server.ItemModule.repository.newRoute.RouteProductRepository;
import eci.server.ItemModule.service.file.FileService;

import eci.server.NewItemModule.dto.newItem.create.NewItemCreateResponse;
import eci.server.NewItemModule.entity.NewItem;
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
import eci.server.ProjectModule.repository.projectAttachmentRepository.ProjectAttachmentRepository;
import eci.server.ProjectModule.repository.projectLevel.ProjectLevelRepository;
import eci.server.ProjectModule.repository.projectType.ProjectTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

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
    private final ProjectAttachmentRepository projectAttachmentRepository;

    @Value("${default.image.address}")
    private String defaultImageAddress;

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
        List<RouteOrdering> routeOrdering = routeOrderingRepository.findByNewItemOrderByIdAsc(project.getNewItem());
        //프로젝트에 딸린 라우트
        Long routeId = routeOrderingRepository.findByNewItemOrderByIdAsc(project.getNewItem()).get(routeOrdering.size()-1).getId();

        //06-17 추가 , route 에 project 등록
        RouteOrdering setRoute =
                routeOrderingRepository.findById(routeId).orElseThrow(RouteNotFoundException::new);

        setRoute.setProject(project);
        saveTrueAttachment(project);

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
        for(ProjectAttachment attachment:attachments){
            if(!attachment.isSave()){
                fileService.delete(attachment.getUniqueName());
            }
        }
    }

    @Transactional

    public ProjectTempCreateUpdateResponse update(Long id, ProjectUpdateRequest req) {

        Project project =  projectRepository.findById(id).orElseThrow(ProjectNotFoundException::new);

//        if (!project.getTempsave()){
//
//            //true면 임시저장 상태, false면 찐 저장 상태
//            //찐 저장 상태라면 UPDATE 불가, 임시저장 일때만 가능
//
//            throw new ProjectUpdateImpossibleException();
//        }

        Project.FileUpdatedResult result = project.update(
                req,
                newItemRepository,
                projectTypeRepository,
                projectLevelRepository,
                produceOrganizationRepository,
                clientOrganizationRepository,
                carTypeRepository,
                memberRepository,
                attachmentTagRepository
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

    public ProjectTempCreateUpdateResponse update2(Long id, Long NewitemId) {
        Project project =  projectRepository.findById(id).orElseThrow(ProjectNotFoundException::new);
        project.updateNewItem(NewitemId, newItemRepository);
        System.out.println("이 서비스 츠로젝트 up2date22222222222222222222222222222 ");
        return new ProjectTempCreateUpdateResponse(project.getId());
    }

    @Transactional
    public DesignCreateUpdateResponse tempEnd(
            Long id, ProjectUpdateRequest req) {

        Project project = projectRepository.findById(id).
                orElseThrow(ProjectNotFoundException::new);
//
//        if (!project.getTempsave() || project.getReadonly()) {
//            //tempsave가 false면 찐 저장 상태
//            //찐 저장 상태라면 UPDATE 불가, 임시저장 일때만 가능
//            //readonly가 true라면 수정 불가상태
//            throw new ProjectUpdateImpossibleException();
//        }

        Project.FileUpdatedResult result = project.tempEnd(
                req,
                newItemRepository,
                projectTypeRepository,
                projectLevelRepository,
                produceOrganizationRepository,
                clientOrganizationRepository,
                carTypeRepository,
                memberRepository,
                attachmentTagRepository
        );



        uploadAttachments(
                result.getAttachmentUpdatedResult().getAddedAttachments(),
                result.getAttachmentUpdatedResult().getAddedAttachmentFiles()
        );
        deleteAttachments(
                result.getAttachmentUpdatedResult().getDeletedAttachments()
        );

        //06-17 추가 , route 에 project 등록 ////////////////////////////////////
        List<RouteOrdering> routeOrdering = routeOrderingRepository.findByNewItemOrderByIdAsc(project.getNewItem());
        //프로젝트에 딸린 라우트
        Long routeId = routeOrderingRepository.findByNewItemOrderByIdAsc(project.getNewItem()).get(routeOrdering.size()-1).getId();

        RouteOrdering setRoute =
                routeOrderingRepository.findById(routeId).orElseThrow(RouteNotFoundException::new);

        setRoute.setProject(project);
        saveTrueAttachment(project);
        /////////////////////////////////////////////////////////////////////////

        return new DesignCreateUpdateResponse(id, routeId);

    }

    public Long routeIdReturn(Long newCreateItemId){

        NewItem newItemOfProject = newItemRepository.findById(newCreateItemId).orElseThrow(ItemNotFoundException::new);

        List<RouteOrdering> routeOrdering = routeOrderingRepository.findByNewItemOrderByIdAsc(newItemOfProject);

        //프로젝트에 딸린 라우트
        Long routeId = routeOrderingRepository.findByNewItemOrderByIdAsc(newItemOfProject).get(routeOrdering.size()-1).getId();

        return routeId;
    }


    // read one project
    public ProjectDto read(Long id){
        Project targetProject = projectRepository.findById(id).orElseThrow(ProjectNotFoundException::new);
        RouteOrdering routeOrdering = routeOrderingRepository.findByNewItemOrderByIdAsc(targetProject.getNewItem()).get(0);
        return ProjectDto.toDto(
                targetProject,
                routeOrdering,
                routeOrderingRepository,
                routeProductRepository,
                bomRepository,
                preliminaryBomRepository,
                attachmentTagRepository,
                defaultImageAddress
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

                        project.getProduceOrganization()==null?
                                "":
                                ProduceOrganizationDto.toDto(project.getProduceOrganization()).getName(),

                        project.getClientOrganization()==null?
                                "":
                                ClientOrganizationDto.toDto(project.getClientOrganization()).getName(),

                        project.getCarType()==null?
                                CarTypeDto.toDto()
                                : CarTypeDto.toDto(project.getCarType()),
                        project.getClientItemNumber(),

                        project.getNewItem()==null?
                                ItemProjectDashboardDto.toDto():
                                ItemProjectDashboardDto.toDto(project.getNewItem()),

                        project.getTempsave(),


                        //현재 phase의 이름
                        //0604 에러 : complete는 인덱스 길이가 10일 때 present 도 10 (인덱스에러 발생)
                        //=> 그래서 complete라면 route_name 에 route_item_complete 반환
                        routeOrderingRepository.findByNewItemOrderByIdAsc(
                                project.getNewItem()
                        ).get(
                                (
                                        routeOrderingRepository.findByNewItemOrderByIdAsc(
                                                project.getNewItem()
                                        ).size()-1
                                )
                        ).getLifecycleStatus().equals("COMPLETE")?
                                "ITEM_COMPLETE" :
                                routeProductRepository.findAllByRouteOrdering(
                                                routeOrderingRepository.findByNewItemOrderByIdAsc(
                                                        project.getNewItem()
                                                ).get(
                                                        (
                                                                (routeOrderingRepository.findByNewItemOrderByIdAsc(
                                                                        project.getNewItem()
                                                                ).size())-1 //아이템의 라우트 오더링 중에서 최신 아이
                                                        )
                                                )
                                        ).get(
                                                (
                                                        routeOrderingRepository.findByNewItemOrderByIdAsc(
                                                                project.getNewItem()
                                                        ).get(
                                                                routeOrderingRepository.findByNewItemOrderByIdAsc(
                                                                        project.getNewItem()
                                                                ).size()-1
                                                        )
                                                ).getPresent() //라우트 오더링 중에서 현재 진행중인 라우트프로덕트
                                        )
                                        .getRoute_name(),

                        routeOrderingRepository.findByNewItemOrderByIdAsc(
                                project.getNewItem()
                        ).get(
                                (
                                        routeOrderingRepository.findByNewItemOrderByIdAsc(
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

                        routeOrderingRepository.findByNewItemOrderByIdAsc(
                                project.getNewItem()
                        ).get(
                                routeOrderingRepository.findByNewItemOrderByIdAsc(
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

                        project.getProduceOrganization()==null?
                                "":
                                ProduceOrganizationDto.toDto(project.getProduceOrganization()).getName(),

                        project.getClientOrganization()==null?
                                "":
                                ClientOrganizationDto.toDto(project.getClientOrganization()).getName(),

                        project.getCarType()==null?
                                CarTypeDto.toDto()
                                : CarTypeDto.toDto(project.getCarType()),
                        project.getClientItemNumber(),

                        project.getNewItem()==null?
                                ItemProjectDashboardDto.toDto():
                                ItemProjectDashboardDto.toDto(project.getNewItem()),

                        project.getTempsave(),


                        //현재 phase의 이름

                        routeOrderingRepository.findByNewItemOrderByIdAsc(
                                        project.getNewItem()
                                ).get(
                                        routeOrderingRepository.findByNewItemOrderByIdAsc(
                                                project.getNewItem()
                                        ).size()-1
                                )//아이템의 라우트 오더링 중에서 최신 아이
                                .getPresent()
                                ==                         routeProductRepository.findAllByRouteOrdering(
                                routeOrderingRepository.findByNewItemOrderByIdAsc(
                                        project.getNewItem()
                                ).get(
                                        routeOrderingRepository.findByNewItemOrderByIdAsc(
                                                project.getNewItem()
                                        ).size()-1
                                )//아이템의 라우트 오더링 중에서 최신 라우트오더링
                        ).size()
                                ?"PROCESS COMPLETE":
                                routeProductRepository.findAllByRouteOrdering(
                                                routeOrderingRepository.findByNewItemOrderByIdAsc(
                                                        project.getNewItem()
                                                ).get(
                                                        routeOrderingRepository.findByNewItemOrderByIdAsc(
                                                                project.getNewItem()
                                                        ).size()-1
                                                )//아이템의 라우트 오더링 중에서 최신 라우트오더링
                                        ).get(
                                                routeOrderingRepository.findByNewItemOrderByIdAsc(
                                                                project.getNewItem()
                                                        ).get(
                                                                routeOrderingRepository.findByNewItemOrderByIdAsc(
                                                                        project.getNewItem()
                                                                ).size()-1
                                                        )//아이템의 라우트 오더링 중에서 최신 아이
                                                        .getPresent() //라우트 오더링 중에서 현재 진행중인 라우트프로덕트
                                        )
                                        .getRoute_name(),

                        routeOrderingRepository.findByNewItemOrderByIdAsc(
                                project.getNewItem()
                        ).get(
                                routeOrderingRepository.findByNewItemOrderByIdAsc(
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

    private void saveTrueAttachment(Project target) {
        projectAttachmentRepository.findByProject(target).
                forEach(
                        i->i.setSave(true)
                );

    }

    public NewItemCreateResponse changeProjectItemToNewMadeItem(Long targetProjId, Long newMadeItemid){

        Project targetProj = projectRepository.findById(targetProjId).orElseThrow(ProjectNotFoundException::new);
        NewItem newMadeItem = newItemRepository.findById(newMadeItemid).orElseThrow(ItemNotFoundException::new);

        // 이 타겟 프로젝트의 아이템을 지금 새로 만들어진 아이템으로 변경해주면 된다.
        //targetProj.changeItemIdOfProjectByNewMadeItem(newMadeItem);
        targetProj.setNewItem(newMadeItem);
        System.out.println("sett 했어 ");
        return new NewItemCreateResponse(targetProjId);

    }

    public NewItemCreateResponse projectUpdateToReadonlyFalseTempsaveTrue(Project project){

        project.projectUpdateToReadonlyFalseTempSaveTrue();
        return new NewItemCreateResponse(project.getId());
    }


}