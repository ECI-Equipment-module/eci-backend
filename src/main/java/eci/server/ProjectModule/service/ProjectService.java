package eci.server.ProjectModule.service;

import eci.server.BomModule.repository.BomRepository;
import eci.server.BomModule.repository.PreliminaryBomRepository;
import eci.server.CRCOModule.dto.co.CoUpdateRequest;
import eci.server.CRCOModule.entity.co.ChangeOrder;
import eci.server.CRCOModule.entity.cofeatures.CoAttachment;
import eci.server.CRCOModule.service.co.CoService;
import eci.server.DashBoardModule.dto.myProject.ProjectDashboardDto;
import eci.server.DesignModule.dto.DesignCreateUpdateResponse;
import eci.server.ItemModule.dto.item.ItemProjectDashboardDto;
import eci.server.ItemModule.dto.item.ItemUpdateRequest;
import eci.server.ItemModule.dto.item.ItemUpdateResponse;
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
import eci.server.ReleaseModule.entity.ReleaseAttachment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
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
        if(!(req.getAttachments()==null || req.getAttachments().size()==0)) {
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
        if(!(req.getAttachments()==null || req.getAttachments().size()==0)) {
            uploadAttachments(project.getProjectAttachments(), req.getAttachments());
        }
        List<RouteOrdering> routeOrdering = routeOrderingRepository.findByNewItemOrderByIdAsc(project.getNewItem());
        //??????????????? ?????? ?????????
        Long routeId = routeOrderingRepository.findByNewItemOrderByIdAsc(project.getNewItem()).get(routeOrdering.size()-1).getId();

        //06-17 ?????? , route ??? project ??????
        RouteOrdering setRoute =
                routeOrderingRepository.findById(routeId).orElseThrow(RouteNotFoundException::new);

        setRoute.setProject(project);
        saveTrueAttachment(project);

        return new ProjectCreateUpdateResponse(project.getId(), routeId);
    }


    @Transactional

    public ItemUpdateResponse update(Long id, ProjectUpdateRequest req) {

        Project project =  projectRepository.findById(id).orElseThrow(ProjectNotFoundException::new);

//        if (!project.getTempsave()){
//
//            //true??? ???????????? ??????, false??? ??? ?????? ??????
//            //??? ?????? ???????????? UPDATE ??????, ???????????? ????????? ??????
//
//            throw new ProjectUpdateImpossibleException();
//        }
        List<Long> oldTags = produceOldNewTagComment(project, req).getOldTag();
        List<Long> newTags = produceOldNewTagComment(project, req).getNewTag();
        List<String> oldComment = produceOldNewTagComment(project, req).getOldComment();
        List<String> newComment =produceOldNewTagComment(project, req).getNewComment();
        List<ProjectAttachment> targetAttachmentsForTagAndComment
                = produceOldNewTagComment(project, req).getTargetAttachmentsForTagAndComment();


        Project.FileUpdatedResult result = project.update(
                req,
                newItemRepository,
                projectTypeRepository,
                projectLevelRepository,
                produceOrganizationRepository,
                clientOrganizationRepository,
                carTypeRepository,
                memberRepository,
                attachmentTagRepository,

                oldTags,
                newTags,
                oldComment,
                newComment,

                targetAttachmentsForTagAndComment

        );


        uploadAttachments(
                result.getAttachmentUpdatedResult().getAddedAttachments(),
                result.getAttachmentUpdatedResult().getAddedAttachmentFiles()
        );
        deleteAttachments(
                result.getAttachmentUpdatedResult().getDeletedAttachments()
        );

        Long routeId = -1L;
        if(routeOrderingRepository.findByNewItemOrderByIdAsc(project.getNewItem()).size()>0) {
            RouteOrdering routeOrdering = routeOrderingRepository
                    .findByNewItemOrderByIdAsc(project.getNewItem()).get
                    (
                            routeOrderingRepository.findByNewItemOrderByIdAsc(project.getNewItem()).size()-1
                    );
            routeId = routeOrdering.getId();
        }

        return new ItemUpdateResponse(id, routeId);

    }

    public ItemUpdateResponse update2(Long id, Long NewitemId) {
        Project project =  projectRepository.findById(id).orElseThrow(ProjectNotFoundException::new);
        project.updateNewItem(NewitemId, newItemRepository);


        Long routeId = -1L;
        if(routeOrderingRepository.findByNewItemOrderByIdAsc(project.getNewItem()).size()>0) {
            RouteOrdering routeOrdering = routeOrderingRepository
                    .findByNewItemOrderByIdAsc(project.getNewItem()).get
                            (
                                    routeOrderingRepository.findByNewItemOrderByIdAsc(project.getNewItem()).size()-1
                            );
            routeId = routeOrdering.getId();
        }

        return new ItemUpdateResponse(project.getId(), routeId);
    }

    @Transactional
    public ItemUpdateResponse tempEnd(
            Long id, ProjectUpdateRequest req) {

        Project project = projectRepository.findById(id).
                orElseThrow(ProjectNotFoundException::new);
//
//        if (!project.getTempsave() || project.getReadonly()) {
//            //tempsave??? false??? ??? ?????? ??????
//            //??? ?????? ???????????? UPDATE ??????, ???????????? ????????? ??????
//            //readonly??? true?????? ?????? ????????????
//            throw new ProjectUpdateImpossibleException();
//        }
        List<Long> oldTags = produceOldNewTagComment(project, req).getOldTag();
        List<Long> newTags = produceOldNewTagComment(project, req).getNewTag();
        List<String> oldComment = produceOldNewTagComment(project, req).getOldComment();
        List<String> newComment =produceOldNewTagComment(project, req).getNewComment();
        List<ProjectAttachment> targetAttachmentsForTagAndComment
                = produceOldNewTagComment(project, req).getTargetAttachmentsForTagAndComment();

        Project.FileUpdatedResult result = project.tempEnd(
                req,
                newItemRepository,
                projectTypeRepository,
                projectLevelRepository,
                produceOrganizationRepository,
                clientOrganizationRepository,
                carTypeRepository,
                memberRepository,
                attachmentTagRepository,

                oldTags,
                newTags,
                oldComment,
                newComment,

                targetAttachmentsForTagAndComment
        );



        uploadAttachments(
                result.getAttachmentUpdatedResult().getAddedAttachments(),
                result.getAttachmentUpdatedResult().getAddedAttachmentFiles()
        );
        deleteAttachments(
                result.getAttachmentUpdatedResult().getDeletedAttachments()
        );

        //06-17 ?????? , route ??? project ?????? ////////////////////////////////////
        List<RouteOrdering> routeOrdering = routeOrderingRepository.findByNewItemOrderByIdAsc(project.getNewItem());
        //??????????????? ?????? ?????????
        Long routeId = routeOrderingRepository.findByNewItemOrderByIdAsc(project.getNewItem()).get(routeOrdering.size()-1).getId();

        RouteOrdering setRoute =
                routeOrderingRepository.findById(routeId).orElseThrow(RouteNotFoundException::new);

        setRoute.setProject(project);
        saveTrueAttachment(project);
        /////////////////////////////////////////////////////////////////////////

        return new ItemUpdateResponse(id, routeId);

    }

    public Long routeIdReturn(Long newCreateItemId){

        NewItem newItemOfProject = newItemRepository.findById(newCreateItemId).orElseThrow(ItemNotFoundException::new);

        List<RouteOrdering> routeOrdering = routeOrderingRepository.findByNewItemOrderByIdAsc(newItemOfProject);

        //??????????????? ?????? ?????????
        Long routeId = routeOrderingRepository.findByNewItemOrderByIdAsc(newItemOfProject).get(routeOrdering.size()-1).getId();

        return routeId;
    }


    // read one project

    public ProjectDto read(Long id){
        Project targetProject = projectRepository.findById(id).orElseThrow(ProjectNotFoundException::new);


        RouteOrdering routeOrdering = routeOrderingRepository
                .findByNewItemOrderByIdAsc(targetProject.getNewItem())
                .get(routeOrderingRepository
                        .findByNewItemOrderByIdAsc(targetProject.getNewItem())
                        .size()-1);


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



    //????????? ??????????????? ???????????? ??????
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

        Page<Project> projectListBefore = projectRepository.findAll(pageRequest);//??????

        List<Project> projectListList =
                projectListBefore.stream().filter(
                        i->i.getTempsave().equals(false)
                ).collect(Collectors.toList());

        List<Project> workingProjectListList =
                projectListList.stream().filter(
                        i->i.getLifecycle().equals("WORKING") //COMPLETE??? ????????? ??????
                ).collect(Collectors.toList());

        List<Project> completeProjectListList =
                projectListList.stream().filter(
                        i->i.getLifecycle().equals("COMPLETE") //COMPLETE??? ????????? ??????
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


                        //?????? phase??? ??????
                        //0604 ?????? : complete??? ????????? ????????? 10??? ??? present ??? 10 (??????????????? ??????)
                        //=> ????????? complete?????? route_name ??? route_item_complete ??????
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
                                                                ).size())-1 //???????????? ????????? ????????? ????????? ?????? ??????
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
                                                ).getPresent() //????????? ????????? ????????? ?????? ???????????? ?????????????????????
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
                        ).getLifecycleStatus(),//????????? ????????? ????????? ?????? ???????????? ?????????????????????

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
                        ).getLifecycleStatus(),//????????? ????????? ????????? ?????? ???????????? ?????????????????????
                        //?????? phase??? sequence???
//
//                        (double) routeOrderingRepository.findByItem(
//                                                project.getItem()
//                                        ).get(
//                                                routeOrderingRepository.findByItem(
//                                                        project.getItem()
//                                                ).size()-1
//                                        ).getPresent() //????????? ????????? ????????? ?????? ???????????? ?????????????????????
//                                /
//                                routeProductRepository.findAllByRouteOrdering(
//                                routeOrderingRepository.findByItem(
//                                        project.getItem()
//                                ).get(
//                                        routeOrderingRepository.findByItem(
//                                                project.getItem()
//                                        ).size()-1 //???????????? ????????? ????????? ????????? ?????? ??????
//                                )
//                        ).size(),

                        project.getCreatedAt()
                )
        );

        return workingPagingList;

    }
//    //????????? ??????????????? ???????????? ??????
//    public Page<ProjectSimpleDto> readPageAll
//    (
//            Pageable pageRequest,
//            ProjectMemberRequest req
//    ){
//        Page<Project> projectListBefore = projectRepository.findAll(pageRequest);//??????
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
//                        //tag??? ??????
//                        project.getProjectAttachments().stream().filter(
//                                        a -> a.getTag().equals("DEVELOP")
//                                ).collect(Collectors.toList())
//                                .stream().map(
//                                        ProjectAttachment::getAttachmentaddress
//                                ).collect(Collectors.toList()),
//
//                        //tag??? ?????????
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
//                        //project.getLifecycle(), //??????????????? ??????????????????
//
//                        routeOrderingRepository.findByItem(project.getItem()).get(
//                                routeOrderingRepository.findByItem(project.getItem()).size()-1
//                        ).getLifecycleStatus(),
//                        //???????????? ????????? ?????????
//
//                        req.getMemberId().equals(project.getMember().getId())
//                        //?????? ????????? ??? ?????? ??????????????? ????????? readonly==true
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

        //??? ??????????????? ???????????? ??? ????????? ????????? ???????????? ????????? ??????????????? ??? ?????? ????????? ???????????? sequence??? ???????????? ???


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


                        //?????? phase??? ??????

                        routeOrderingRepository.findByNewItemOrderByIdAsc(
                                        project.getNewItem()
                                ).get(
                                        routeOrderingRepository.findByNewItemOrderByIdAsc(
                                                project.getNewItem()
                                        ).size()-1
                                )//???????????? ????????? ????????? ????????? ?????? ??????
                                .getPresent()
                                ==                         routeProductRepository.findAllByRouteOrdering(
                                routeOrderingRepository.findByNewItemOrderByIdAsc(
                                        project.getNewItem()
                                ).get(
                                        routeOrderingRepository.findByNewItemOrderByIdAsc(
                                                project.getNewItem()
                                        ).size()-1
                                )//???????????? ????????? ????????? ????????? ?????? ??????????????????
                        ).size()
                                ?"PROCESS COMPLETE":
                                routeProductRepository.findAllByRouteOrdering(
                                                routeOrderingRepository.findByNewItemOrderByIdAsc(
                                                        project.getNewItem()
                                                ).get(
                                                        routeOrderingRepository.findByNewItemOrderByIdAsc(
                                                                project.getNewItem()
                                                        ).size()-1
                                                )//???????????? ????????? ????????? ????????? ?????? ??????????????????
                                        ).get(
                                                routeOrderingRepository.findByNewItemOrderByIdAsc(
                                                                project.getNewItem()
                                                        ).get(
                                                                routeOrderingRepository.findByNewItemOrderByIdAsc(
                                                                        project.getNewItem()
                                                                ).size()-1
                                                        )//???????????? ????????? ????????? ????????? ?????? ??????
                                                        .getPresent() //????????? ????????? ????????? ?????? ???????????? ?????????????????????
                                        )
                                        .getRoute_name(),

                        routeOrderingRepository.findByNewItemOrderByIdAsc(
                                project.getNewItem()
                        ).get(
                                routeOrderingRepository.findByNewItemOrderByIdAsc(
                                        project.getNewItem()
                                ).size()-1
                        ).getLifecycleStatus(),//????????? ????????? ????????? ?????? ???????????? ?????????????????????
                        //?????? phase??? sequence???

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


    public NewItemCreateResponse changeProjectItemToNewMadeItem(Long targetProjId, Long newMadeItemid){

        Project targetProj = projectRepository.findById(targetProjId).orElseThrow(ProjectNotFoundException::new);
        NewItem newMadeItem = newItemRepository.findById(newMadeItemid).orElseThrow(ItemNotFoundException::new);

        // ??? ?????? ??????????????? ???????????? ?????? ?????? ???????????? ??????????????? ??????????????? ??????.
        //targetProj.changeItemIdOfProjectByNewMadeItem(newMadeItem);
        targetProj.setNewItem(newMadeItem);
        System.out.println("sett ?????? ");
        return new NewItemCreateResponse(targetProjId);

    }

    public NewItemCreateResponse projectUpdateToReadonlyFalseTempsaveTrue(Project project){

        project.projectUpdateToReadonlyFalseTempSaveTrue();
        return new NewItemCreateResponse(project.getId());
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

    private void uploadAttachments(List<ProjectAttachment> attachments, List<MultipartFile> filedAttachments) {
        // ?????? ????????? ????????? ????????? ?????? Multipart ?????????
        // ????????? ????????? unique name ??? ??????????????? ?????? ??????????????? ?????????
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

    @Getter
    @AllArgsConstructor
    public static class OldNewTagCommentUpdatedResult {
        private List<Long> oldTag;
        private List<Long> newTag;
        private List<String> oldComment;
        private List<String> newComment;

        private List<ProjectAttachment> targetAttachmentsForTagAndComment;
    }

    public ProjectService.OldNewTagCommentUpdatedResult produceOldNewTagComment(
            Project entity, //update ????????? ?????????
            ProjectUpdateRequest req
    ) {
        List<Long> oldDocTag = new ArrayList<>();
        List<Long> newDocTag = new ArrayList<>();
        List<String> oldDocComment = new ArrayList<>();
        List<String> newDocComment = new ArrayList<>();

        List<ProjectAttachment> attachments
                = entity.getProjectAttachments();

        List<ProjectAttachment> targetAttachmentsForTagAndComment = new ArrayList<>();

        // OLD TAG, NEW TAG, COMMENT OLD NEW GENERATE
        if (attachments.size() > 0) {
            // ?????? ?????? ?????? ??????(?????? ??????, ????????? ????????? ???) == ?????? deleted ????????? - deleted=true ??? ??????

            // ????????? ??? ?????????
            List<ProjectAttachment> oldAttachments = entity.getProjectAttachments();
            for (ProjectAttachment attachment : oldAttachments) {

                if (
                        (!attachment.isDeleted())
                    // (1) ????????? ?????? : DELETED = FALSE (??????, ????????? ?????? ???????????? ?????? ??????????????? ??????)
                ) {
                    if(req.getDeletedAttachments() != null){ // (1-1) ?????? ???????????? delete ??? ???????????? ???????????????
                        if(!(req.getDeletedAttachments().contains(attachment.getId()))){
                            // ??? delete ?????? ??? attachment ????????? ???????????? ?????? ?????? ??????
                            targetAttachmentsForTagAndComment.add(attachment);
                        }
                    }
                    else{ //??? ????????? delete ????????? ????????? ??? ???????????? ??????
                        targetAttachmentsForTagAndComment.add(attachment);
                    }

                }

            }

            int standardIdx = targetAttachmentsForTagAndComment.size();

            oldDocTag.addAll(req.getAddedTag().subList(0, standardIdx));
            newDocTag.addAll(req.getAddedTag().subList(standardIdx, req.getAddedTag().size()));

            oldDocComment.addAll(req.getAddedAttachmentComment().subList(0, standardIdx));
            newDocComment.addAll(req.getAddedAttachmentComment().subList(standardIdx, req.getAddedTag().size()));

        }
        return new OldNewTagCommentUpdatedResult(

                oldDocTag,
                newDocTag,
                oldDocComment,
                newDocComment,

                targetAttachmentsForTagAndComment
        );
    }


}