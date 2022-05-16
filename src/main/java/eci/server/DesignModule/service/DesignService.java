package eci.server.DesignModule.service;

import eci.server.DashBoardModule.dto.myProject.ProjectDashboardDto;
import eci.server.DesignModule.dto.*;
import eci.server.DesignModule.entity.design.Design;
import eci.server.DesignModule.entity.designfile.DesignAttachment;
import eci.server.DesignModule.exception.DesignNotFoundException;
import eci.server.DesignModule.exception.DesignUpdateImpossibleException;
import eci.server.DesignModule.repository.DesignRepository;
import eci.server.ItemModule.dto.item.ItemProjectDashboardDto;
import eci.server.ItemModule.dto.item.ItemProjectDto;
import eci.server.ItemModule.entity.newRoute.RouteOrdering;
import eci.server.ItemModule.exception.member.MemberNotFoundException;
import eci.server.ItemModule.repository.item.ItemRepository;
import eci.server.ItemModule.repository.member.MemberRepository;
import eci.server.ItemModule.repository.newRoute.RouteOrderingRepository;
import eci.server.ItemModule.repository.newRoute.RouteProductRepository;
import eci.server.ItemModule.service.file.FileService;
import eci.server.ProjectModule.dto.carType.CarTypeDto;
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

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DesignService {

    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    private final ProjectRepository projectRepository;

    private final FileService fileService;
    private final RouteOrderingRepository routeOrderingRepository;
    private final RouteProductRepository routeProductRepository;

    private final DesignRepository designRepository;

//    public ProjectListDto readDashboardAll(ProjectReadCondition cond) {
//        return ProjectListDto.toDto(
//                projectRepository.findAllByCondition(cond)
//        );
//    }

//    public DesignListDto readDashboardAll(ProjectReadCondition cond) {
//        return ProjectListDto.toDto(
//                projectRepository.findAllByCondition(cond)
//        );
//    }


    @Transactional
    public DesignTempCreateUpdateResponse tempCreate(DesignTempCreateRequest req) {

        Design design = designRepository.save(
                DesignTempCreateRequest.toEntity(
                        req,
                        memberRepository,
                        itemRepository
                )
        );
        if (!(req.getTag().size() == 0)) {
            uploadAttachments(design.getDesignAttachments(), req.getAttachments());
        }

        return new DesignTempCreateUpdateResponse(design.getId());
    }


    @Transactional
    public DesignCreateUpdateResponse create(DesignCreateRequest req) {

        Design design = designRepository.save(
                DesignCreateRequest.toEntity(
                        req,
                        memberRepository,
                        itemRepository
                )
        );
        if (!(req.getTag().size() == 0)) {
            uploadAttachments(design.getDesignAttachments(), req.getAttachments());
        }
        List<RouteOrdering> routeOrdering = routeOrderingRepository.findByItem(design.getItem());
        //프로젝트에 딸린 라우트
        Long routeId = routeOrderingRepository.findByItem(design.getItem()).get(routeOrdering.size() - 1).getId();

        return new DesignCreateUpdateResponse(design.getId(), routeId);
    }

    private void uploadAttachments(List<DesignAttachment> attachments, List<MultipartFile> filedAttachments) {
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

    private void deleteAttachments(List<DesignAttachment> attachments) {
//        attachments.stream().forEach(i -> fileService.delete(i.getUniqueName()));
        attachments.
                stream().
                forEach(
                        i -> i.setDeleted(true)
                );
    }

    @Transactional

    public DesignTempCreateUpdateResponse update(Long id, DesignUpdateRequest req) {

        Design design = designRepository.findById(id).orElseThrow(DesignNotFoundException::new);

        if (design.getTempsave() == false) {

            //true면 임시저장 상태, false면 찐 저장 상태
            //찐 저장 상태라면 UPDATE 불가, 임시저장 일때만 가능

            throw new DesignUpdateImpossibleException();
        }

        Design.FileUpdatedResult result = design.update(
                req,
                itemRepository
        );


        uploadAttachments(
                result.getDesignUpdatedResult().getAddedAttachments(),
                result.getDesignUpdatedResult().getAddedAttachmentFiles()
        );
        deleteAttachments(
                result.getDesignUpdatedResult().getDeletedAttachments()
        );
        return new DesignTempCreateUpdateResponse(id);

    }


    // read one project
    public DesignDto read(Long id) {
        Design targetDesign = designRepository.findById(id).orElseThrow(DesignNotFoundException::new);
        return DesignDto.toDto(
                targetDesign,
                routeOrderingRepository,
                routeProductRepository
        );
    }
//
//
//    //로젝트 리스트에서 찾아노는 경우
//    public DesignListDto readAll
//    (
//            ProjectReadCondition cond,
//            ProjectMemberRequest req
//    ) {
//        return ProjectListDto.toDto(
//                projectRepository.findAllByCondition(
//                        cond
//                )
//        );
//    }
//
//    //로젝트 리스트에서 찾아노는 경우
//    public Page<ProjectSimpleDto> readPageAll
//    (
//            Pageable pageRequest,
//            ProjectMemberRequest req
//    ) {
//        Page<Project> projectListBefore = projectRepository.findAll(pageRequest);//에러
//
//        List<Project> projectListList =
//                projectListBefore.stream().filter(
//                        i -> i.getTempsave().equals(false)
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
//                        project.getStartPeriod(),
//                        project.getOverPeriod(),
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
//                                routeOrderingRepository.findByItem(project.getItem()).size() - 1
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
//
//    public Page<ProjectDashboardDto> readDashboard(
//
//            Pageable pageRequest,
//            ProjectMemberRequest req
//
//    ) {
//
//        Page<Project> projectList = projectRepository.
//                findByMember(
//                        memberRepository.findById(req.getMemberId())
//                                .orElseThrow(MemberNotFoundException::new)
//                        ,
//                        pageRequest
//                );
//
//        //이 프로젝트의 아이템의 맨 마지막 라우트 오더링의 라우트 프로덕트들 중 현재 라우트 오더링의 sequence에 해당하는 값
//
//
//        Page<ProjectDashboardDto> pagingList = projectList.map(
//                project -> new ProjectDashboardDto(
//
//                        project.getId(),
//                        project.getProjectNumber(),
//                        project.getName(),
//
//                        CarTypeDto.toDto(project.getCarType()),
//
//                        ItemProjectDashboardDto.toDto(project.getItem()),
//
//                        project.getStartPeriod(),
//                        project.getOverPeriod(),
//
//                        project.getTempsave(),
//
//                        routeOrderingRepository.findByItem(project.getItem())
//                                .get(
//                                        routeOrderingRepository.findByItem(project.getItem()).size() - 1
//                                ).getLifecycleStatus()
//                        ,
//
//                        //현재 phase의 이름
//
//                        routeProductRepository.findAllByRouteOrdering(
//                                        routeOrderingRepository.findByItem(
//                                                project.getItem()
//                                        ).get(
//                                                routeOrderingRepository.findByItem(
//                                                        project.getItem()
//                                                ).size() - 1 //아이템의 라우트 오더링 중에서 최신 아이
//                                        )
//                                ).get(
//                                        routeOrderingRepository.findByItem(
//                                                project.getItem()
//                                        ).get(
//                                                routeOrderingRepository.findByItem(
//                                                        project.getItem()
//                                                ).size() - 1
//                                        ).getPresent() //라우트 오더링 중에서 현재 진행중인 라우트프로덕트
//                                )
//                                .getRoute_name(),
//
//
//                        //현재 phase의 sequence가
//
//                        (double) routeOrderingRepository.findByItem(
//                                project.getItem()
//                        ).get(
//                                routeOrderingRepository.findByItem(
//                                        project.getItem()
//                                ).size() - 1
//                        ).getPresent() //라우트 오더링 중에서 현재 진행중인 라우트프로덕트
//                                /
//                                routeProductRepository.findAllByRouteOrdering(
//                                        routeOrderingRepository.findByItem(
//                                                project.getItem()
//                                        ).get(
//                                                routeOrderingRepository.findByItem(
//                                                        project.getItem()
//                                                ).size() - 1 //아이템의 라우트 오더링 중에서 최신 아이
//                                        )
//                                ).size(),
//
//                        project.getCreatedAt()
//                )
//        );
//
//        return pagingList;
//
//    }

    //delete one project

    @Transactional
    public void delete(Long id) {
        Design design = designRepository.findById(id).orElseThrow(DesignNotFoundException::new);
        deleteDesignAttachments(design.getDesignAttachments());
        designRepository.delete(design);
    }

    private void deleteDesignAttachments(List<DesignAttachment> designAttachments) {
        designAttachments.forEach(i -> fileService.delete(i.getUniqueName()));
    }

}